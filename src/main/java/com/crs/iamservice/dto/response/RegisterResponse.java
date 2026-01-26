package com.crs.iamservice.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(
        String message,
        String email,
        String fullName
) {}