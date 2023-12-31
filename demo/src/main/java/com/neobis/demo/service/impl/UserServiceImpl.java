package com.neobis.demo.service.impl;

import com.neobis.demo.entity.ActivationToken;
import com.neobis.demo.entity.Role;
import com.neobis.demo.entity.User;
import com.neobis.demo.repository.RoleRepo;
import com.neobis.demo.repository.UserRepo;
import com.neobis.demo.service.ActivationTokenService;
import com.neobis.demo.service.UserService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(value -> new org.springframework.security.core.userdetails.User(
                        value.getEmail(), value.getPassword(), value.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found!"));
    }
    @Override
    public User saveUser(User user) {
        Optional<User> userWithEmail = userRepo.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This email " + user.getEmail() + " is already exists!");
        }
        Optional<User> userWithUsername = userRepo.findByUsername(user.getUsername());
        if (userWithUsername.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This username " + user.getUsername() + " is already exists!");
        }
        Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow(
                ()-> new IllegalStateException("Role 'USER' not found"));
        user.setRoleSet(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        ActivationToken activationToken = activationTokenService.generateActivationToken(user);
        sendActivationEmail(user.getEmail(), activationToken.getToken());
        return user;
    }

    @Override
    public void activateUserByToken(String token) {
        Optional<ActivationToken> tokenOptional = activationTokenService.getActivationTokenByToken(token);
        if (tokenOptional.isPresent()) {
            ActivationToken activationToken = tokenOptional.get();
            User user = activationToken.getUser();
            user.setEnabled(true);
            userRepo.save(user);
            activationTokenService.deleteActivationToken(activationToken);
        }else throw new IllegalArgumentException("Activation token not found");
    }

    @Override
    public boolean isEmailVerified(String username) {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        return optionalUser.map(User::isEnabled).orElse(false);
    }

    private void sendActivationEmail(String email, String token) {
    String activationLink = "https://neobisauthproject-production.up.railway.app/api/activate?token=" + token;
    emailService.sendActivationEmail(email, activationLink);
    }

    public void resendActivationEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User with email " + email + " not found!"));
        ActivationToken activationToken = activationTokenService.generateActivationToken(user);
        String activationLink = "https://neobisauthproject-production.up.railway.app/api/activate?token=" + activationToken.getToken();
        emailService.sendActivationEmail(user.getEmail(), activationLink);
    }


}
