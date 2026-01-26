package com.crs.iamservice.dto.response;

import lombok.Builder;

@Builder
public record IntrospectResponse(
        boolean isValid
) {}