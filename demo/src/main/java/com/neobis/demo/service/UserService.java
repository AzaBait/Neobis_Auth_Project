package com.neobis.demo.service;

import com.neobis.demo.entity.User;
import com.neobis.demo.repository.UserRepo;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
}
