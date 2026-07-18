package com.atlashr.atlas_hr.infrastructure.output.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private BigDecimal salario;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @Column(nullable = true)
    private String ciudad;

    @Column(nullable = false)
    private boolean activo;

    protected EmpleadoEntity() {}

    public EmpleadoEntity(String nombre, String apellido, String email, String cargo,
                          BigDecimal salario, LocalDate fechaIngreso, String ciudad, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cargo = cargo;
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
        this.ciudad = ciudad;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public BigDecimal getSalario() { return salario; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public String getCiudad() { return ciudad; }
    public boolean isActivo() { return activo; }
}
