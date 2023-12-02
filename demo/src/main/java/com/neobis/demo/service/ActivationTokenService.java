package com.neobis.demo.service;

import com.neobis.demo.entity.ActivationToken;
import com.neobis.demo.entity.User;

import java.util.Optional;

public interface ActivationTokenService {

    ActivationToken generateActivationToken(User user);
    Optional<ActivationToken> getActivationTokenByToken(String token);
    void deleteActivationToken(ActivationToken activationToken);
}
