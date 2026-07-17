package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;

public interface RegistrarUsuarioUseCase {
    UsuarioCreadoDto registrar(CrearUsuarioDto dto);
}
