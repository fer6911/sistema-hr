import { apiBar } from '~/api/core/axios'
import { isAxiosError } from 'axios'
import type { AuthResponse } from '../interfaces'

interface RegisterError {
  ok: false
  message: string
}

interface RegisterSuccess {
  ok: true
  user: AuthResponse['data']
  token: string
}

export const registerAction = async (
  username: string,
  email: string,
  password: string
): Promise<RegisterError | RegisterSuccess> => {
  try {
    const response = await apiBar.post<AuthResponse>('/api/auth/register', {
      username,
      email,
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
          error.response?.status === 409
            ? 'El nombre de usuario o correo ya está registrado'
            : 'Error en el servidor, intente de nuevo',
      }
    }

    return {
      ok: false,
      message: 'Sin conexión con el servidor',
    }
  }
}
