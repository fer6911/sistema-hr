package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.config.ApiResponse;
import com.atlashr.atlas_hr.infrastructure.output.microservicio.BeneficiosMicroservicioClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final BeneficiosMicroservicioClient microservicioClient;
    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public BeneficioController(BeneficiosMicroservicioClient microservicioClient,
                               EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.microservicioClient = microservicioClient;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listarPorEmpleado(@PathVariable Long empleadoId) {
        String ciudad = empleadoRepositoryPort.findById(empleadoId)
                .map(Empleado::getCiudad)
                .orElse(null);

        Map<String, Object> respuesta = microservicioClient.listarPorEmpleado(empleadoId, ciudad);
        return ResponseEntity.ok(ApiResponse.success(
                (String) respuesta.get("message"),
                (Map<String, Object>) respuesta.get("data")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> crear(@RequestBody Map<String, Object> datos) {
        Map<String, Object> respuesta = microservicioClient.crear(datos);
        return ResponseEntity.ok(ApiResponse.success(
                (String) respuesta.get("message"),
                (Map<String, Object>) respuesta.get("data")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> editar(@PathVariable Long id,
                                                                    @RequestBody Map<String, Object> datos) {
        Map<String, Object> respuesta = microservicioClient.editar(id, datos);
        return ResponseEntity.ok(ApiResponse.success(
                (String) respuesta.get("message"),
                (Map<String, Object>) respuesta.get("data")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        Map<String, Object> respuesta = microservicioClient.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(
                (String) respuesta.get("message"), null));
    }
}
