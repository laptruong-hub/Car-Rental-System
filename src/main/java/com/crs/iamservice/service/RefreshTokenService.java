package com.crs.iamservice.service;

import com.crs.iamservice.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenService {

    // Tìm kiếm token trong Database
    Optional<RefreshToken> findByToken(String token);

    // Tạo mới một Refresh Token gắn liền với Email của User
    RefreshToken createRefreshToken(String email);

    // Kiểm tra xem Token đã hết hạn chưa, nếu hết hạn thì xóa khỏi DB
    RefreshToken verifyExpiration(RefreshToken token);

    // Xóa Token theo User (Dùng khi Logout hoặc Rotation)
    void deleteByToken(String userId);
}