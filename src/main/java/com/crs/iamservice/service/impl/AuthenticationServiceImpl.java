package com.crs.iamservice.service.impl;

import com.crs.iamservice.config.UserPrincipal;
import com.crs.iamservice.dto.request.AuthenticationRequest;
import com.crs.iamservice.dto.request.IntrospectRequest;
import com.crs.iamservice.dto.request.LogoutRequest;
import com.crs.iamservice.dto.request.RegisterRequest;
import com.crs.iamservice.dto.response.AuthenticationResponse;
import com.crs.iamservice.dto.response.IntrospectResponse;
import com.crs.iamservice.dto.response.RegisterResponse;
import com.crs.iamservice.dto.response.UserResponse;
import com.crs.iamservice.entity.RefreshToken;
import com.crs.iamservice.entity.User;
import com.crs.iamservice.repository.RoleRepository;
import com.crs.iamservice.repository.UserRepository;
import com.crs.iamservice.service.AuthenticationService;
import com.crs.iamservice.service.JwtService;
import com.crs.iamservice.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Inject Interface
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // 1. Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // 2. Lấy Role mặc định là CUSTOMER (đã khởi tạo trong DataInitializer)
        var defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy vai trò khách hàng"));

        // 3. Tạo Entity User mới
        var user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(defaultRole)
                .isActive(true) // Sử dụng @Builder.Default đã sửa ở bước trước
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .message("Đăng ký tài khoản thành công!")
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var userPrincipal = UserPrincipal.create(user);
        String accessToken = jwtService.generateToken(userPrincipal);

        var refreshTokenEntity = refreshTokenService.createRefreshToken(user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenEntity.getToken())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.token();
        boolean isValid = true;

        try {
            // Kiểm tra Token bằng JwtService (kiểm tra hết hạn, chữ ký...)
            // Ở đây ta chỉ cần kiểm tra username có trích xuất được không và token chưa hết hạn
            String username = jwtService.extractUsername(token);

            // Bạn có thể bổ sung kiểm tra DB nếu muốn chắc chắn User vẫn tồn tại/Active
            if (username == null || username.isEmpty()) {
                isValid = false;
            }
        } catch (Exception e) {
            // Nếu có bất kỳ lỗi nào (Expired, Malformed, SignatureException...)
            isValid = false;
        }

        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    @Override
    public UserResponse getMyProfile() {
        // 1. Lấy email (username) từ SecurityContext do JwtAuthenticationFilter đã xác thực trước đó
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 2. Tìm User trong DB
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));

        // 3. Map sang DTO trả về
        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getName())
                .isActive(user.isActive())
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        // Tìm và xóa Refresh Token trong Database
        refreshTokenService.deleteByToken(request.refreshToken());
        // Xóa SecurityContext để đảm bảo phiên làm việc hiện tại bị ngắt hoàn toàn
        SecurityContextHolder.clearContext();
    }
}