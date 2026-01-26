package com.crs.iamservice.dto.request;

public record AuthenticationRequest(
        String email,
        String password
) {}