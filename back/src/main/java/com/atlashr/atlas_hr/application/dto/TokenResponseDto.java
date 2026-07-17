package com.atlashr.atlas_hr.application.dto;

public record TokenResponseDto(
    String token,
    String username,
    String rol
) {}
