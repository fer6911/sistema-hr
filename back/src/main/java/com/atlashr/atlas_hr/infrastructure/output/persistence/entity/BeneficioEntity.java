package com.atlashr.atlas_hr.infrastructure.output.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "beneficios")
public class BeneficioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long empleadoId;

    @Column(nullable = false)
    private String nombreBeneficio;

    @Column(nullable = false)
    private BigDecimal monto;

    protected BeneficioEntity() {}

    public BeneficioEntity(Long empleadoId, String nombreBeneficio, BigDecimal monto) {
        this.empleadoId = empleadoId;
        this.nombreBeneficio = nombreBeneficio;
        this.monto = monto;
    }

    public Long getId() { return id; }
    public Long getEmpleadoId() { return empleadoId; }
    public String getNombreBeneficio() { return nombreBeneficio; }
    public BigDecimal getMonto() { return monto; }
}
