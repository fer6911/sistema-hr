package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empleado {
    private final Long id;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String cargo;
    private final BigDecimal salario;
    private final LocalDate fechaIngreso;
    private final String ciudad;
    private final boolean activo;

    private Empleado(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarNombre(builder.nombre, errores);
        validarApellido(builder.apellido, errores);
        validarEmail(builder.email, errores);
        validarCargo(builder.cargo, errores);
        validarSalario(builder.salario, errores);
        validarFechaIngreso(builder.fechaIngreso, errores);

        if (!errores.isEmpty()) throw new EmpleadoNotValidException(errores);

        this.id = builder.id;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.email = builder.email;
        this.cargo = builder.cargo;
        this.salario = builder.salario;
        this.fechaIngreso = builder.fechaIngreso;
        this.ciudad = builder.ciudad;
        this.activo = builder.activo == null ? true : builder.activo;
    }

    private void validarNombre(String nombre, List<String> errores) {
        if (nombre == null || nombre.isBlank()) errores.add("El nombre no puede estar vacío");
        else if (nombre.length() > 50) errores.add("El nombre es demasiado largo");
    }

    private void validarApellido(String apellido, List<String> errores) {
        if (apellido == null || apellido.isBlank()) errores.add("El apellido no puede estar vacío");
        else if (apellido.length() > 50) errores.add("El apellido es demasiado largo");
    }

    private void validarEmail(String email, List<String> errores) {
        if (email == null || email.isBlank()) errores.add("El email no puede estar vacío");
        else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) errores.add("El email no tiene un formato válido");
    }

    private void validarCargo(String cargo, List<String> errores) {
        if (cargo == null || cargo.isBlank()) errores.add("El cargo no puede estar vacío");
    }

    private void validarSalario(BigDecimal salario, List<String> errores) {
        if (salario == null) errores.add("El salario no puede estar vacío");
        else if (salario.compareTo(BigDecimal.ZERO) < 0) errores.add("El salario no puede ser negativo");
    }

    private void validarFechaIngreso(LocalDate fechaIngreso, List<String> errores) {
        if (fechaIngreso == null) errores.add("La fecha de ingreso no puede estar vacía");
    }

    private void validarCiudad(String ciudad, List<String> errores) {
        if (ciudad == null || ciudad.isBlank()) errores.add("La ciudad no puede estar vacía");
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private String cargo;
        private BigDecimal salario;
        private LocalDate fechaIngreso;
        private String ciudad;
        private Boolean activo;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder apellido(String apellido) { this.apellido = apellido; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder cargo(String cargo) { this.cargo = cargo; return this; }
        public Builder salario(BigDecimal salario) { this.salario = salario; return this; }
        public Builder fechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; return this; }
        public Builder ciudad(String ciudad) { this.ciudad = ciudad; return this; }
        public Builder activo(Boolean activo) { this.activo = activo; return this; }

        public Empleado build() { return new Empleado(this); }
    }
}
