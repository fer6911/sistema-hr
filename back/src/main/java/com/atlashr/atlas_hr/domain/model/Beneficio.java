package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Beneficio {
    private final Long id;
    private final Long empleadoId;
    private final String nombreBeneficio;
    private final BigDecimal monto;

    private Beneficio(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarEmpleadoId(builder.empleadoId, errores);
        validarNombreBeneficio(builder.nombreBeneficio, errores);
        validarMonto(builder.monto, errores);

        if (!errores.isEmpty()) throw new BeneficioNotValidException(errores);

        this.id = builder.id;
        this.empleadoId = builder.empleadoId;
        this.nombreBeneficio = builder.nombreBeneficio;
        this.monto = builder.monto;
    }

    private void validarEmpleadoId(Long empleadoId, List<String> errores) {
        if (empleadoId == null) errores.add("El ID del empleado no puede estar vacío");
    }

    private void validarNombreBeneficio(String nombreBeneficio, List<String> errores) {
        if (nombreBeneficio == null || nombreBeneficio.isBlank()) errores.add("El nombre del beneficio no puede estar vacío");
    }

    private void validarMonto(BigDecimal monto, List<String> errores) {
        if (monto == null) errores.add("El monto no puede estar vacío");
        else if (monto.compareTo(BigDecimal.ZERO) <= 0) errores.add("El monto debe ser mayor a 0");
    }

    public Long getId() { return id; }
    public Long getEmpleadoId() { return empleadoId; }
    public String getNombreBeneficio() { return nombreBeneficio; }
    public BigDecimal getMonto() { return monto; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long empleadoId;
        private String nombreBeneficio;
        private BigDecimal monto;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder empleadoId(Long empleadoId) { this.empleadoId = empleadoId; return this; }
        public Builder nombreBeneficio(String nombreBeneficio) { this.nombreBeneficio = nombreBeneficio; return this; }
        public Builder monto(BigDecimal monto) { this.monto = monto; return this; }

        public Beneficio build() { return new Beneficio(this); }
    }
}
