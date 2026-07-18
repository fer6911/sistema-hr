class BeneficioSerializer
  def self.serializar(beneficio)
    {
      id: beneficio.id,
      empleado_id: beneficio.empleado_id,
      nombre_beneficio: beneficio.nombre_beneficio,
      monto: beneficio.monto.to_f
    }
  end
end
