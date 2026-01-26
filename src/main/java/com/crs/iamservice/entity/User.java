package com.crs.iamservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Sử dụng UUID cho bảo mật Microservices
    @Column(name = "user_id")
    String userId;

    @Column(unique = true, nullable = false)
    String email;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    String phone;
    String gender;
    LocalDate dob;

    @Column(name = "is_active")
    boolean isActive = true;

    @Column(name = "is_deleted")
    boolean isDeleted = false;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordHistory> passwordHistories;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}