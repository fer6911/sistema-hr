class CrearBeneficioService
  def self.ejecutar(params)
    beneficio = Beneficio.new(
      empleado_id: params[:empleado_id],
      nombre_beneficio: params[:nombre_beneficio],
      monto: params[:monto]
    )

    if beneficio.save
      { exito: true, beneficio: beneficio }
    else
      { exito: false, errores: beneficio.errors.full_messages }
    end
  end
end
