package com.neobis.demo.service.impl;

import com.neobis.demo.entity.Role;
import com.neobis.demo.entity.User;
import com.neobis.demo.repository.RoleRepo;
import com.neobis.demo.repository.UserRepo;
import com.neobis.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    @Override
    public User saveUser(User user) {
        Optional<User> userWithEmail = userRepo.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new IllegalStateException("This email " + user.getEmail() + " is already exists!");
        }
        Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow(()-> new IllegalStateException("Role 'USER' not found"));
        user.setRoleSet(Collections.singleton(userRole));
        return userRepo.save(user);
    }
}
