package com.neobis.demo.service;

import com.neobis.demo.dto.JwtRequest;

public interface AuthenticationService {

    String authenticateAndGetToken(JwtRequest jwtRequest) throws Exception;
}
