package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.domain.model.Beneficio;
import com.atlashr.atlas_hr.infrastructure.mapper.BeneficioPersistenceMapper;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.BeneficioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BeneficioPersistenceAdapterTest {

    @Autowired
    private BeneficioRepository beneficioRepository;

    @Autowired
    private BeneficioPersistenceMapper beneficioPersistenceMapper;

    private BeneficioPersistenceAdapter adapter;

    private void initAdapter() {
        adapter = new BeneficioPersistenceAdapter(beneficioRepository, beneficioPersistenceMapper);
    }

    private Beneficio crearBeneficio(Long empleadoId) {
        return Beneficio.builder()
                .empleadoId(empleadoId)
                .nombreBeneficio("Seguro Médico")
                .monto(new BigDecimal("500.00"))
                .build();
    }

    @Test
    void guardarYVerificarId() {
        initAdapter();
        Beneficio beneficio = crearBeneficio(1L);

        Beneficio guardado = adapter.save(beneficio);

        assertNotNull(guardado.getId());
        assertEquals(1L, guardado.getEmpleadoId());
        assertEquals("Seguro Médico", guardado.getNombreBeneficio());
    }

    @Test
    void findByEmpleadoIdRetornaBeneficios() {
        initAdapter();
        adapter.save(crearBeneficio(1L));
        adapter.save(crearBeneficio(1L));

        List<Beneficio> beneficios = adapter.findByEmpleadoId(1L);

        assertEquals(2, beneficios.size());
    }

    @Test
    void findByEmpleadoIdSinBeneficiosRetornaListaVacia() {
        initAdapter();

        List<Beneficio> beneficios = adapter.findByEmpleadoId(999L);

        assertTrue(beneficios.isEmpty());
    }

    @Test
    void saveRetornaDominioCompleto() {
        initAdapter();
        Beneficio beneficio = crearBeneficio(1L);

        Beneficio guardado = adapter.save(beneficio);

        assertEquals(1L, guardado.getEmpleadoId());
        assertEquals("Seguro Médico", guardado.getNombreBeneficio());
        assertEquals(new BigDecimal("500.00"), guardado.getMonto());
    }

    @Test
    void findByIdExistente() {
        initAdapter();
        Beneficio guardado = adapter.save(crearBeneficio(1L));

        var encontrado = adapter.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Seguro Médico", encontrado.get().getNombreBeneficio());
    }

    @Test
    void findByIdInexistente() {
        initAdapter();

        var encontrado = adapter.findById(999L);

        assertTrue(encontrado.isEmpty());
    }

    @Test
    void deleteByIdEliminaElRegistro() {
        initAdapter();
        Beneficio guardado = adapter.save(crearBeneficio(1L));

        adapter.deleteById(guardado.getId());

        assertTrue(adapter.findById(guardado.getId()).isEmpty());
    }
}
