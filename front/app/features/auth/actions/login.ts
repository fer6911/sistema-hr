import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { AuthResponse } from '../interfaces'

interface LoginError {
  ok: false
  message: string
}

interface LoginSuccess {
  ok: true
  user: AuthResponse['data']
  token: string
}

export const loginAction = async (
  username: string,
  password: string
): Promise<LoginError | LoginSuccess> => {
  try {
    const response = await apiBar.post<AuthResponse>('/api/auth/login', {
      username,
      password,
    })

    const token = response.headers['authorization'] ?? response.headers['Authorization'] ?? ''
    const user = response.data.data

    return {
      ok: true,
      user,
      token: token.startsWith('Bearer ') ? token.slice(7) : token,
    }
  } catch (error) {
    if (isAxiosError(error)) {
      return {
        ok: false,
        message:
          error.response?.status === 401
            ? 'Usuario o contraseña incorrectos'
            : 'Error en el servidor, intente de nuevo',
      }
    }

    return {
      ok: false,
      message: 'Sin conexión con el servidor',
    }
  }
}
