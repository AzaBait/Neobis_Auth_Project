package com.neobis.demo.dto;

import lombok.Data;

@Data
public class JwtRequest {

    private final String username;
    private final String password;
}
