import { describe, it, expect, vi, beforeEach } from 'vitest'
import { AxiosError } from 'axios'
import { listarBeneficiosPorEmpleado, crearBeneficio, eliminarBeneficio } from './beneficio'
import { apiBar } from '~/api/core/axios'

vi.mock('~/api/core/axios', () => ({
  apiBar: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn(),
  },
}))

describe('listarBeneficiosPorEmpleado', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna los beneficios cuando la respuesta es exitosa', async () => {
    const mockData = { beneficios: [{ id: 1, nombreBeneficio: 'Seguro', monto: 100 }], ubicacion: null }
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: mockData } })

    const result = await listarBeneficiosPorEmpleado(1)

    expect(apiBar.get).toHaveBeenCalledWith('/api/beneficios/empleado/1')
    expect(result).toEqual(mockData)
  })

  it('retorna serviceError cuando el backend responde error: true', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: true, message: 'Error' } })

    const result = await listarBeneficiosPorEmpleado(1)

    expect(result).toEqual({ beneficios: [], ubicacion: null, serviceError: true })
  })

  it('retorna valores por defecto cuando data.data es null', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: null } })

    const result = await listarBeneficiosPorEmpleado(1)

    expect(result).toEqual({ beneficios: [], ubicacion: null })
  })

  it('retorna serviceError cuando ocurre una excepción de red', async () => {
    vi.mocked(apiBar.get).mockRejectedValue(new Error('Network Error'))

    const result = await listarBeneficiosPorEmpleado(1)

    expect(result).toEqual({ beneficios: [], ubicacion: null, serviceError: true })
  })
})

describe('crearBeneficio', () => {
  beforeEach(() => vi.clearAllMocks())

  const dto = { empleadoId: 1, nombreBeneficio: 'Seguro', monto: 100 }

  it('retorna ok:true con el beneficio creado', async () => {
    const mockBeneficio = { id: 1, ...dto }
    vi.mocked(apiBar.post).mockResolvedValue({ data: { error: false, data: mockBeneficio } })

    const result = await crearBeneficio(dto)

    expect(apiBar.post).toHaveBeenCalledWith('/api/beneficios', dto)
    expect(result).toEqual({ ok: true, beneficio: mockBeneficio })
  })

  it('retorna ok:false con el primer error del array cuando error:true', async () => {
    vi.mocked(apiBar.post).mockResolvedValue({
      data: { error: true, errors: ['Monto inválido'], message: 'Error general' },
    })

    const result = await crearBeneficio(dto)

    expect(result).toEqual({ ok: false, message: 'Monto inválido' })
  })

  it('retorna ok:false con message cuando no hay errors', async () => {
    vi.mocked(apiBar.post).mockResolvedValue({
      data: { error: true, errors: [], message: 'Error general' },
    })

    const result = await crearBeneficio(dto)

    expect(result).toEqual({ ok: false, message: 'Error general' })
  })

  it('retorna ok:false con mensaje de axios cuando la petición falla', async () => {
    const axiosError = new AxiosError('Request failed')
    axiosError.response = {
      data: { errors: ['Empleado no existe'] },
      status: 400,
      statusText: 'Bad Request',
      headers: {},
      config: {} as never,
    }
    vi.mocked(apiBar.post).mockRejectedValue(axiosError)

    const result = await crearBeneficio(dto)

    expect(result).toEqual({ ok: false, message: 'Empleado no existe' })
  })

  it('retorna mensaje genérico cuando no es un error de axios', async () => {
    vi.mocked(apiBar.post).mockRejectedValue(new Error('fallo desconocido'))

    const result = await crearBeneficio(dto)

    expect(result).toEqual({ ok: false, message: 'Sin conexión con el servidor' })
  })
})

describe('eliminarBeneficio', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna ok:true cuando la eliminación es exitosa', async () => {
    vi.mocked(apiBar.delete).mockResolvedValue({ data: { error: false } })

    const result = await eliminarBeneficio(1)

    expect(apiBar.delete).toHaveBeenCalledWith('/api/beneficios/1')
    expect(result).toEqual({ ok: true })
  })

  it('retorna ok:false con el message cuando error:true', async () => {
    vi.mocked(apiBar.delete).mockResolvedValue({ data: { error: true, message: 'No se pudo eliminar' } })

    const result = await eliminarBeneficio(1)

    expect(result).toEqual({ ok: false, message: 'No se pudo eliminar' })
  })

  it('retorna ok:false con mensaje de axios cuando la petición falla', async () => {
    const axiosError = new AxiosError('Request failed')
    axiosError.response = {
      data: { message: 'Beneficio no encontrado' },
      status: 404,
      statusText: 'Not Found',
      headers: {},
      config: {} as never,
    }
    vi.mocked(apiBar.delete).mockRejectedValue(axiosError)

    const result = await eliminarBeneficio(1)

    expect(result).toEqual({ ok: false, message: 'Beneficio no encontrado' })
  })

  it('retorna mensaje genérico cuando no es un error de axios', async () => {
    vi.mocked(apiBar.delete).mockRejectedValue(new Error('fallo desconocido'))

    const result = await eliminarBeneficio(1)

    expect(result).toEqual({ ok: false, message: 'Sin conexión con el servidor' })
  })
})