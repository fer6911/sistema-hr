import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { ApiResponse, BeneficiosResponse, BeneficioDto, CrearBeneficioDto } from '../interfaces'

export const listarBeneficiosPorEmpleado = async (empleadoId: number): Promise<BeneficiosResponse> => {
  try {
    const response = await apiBar.get<ApiResponse<BeneficiosResponse>>(`/api/beneficios/empleado/${empleadoId}`)
    if (response.data.error) return { beneficios: [], ubicacion: null }
    return response.data.data ?? { beneficios: [], ubicacion: null }
  } catch (error) {
    if (isAxiosError(error)) {
      console.error('Error al listar beneficios:', error.response?.data?.message ?? error.message)
    }
    return { beneficios: [], ubicacion: null }
  }
}

export const crearBeneficio = async (
  dto: CrearBeneficioDto
): Promise<{ ok: true; beneficio: BeneficioDto } | { ok: false; message: string }> => {
  try {
    const response = await apiBar.post<ApiResponse<BeneficioDto>>('/api/beneficios', dto)
    if (response.data.error) {
      return { ok: false, message: response.data.errors?.[0] ?? response.data.message }
    }
    return { ok: true, beneficio: response.data.data }
  } catch (error) {
    if (isAxiosError(error)) {
      const msg = error.response?.data?.errors?.[0] ?? error.response?.data?.message ?? 'Error al crear beneficio'
      return { ok: false, message: msg }
    }
    return { ok: false, message: 'Sin conexión con el servidor' }
  }
}

export const eliminarBeneficio = async (id: number): Promise<{ ok: true } | { ok: false; message: string }> => {
  try {
    const response = await apiBar.delete<ApiResponse<void>>(`/api/beneficios/${id}`)
    if (response.data.error) {
      return { ok: false, message: response.data.message }
    }
    return { ok: true }
  } catch (error) {
    if (isAxiosError(error)) {
      const msg = error.response?.data?.message ?? 'Error al eliminar beneficio'
      return { ok: false, message: msg }
    }
    return { ok: false, message: 'Sin conexión con el servidor' }
  }
}
