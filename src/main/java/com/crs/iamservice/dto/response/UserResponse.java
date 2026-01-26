package com.crs.iamservice.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String email,
        String fullName,
        String role,
        boolean isActive
) {}