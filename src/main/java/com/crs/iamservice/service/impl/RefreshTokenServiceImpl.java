package com.crs.iamservice.service.impl;

import com.crs.iamservice.entity.RefreshToken;
import com.crs.iamservice.repository.RefreshTokenRepository;
import com.crs.iamservice.repository.UserRepository;
import com.crs.iamservice.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    @Override
    @Transactional // Đảm bảo có Transaction bao quanh lệnh Repository
    public void deleteByToken(String token) {
        // Tìm đúng bản ghi chứa chuỗi token đó và xóa nó
        refreshTokenRepository.findByToken(token)
                .ifPresent(tokenEntity -> {
                    refreshTokenRepository.delete(tokenEntity);
                });
    }
}