import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { Employee } from '../interfaces'

interface ApiResponse<T> {
  error: boolean
  message: string
  data: T
  errors: string[]
}

export const listarEmpleados = async (): Promise<Employee[]> => {
  try {
    const response = await apiBar.get<ApiResponse<Employee[]>>('/api/empleados')

    if (response.data.error) return []
    return response.data.data ?? []
  } catch (error) {
    if (isAxiosError(error)) {
      console.error('API Error:', error.response?.data?.message ?? error.message)
    }
    return []
  }
}
