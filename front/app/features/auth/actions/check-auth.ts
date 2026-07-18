import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { AuthResponse, User } from '../interfaces'

interface CheckError {
  ok: false
}

interface CheckSuccess {
  ok: true
  user: User
  token: string
}

export const checkAuthAction = async (): Promise<CheckError | CheckSuccess> => {
  try {
    if (!process.client) return { ok: false }

    const localToken = localStorage.getItem('token')
    if (!localToken) return { ok: false }

    const response = await apiBar.get<AuthResponse>('/api/auth/check-status')

    const token = response.headers['authorization'] ?? response.headers['Authorization'] ?? ''
    const user = response.data.data

    return {
      ok: true,
      user,
      token: token.startsWith('Bearer ') ? token.slice(7) : token,
    }
  } catch (error) {
    if (isAxiosError(error) && error.response?.status === 401) {
      return { ok: false }
    }

    throw new Error('No se pudo verificar la sesión')
  }
}
