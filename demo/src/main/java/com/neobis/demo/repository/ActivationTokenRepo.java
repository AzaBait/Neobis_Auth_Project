package com.neobis.demo.repository;

import com.neobis.demo.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActivationTokenRepo extends JpaRepository<ActivationToken, Long> {
    Optional<ActivationToken> findByToken(String Token);
}
