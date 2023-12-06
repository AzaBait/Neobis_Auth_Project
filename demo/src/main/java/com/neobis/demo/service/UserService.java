package com.neobis.demo.service;


import com.neobis.demo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User saveUser(User user);

    void activateUserByToken(String token);

    boolean isEmailVerified(String username);

    void resendActivationEmail(String email);
}
