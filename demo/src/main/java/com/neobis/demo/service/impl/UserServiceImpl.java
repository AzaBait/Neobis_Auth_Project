package com.neobis.demo.service.impl;

import com.neobis.demo.entity.ActivationToken;
import com.neobis.demo.entity.Role;
import com.neobis.demo.entity.User;
import com.neobis.demo.repository.RoleRepo;
import com.neobis.demo.repository.UserRepo;
import com.neobis.demo.service.ActivationTokenService;
import com.neobis.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        return user.map(value -> new org.springframework.security.core.userdetails.User(
                        value.getEmail(), value.getPassword(), value.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));
    }
    @Override
    public User saveUser(User user) {
        Optional<User> userWithEmail = userRepo.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new IllegalStateException("This email " + user.getEmail() + " is already exists!");
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

    private void sendActivationEmail(String email, String token) {
    String activationLink = "https://neobisauthproject-production.up.railway.app/api/activate?token=" + token;
    emailService.sendActivationEmail(email, activationLink);


    }

}
