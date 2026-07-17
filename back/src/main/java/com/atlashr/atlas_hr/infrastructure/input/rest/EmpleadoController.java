package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.in.CrearEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarEmpleadosUseCase;
import com.atlashr.atlas_hr.infrastructure.config.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final CrearEmpleadoUseCase crearEmpleadoUseCase;
    private final ListarEmpleadosUseCase listarEmpleadosUseCase;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleadoUseCase, ListarEmpleadosUseCase listarEmpleadosUseCase) {
        this.crearEmpleadoUseCase = crearEmpleadoUseCase;
        this.listarEmpleadosUseCase = listarEmpleadosUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpleadoDto>>> listar() {
        List<EmpleadoDto> empleados = listarEmpleadosUseCase.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Empleados listados exitosamente", empleados));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpleadoDto>> crear(@Valid @RequestBody CrearEmpleadoDto dto) {
        EmpleadoDto creado = crearEmpleadoUseCase.crear(dto);
        return ResponseEntity.ok(ApiResponse.success("Empleado registrado exitosamente", creado));
    }
}
