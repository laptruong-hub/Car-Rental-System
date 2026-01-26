package com.crs.iamservice.repository;

import com.crs.iamservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Tìm user theo email để đăng nhập
    Optional<User> findByEmail(String email);

    // Kiểm tra tồn tại để validate khi đăng ký
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.role r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);
}