package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.ports.in.CrearBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.EditarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.EliminarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosUseCase;
import com.atlashr.atlas_hr.infrastructure.config.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final CrearBeneficioUseCase crearBeneficioUseCase;
    private final ListarBeneficiosUseCase listarBeneficiosUseCase;
    private final EditarBeneficioUseCase editarBeneficioUseCase;
    private final EliminarBeneficioUseCase eliminarBeneficioUseCase;

    public BeneficioController(CrearBeneficioUseCase crearBeneficioUseCase, ListarBeneficiosUseCase listarBeneficiosUseCase,
                               EditarBeneficioUseCase editarBeneficioUseCase, EliminarBeneficioUseCase eliminarBeneficioUseCase) {
        this.crearBeneficioUseCase = crearBeneficioUseCase;
        this.listarBeneficiosUseCase = listarBeneficiosUseCase;
        this.editarBeneficioUseCase = editarBeneficioUseCase;
        this.eliminarBeneficioUseCase = eliminarBeneficioUseCase;
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<ApiResponse<List<BeneficioDto>>> listarPorEmpleado(@PathVariable Long empleadoId) {
        List<BeneficioDto> beneficios = listarBeneficiosUseCase.listarPorEmpleado(empleadoId);
        return ResponseEntity.ok(ApiResponse.success("Beneficios listados exitosamente", beneficios));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficioDto>> crear(@Valid @RequestBody CrearBeneficioDto dto) {
        BeneficioDto creado = crearBeneficioUseCase.crear(dto);
        return ResponseEntity.ok(ApiResponse.success("Beneficio registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficioDto>> editar(@PathVariable Long id, @Valid @RequestBody EditarBeneficioDto dto) {
        BeneficioDto editado = editarBeneficioUseCase.editar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Beneficio actualizado exitosamente", editado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        eliminarBeneficioUseCase.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Beneficio eliminado exitosamente", null));
    }
}
