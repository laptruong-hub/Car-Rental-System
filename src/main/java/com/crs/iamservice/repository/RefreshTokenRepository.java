package com.crs.iamservice.repository;

import com.crs.iamservice.entity.RefreshToken;
import com.crs.iamservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    // Xóa refresh token theo User object (dùng cho token rotation)
    @Modifying
    @Transactional
    void deleteByUser(User user);

    // Xóa refresh token theo userId (dùng cho logout / revoke)
    void deleteByUser_UserId(String userId);
}