package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmpleadoPersistenceAdapterTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private EmpleadoPersistenceAdapter adapter;

    private void initAdapter() {
        adapter = new EmpleadoPersistenceAdapter(empleadoRepository);
    }

    private Empleado crearEmpleado(String email) {
        return Empleado.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email(email)
                .cargo("Desarrollador")
                .salario(new BigDecimal("50000.00"))
                .fechaIngreso(LocalDate.of(2026, 1, 15))
                .build();
    }

    @Test
    void guardarYVerificarId() {
        initAdapter();
        Empleado empleado = crearEmpleado("juan@example.com");

        Empleado guardado = adapter.save(empleado);

        assertNotNull(guardado.getId());
        assertEquals("Juan", guardado.getNombre());
        assertEquals("juan@example.com", guardado.getEmail());
    }

    @Test
    void existsByEmailTrue() {
        initAdapter();
        adapter.save(crearEmpleado("juan@example.com"));

        assertTrue(adapter.existsByEmail("juan@example.com"));
    }

    @Test
    void existsByEmailFalse() {
        initAdapter();

        assertFalse(adapter.existsByEmail("noexiste@example.com"));
    }

    @Test
    void existsByIdTrue() {
        initAdapter();
        Empleado guardado = adapter.save(crearEmpleado("juan@example.com"));

        assertTrue(adapter.existsById(guardado.getId()));
    }

    @Test
    void existsByIdFalse() {
        initAdapter();

        assertFalse(adapter.existsById(999L));
    }

    @Test
    void findAllRetornaTodos() {
        initAdapter();
        adapter.save(crearEmpleado("juan@example.com"));
        adapter.save(crearEmpleado("ana@example.com"));

        List<Empleado> empleados = adapter.findAll();

        assertEquals(2, empleados.size());
    }

    @Test
    void findAllSinRegistrosRetornaListaVacia() {
        initAdapter();

        List<Empleado> empleados = adapter.findAll();

        assertTrue(empleados.isEmpty());
    }

    @Test
    void saveRetornaDominioCompleto() {
        initAdapter();
        Empleado empleado = crearEmpleado("juan@example.com");

        Empleado guardado = adapter.save(empleado);

        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellido());
        assertEquals("juan@example.com", guardado.getEmail());
        assertEquals("Desarrollador", guardado.getCargo());
        assertEquals(new BigDecimal("50000.00"), guardado.getSalario());
        assertEquals(LocalDate.of(2026, 1, 15), guardado.getFechaIngreso());
        assertTrue(guardado.isActivo());
    }
}
