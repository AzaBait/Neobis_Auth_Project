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
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/activate")
    public ResponseEntity<String> activateUserAccount(@RequestParam("token") String token) {
        userService.activateUserByToken(token);
        return ResponseEntity.ok("User account activated successfully!");
    }
}
