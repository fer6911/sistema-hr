class EliminarBeneficioService
  def self.ejecutar(id)
    beneficio = Beneficio.find_by(id: id)

    return { exito: false, errores: ["El beneficio no existe"] } unless beneficio

    beneficio.destroy!
    { exito: true }
  rescue ActiveRecord::RecordNotDestroyed => e
    { exito: false, errores: [e.message] }
  end
end
