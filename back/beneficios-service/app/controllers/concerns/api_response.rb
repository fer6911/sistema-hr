module ApiResponse
  def render_exitoso(data, mensaje = "Operación realizada exitosamente")
    render json: {
      error: false,
      message: mensaje,
      data: data,
      errors: []
    }
  end

  def render_error(mensaje, errores = [], status = :unprocessable_entity)
    render json: {
      error: true,
      message: mensaje,
      data: nil,
      errors: Array(errores)
    }, status: status
  end
end
