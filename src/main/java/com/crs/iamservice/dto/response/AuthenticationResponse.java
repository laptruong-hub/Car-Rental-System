package com.crs.iamservice.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        String email,
        String role
) {}