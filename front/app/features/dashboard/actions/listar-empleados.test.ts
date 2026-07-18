import { describe, it, expect, vi, beforeEach } from 'vitest'
import { listarEmpleados } from './listar-empleados'
import { apiBar } from '~/api/core/axios'

vi.mock('~/api/core/axios', () => ({
  apiBar: {
    get: vi.fn(),
  },
}))

const mockEmployee = {
  id: 1,
  nombre: 'Juan',
  apellido: 'Pérez',
  email: 'juan.perez@example.com',
  cargo: 'Desarrollador',
  salario: 50000,
  fechaIngreso: '2026-07-17',
  ciudad: 'Bogota',
}

describe('listarEmpleados (dashboard)', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna la lista de empleados cuando la respuesta es exitosa', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: [mockEmployee] } })

    const result = await listarEmpleados()

    expect(apiBar.get).toHaveBeenCalledWith('/api/empleados')
    expect(result).toEqual([mockEmployee])
  })

  it('retorna array vacío cuando el backend responde error: true', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: true, message: 'Error' } })

    const result = await listarEmpleados()

    expect(result).toEqual([])
  })

  it('retorna array vacío cuando data.data es null', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: null } })

    const result = await listarEmpleados()

    expect(result).toEqual([])
  })

  it('retorna array vacío cuando ocurre una excepción de red', async () => {
    vi.mocked(apiBar.get).mockRejectedValue(new Error('Network Error'))

    const result = await listarEmpleados()

    expect(result).toEqual([])
  })
})