package com.atlashr.atlas_hr.infrastructure.security;

import com.atlashr.atlas_hr.application.ports.out.TokenGenerationPort;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenGenerationPort {

    private final JwtUtil jwtUtil;

    public JwtTokenAdapter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String generateToken(String username, String rol) {
        return jwtUtil.generateToken(username, rol);
    }
}
