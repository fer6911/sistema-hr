package com.atlashr.atlas_hr.application.ports.out;

public interface TokenGenerationPort {
    String generateToken(String username, String rol);
}
