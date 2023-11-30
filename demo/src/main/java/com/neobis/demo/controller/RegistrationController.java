package com.neobis.demo.controller;

import com.neobis.demo.dto.RegisterDto;
import com.neobis.demo.dto.UserDto;
import com.neobis.demo.entity.User;
import com.neobis.demo.mapper.UserMapper;
import com.neobis.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final UserService userService;
    private final UserMapper userMapper;
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerNewUser(@Validated @RequestBody RegisterDto registerDto) {
        User savedUser = userService.saveUser(userMapper.registerDtoToEntity(registerDto));
        UserDto userDto = userMapper.entityToDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
