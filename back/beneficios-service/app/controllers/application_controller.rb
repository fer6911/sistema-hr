class ApplicationController < ActionController::API
  include ApiResponse

  rescue_from ActiveRecord::RecordNotFound, with: :registro_no_encontrado

  private

  def registro_no_encontrado(excepcion)
    render_error("Recurso no encontrado", [excepcion.message], :not_found)
  end
end
