package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;

public interface LoginUseCase {
    TokenResponseDto login(LoginDto dto);
}
