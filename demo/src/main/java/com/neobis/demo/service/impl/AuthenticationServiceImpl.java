package com.neobis.demo.service.impl;

import com.neobis.demo.configuration.security.jwt.JwtTokenUtil;
import com.neobis.demo.dto.JwtRequest;
import com.neobis.demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    @Override
    public String authenticateAndGetToken(JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e) {
            throw new Exception("User disabled", e);
        }catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }
}
