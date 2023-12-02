package com.neobis.demo.service;


import com.neobis.demo.entity.User;

public interface UserService {

    User saveUser(User user);

    void activateUserByToken(String token);
}
