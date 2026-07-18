package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EditarEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.in.CrearEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.in.EditarEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.in.EliminarEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarEmpleadosUseCase;
import com.atlashr.atlas_hr.application.ports.in.ObtenerEmpleadoUseCase;
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
    private final ObtenerEmpleadoUseCase obtenerEmpleadoUseCase;
    private final EditarEmpleadoUseCase editarEmpleadoUseCase;
    private final EliminarEmpleadoUseCase eliminarEmpleadoUseCase;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleadoUseCase,
                              ListarEmpleadosUseCase listarEmpleadosUseCase,
                              ObtenerEmpleadoUseCase obtenerEmpleadoUseCase,
                              EditarEmpleadoUseCase editarEmpleadoUseCase,
                              EliminarEmpleadoUseCase eliminarEmpleadoUseCase) {
        this.crearEmpleadoUseCase = crearEmpleadoUseCase;
        this.listarEmpleadosUseCase = listarEmpleadosUseCase;
        this.obtenerEmpleadoUseCase = obtenerEmpleadoUseCase;
        this.editarEmpleadoUseCase = editarEmpleadoUseCase;
        this.eliminarEmpleadoUseCase = eliminarEmpleadoUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpleadoDto>>> listar() {
        List<EmpleadoDto> empleados = listarEmpleadosUseCase.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Empleados listados exitosamente", empleados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoDto>> obtener(@PathVariable Long id) {
        return obtenerEmpleadoUseCase.obtenerPorId(id)
                .map(empleado -> ResponseEntity.ok(ApiResponse.success("Empleado encontrado", empleado)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Empleado no encontrado", List.of())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpleadoDto>> crear(@Valid @RequestBody CrearEmpleadoDto dto) {
        EmpleadoDto creado = crearEmpleadoUseCase.crear(dto);
        return ResponseEntity.ok(ApiResponse.success("Empleado registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoDto>> editar(@PathVariable Long id,
                                                           @Valid @RequestBody EditarEmpleadoDto dto) {
        return editarEmpleadoUseCase.editar(id, dto)
                .map(empleado -> ResponseEntity.ok(ApiResponse.success("Empleado actualizado exitosamente", empleado)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Empleado no encontrado", List.of())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        boolean eliminado = eliminarEmpleadoUseCase.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok(ApiResponse.success("Empleado eliminado exitosamente", null));
        }
        return ResponseEntity.status(404).body(ApiResponse.error("Empleado no encontrado", List.of()));
    }
}
