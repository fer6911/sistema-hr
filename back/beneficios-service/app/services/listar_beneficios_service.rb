class ListarBeneficiosService
  def self.ejecutar(empleado_id)
    beneficios = Beneficio.por_empleado(empleado_id)
    { exito: true, beneficios: beneficios }
  end
end
