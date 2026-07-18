import axios, { AxiosHeaders } from 'axios'
import { useAuthStore } from '~/features/auth/store/useAuthStore'

const API_PORT = '8080'
const DEFAULT_API_URL = `http://localhost:${API_PORT}`
const REQUEST_TIMEOUT = 30000

export const getApiErrorMessage = (error: unknown, fallback: string): string => {
  const err = error as { response?: { data?: { errors?: string[]; message?: string } } }
  const responseErrors = err?.response?.data?.errors
  if (Array.isArray(responseErrors) && responseErrors.length > 0) {
    const firstError = responseErrors[0]
    if (typeof firstError === 'string') return firstError
  }
  const message = err?.response?.data?.message
  if (message && typeof message === 'string') return message
  return fallback
}

const isClient = () => process.client && typeof window !== 'undefined'

const isLocalhost = (hostname: string): boolean =>
  hostname === 'localhost' || hostname === '127.0.0.1'

const getDynamicBaseUrl = (): string | null => {
  if (!isClient()) return null

  const { protocol, hostname } = window.location

  if (isLocalhost(hostname)) {
    return DEFAULT_API_URL
  }

  return `${protocol}//${hostname}:${API_PORT}`
}

const getApiBaseUrl = (): string => {
  return (
    getDynamicBaseUrl() ||
    import.meta.env.VITE_API_URL ||
    DEFAULT_API_URL
  )
}

const setAuthorizationHeader = (
  headers: unknown,
  token: string
): void => {
  if (headers instanceof AxiosHeaders) {
    headers.set('Authorization', `Bearer ${token}`)
    return
  }

  ;(headers as Record<string, string>).Authorization = `Bearer ${token}`
}

const apiBar = axios.create({
  timeout: REQUEST_TIMEOUT,
})

apiBar.interceptors.request.use((config) => {
  config.baseURL = getApiBaseUrl()

  if (!isClient()) return config

  try {
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers = config.headers || {}
      setAuthorizationHeader(config.headers, authStore.token)
    }
  } catch {
    // Store no disponible temporalmente (fuera de contexto Vue)
  }

  return config
})

apiBar.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error?.response?.status === 401 && isClient()) {
      try {
        const authStore = useAuthStore()
        authStore.logout(true)
      } catch {
        // Ignorar errores del store
      }
    }

    return Promise.reject(error)
  }
)

export { apiBar }
