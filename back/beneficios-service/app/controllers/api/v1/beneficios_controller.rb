module Api
  module V1
    class BeneficiosController < ApplicationController
      def listar_por_empleado
        resultado = ListarBeneficiosService.ejecutar(params[:empleado_id].to_i)

        ubicacion = nil
        if params[:ciudad].present?
          ubicacion = NominatimService.buscar_por_ciudad(params[:ciudad])
        end

        data = {
          beneficios: resultado[:beneficios].map { |b| BeneficioSerializer.serializar(b) },
          ubicacion: ubicacion
        }

        render_exitoso(data, "Beneficios listados exitosamente")
      end

      def crear
        resultado = CrearBeneficioService.ejecutar(params.permit(:empleado_id, :nombre_beneficio, :monto))

        if resultado[:exito]
          render_exitoso(
            BeneficioSerializer.serializar(resultado[:beneficio]),
            "Beneficio registrado exitosamente"
          )
        else
          render_error("Beneficio no válido", resultado[:errores])
        end
      end

      def editar
        resultado = EditarBeneficioService.ejecutar(
          params[:id].to_i,
          params.permit(:nombre_beneficio, :monto)
        )

        if resultado[:exito]
          render_exitoso(
            BeneficioSerializer.serializar(resultado[:beneficio]),
            "Beneficio actualizado exitosamente"
          )
        else
          render_error("No se pudo actualizar el beneficio", resultado[:errores])
        end
      end

      def eliminar
        resultado = EliminarBeneficioService.ejecutar(params[:id].to_i)

        if resultado[:exito]
          render_exitoso(nil, "Beneficio eliminado exitosamente")
        else
          render_error("No se pudo eliminar el beneficio", resultado[:errores])
        end
      end
    end
  end
end
