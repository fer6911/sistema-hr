class EditarBeneficioService
  def self.ejecutar(id, params)
    beneficio = Beneficio.find_by(id: id)

    return { exito: false, errores: ["El beneficio no existe"] } unless beneficio

    if beneficio.update(
      nombre_beneficio: params[:nombre_beneficio],
      monto: params[:monto]
    )
      { exito: true, beneficio: beneficio }
    else
      { exito: false, errores: beneficio.errors.full_messages }
    end
  end
end
