package com.neobis.demo.service.impl;

import com.neobis.demo.entity.ActivationToken;
import com.neobis.demo.entity.User;
import com.neobis.demo.repository.ActivationTokenRepo;
import com.neobis.demo.service.ActivationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivationTokenServiceImpl implements ActivationTokenService {

    private final ActivationTokenRepo tokenRepo;
    @Override
    public ActivationToken generateActivationToken(User user) {
        ActivationToken activationToken = new ActivationToken(user);
        return tokenRepo.save(activationToken);
    }

    @Override
    public Optional<ActivationToken> getActivationTokenByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    @Override
    public void deleteActivationToken(ActivationToken activationToken) {
    tokenRepo.delete(activationToken);
    }
}
