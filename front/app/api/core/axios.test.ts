import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import MockAdapter from 'axios-mock-adapter'
import { getApiErrorMessage, apiBar } from './axios'

// Mock del store de auth — controlamos su estado en cada test
const authStoreState = { token: null as string | null, logout: vi.fn() }

vi.mock('~/features/auth/store/useAuthStore', () => ({
  useAuthStore: () => authStoreState,
}))

describe('getApiErrorMessage', () => {
  it('retorna el primer error del array "errors" si existe', () => {
    const error = {
      response: { data: { errors: ['El email ya está registrado', 'Otro error'] } },
    }
    expect(getApiErrorMessage(error, 'fallback')).toBe('El email ya está registrado')
  })

  it('retorna "message" si no hay array de errores', () => {
    const error = {
      response: { data: { message: 'Credenciales inválidas' } },
    }
    expect(getApiErrorMessage(error, 'fallback')).toBe('Credenciales inválidas')
  })

  it('retorna el fallback si no hay errors ni message', () => {
    const error = { response: { data: {} } }
    expect(getApiErrorMessage(error, 'Error genérico')).toBe('Error genérico')
  })

  it('retorna el fallback si errors es un array vacío', () => {
    const error = { response: { data: { errors: [] } } }
    expect(getApiErrorMessage(error, 'Error genérico')).toBe('Error genérico')
  })

  it('retorna el fallback si errors no es un array de strings', () => {
    const error = { response: { data: { errors: [{ campo: 'email' }] } } }
    expect(getApiErrorMessage(error, 'Error genérico')).toBe('Error genérico')
  })

  it('retorna el fallback si no hay response en absoluto', () => {
    expect(getApiErrorMessage(new Error('network fail'), 'Error genérico')).toBe('Error genérico')
  })

  it('retorna el fallback si error es undefined', () => {
    expect(getApiErrorMessage(undefined, 'Error genérico')).toBe('Error genérico')
  })
})

describe('apiBar (interceptors)', () => {
  let mock: MockAdapter

  beforeEach(() => {
    mock = new MockAdapter(apiBar)
    authStoreState.token = null
    authStoreState.logout.mockClear()
  })

  afterEach(() => {
    mock.restore()
  })

  it('agrega el header Authorization cuando hay token', async () => {
    authStoreState.token = 'fake-jwt-token'
    mock.onGet('/api/empleados').reply((config) => {
      expect(config.headers?.Authorization).toBe('Bearer fake-jwt-token')
      return [200, { data: [] }]
    })

    await apiBar.get('/api/empleados')
  })

  it('NO agrega el header Authorization cuando no hay token', async () => {
    authStoreState.token = null
    mock.onGet('/api/empleados').reply((config) => {
      expect(config.headers?.Authorization).toBeUndefined()
      return [200, { data: [] }]
    })

    await apiBar.get('/api/empleados')
  })

  it('llama a authStore.logout(true) cuando la respuesta es 401', async () => {
    mock.onGet('/api/empleados').reply(401)

    await expect(apiBar.get('/api/empleados')).rejects.toBeTruthy()
    expect(authStoreState.logout).toHaveBeenCalledWith(true)
  })

  it('NO llama a logout si el error no es 401', async () => {
    mock.onGet('/api/empleados').reply(500)

    await expect(apiBar.get('/api/empleados')).rejects.toBeTruthy()
    expect(authStoreState.logout).not.toHaveBeenCalled()
  })
})