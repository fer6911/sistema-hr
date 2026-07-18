import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { ApiResponse, EmpleadoDto } from '../interfaces'

export const listarEmpleados = async (): Promise<EmpleadoDto[]> => {
  try {
    const response = await apiBar.get<ApiResponse<EmpleadoDto[]>>('/api/empleados')
    if (response.data.error) return []
    return response.data.data ?? []
  } catch (error) {
    if (isAxiosError(error)) {
      console.error('Error al listar empleados:', error.response?.data?.message ?? error.message)
    }
    return []
  }
}

export const obtenerEmpleado = async (id: number): Promise<EmpleadoDto | null> => {
  try {
    const response = await apiBar.get<ApiResponse<EmpleadoDto>>(`/api/empleados/${id}`)
    if (response.data.error) return null
    return response.data.data ?? null
  } catch (error) {
    if (isAxiosError(error)) {
      console.error('Error al obtener empleado:', error.response?.data?.message ?? error.message)
    }
    return null
  }
}

export const crearEmpleado = async (dto: {
  nombre: string
  apellido: string
  email: string
  cargo: string
  salario: number
  fechaIngreso: string
  ciudad: string
}): Promise<{ ok: true; empleado: EmpleadoDto } | { ok: false; message: string }> => {
  try {
    const response = await apiBar.post<ApiResponse<EmpleadoDto>>('/api/empleados', dto)
    if (response.data.error) {
      return { ok: false, message: response.data.errors?.[0] ?? response.data.message }
    }
    return { ok: true, empleado: response.data.data }
  } catch (error) {
    if (isAxiosError(error)) {
      const msg = error.response?.data?.errors?.[0] ?? error.response?.data?.message ?? 'Error al crear empleado'
      return { ok: false, message: msg }
    }
    return { ok: false, message: 'Sin conexión con el servidor' }
  }
}

export const editarEmpleado = async (
  id: number,
  dto: {
    nombre?: string
    apellido?: string
    email?: string
    cargo?: string
    salario?: number
    fechaIngreso?: string
    ciudad?: string
    activo?: boolean
  }
): Promise<{ ok: true; empleado: EmpleadoDto } | { ok: false; message: string }> => {
  try {
    const response = await apiBar.put<ApiResponse<EmpleadoDto>>(`/api/empleados/${id}`, dto)
    if (response.data.error) {
      return { ok: false, message: response.data.errors?.[0] ?? response.data.message }
    }
    return { ok: true, empleado: response.data.data }
  } catch (error) {
    if (isAxiosError(error)) {
      const msg = error.response?.data?.errors?.[0] ?? error.response?.data?.message ?? 'Error al editar empleado'
      return { ok: false, message: msg }
    }
    return { ok: false, message: 'Sin conexión con el servidor' }
  }
}

export const eliminarEmpleado = async (id: number): Promise<{ ok: true } | { ok: false; message: string }> => {
  try {
    const response = await apiBar.delete<ApiResponse<void>>(`/api/empleados/${id}`)
    if (response.data.error) {
      return { ok: false, message: response.data.message }
    }
    return { ok: true }
  } catch (error) {
    if (isAxiosError(error)) {
      const msg = error.response?.data?.message ?? 'Error al eliminar empleado'
      return { ok: false, message: msg }
    }
    return { ok: false, message: 'Sin conexión con el servidor' }
  }
}
