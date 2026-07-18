import { describe, it, expect, vi, beforeEach } from 'vitest'
import { AxiosError } from 'axios'
import {
  listarEmpleados,
  obtenerEmpleado,
  crearEmpleado,
  editarEmpleado,
  eliminarEmpleado,
} from './empleado'
import { apiBar } from '~/api/core/axios'

vi.mock('~/api/core/axios', () => ({
  apiBar: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  },
}))

const mockEmpleado = {
  id: 1,
  nombre: 'Juan',
  apellido: 'Pérez',
  email: 'juan.perez@example.com',
  cargo: 'Desarrollador',
  salario: 50000,
  fechaIngreso: '2026-07-17',
  ciudad: 'Bogota',
}

describe('listarEmpleados', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna la lista de empleados cuando la respuesta es exitosa', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: [mockEmpleado] } })

    const result = await listarEmpleados()

    expect(apiBar.get).toHaveBeenCalledWith('/api/empleados')
    expect(result).toEqual([mockEmpleado])
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

  it('retorna array vacío cuando ocurre una excepción', async () => {
    vi.mocked(apiBar.get).mockRejectedValue(new Error('Network Error'))

    const result = await listarEmpleados()

    expect(result).toEqual([])
  })
})

describe('obtenerEmpleado', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna el empleado cuando la respuesta es exitosa', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: mockEmpleado } })

    const result = await obtenerEmpleado(1)

    expect(apiBar.get).toHaveBeenCalledWith('/api/empleados/1')
    expect(result).toEqual(mockEmpleado)
  })

  it('retorna null cuando el backend responde error: true', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: true, message: 'No encontrado' } })

    const result = await obtenerEmpleado(999)

    expect(result).toBeNull()
  })

  it('retorna null cuando data.data es null', async () => {
    vi.mocked(apiBar.get).mockResolvedValue({ data: { error: false, data: null } })

    const result = await obtenerEmpleado(1)

    expect(result).toBeNull()
  })

  it('retorna null cuando ocurre una excepción', async () => {
    vi.mocked(apiBar.get).mockRejectedValue(new Error('Network Error'))

    const result = await obtenerEmpleado(1)

    expect(result).toBeNull()
  })
})

describe('crearEmpleado', () => {
  beforeEach(() => vi.clearAllMocks())

  const dto = {
    nombre: 'Juan',
    apellido: 'Pérez',
    email: 'juan.perez@example.com',
    cargo: 'Desarrollador',
    salario: 50000,
    fechaIngreso: '2026-07-17',
    ciudad: 'Bogota',
  }

  it('retorna ok:true con el empleado creado', async () => {
    vi.mocked(apiBar.post).mockResolvedValue({ data: { error: false, data: mockEmpleado } })

    const result = await crearEmpleado(dto)

    expect(apiBar.post).toHaveBeenCalledWith('/api/empleados', dto)
    expect(result).toEqual({ ok: true, empleado: mockEmpleado })
  })

  it('retorna ok:false con el primer error del array cuando error:true', async () => {
    vi.mocked(apiBar.post).mockResolvedValue({
      data: { error: true, errors: ['El email ya está registrado'], message: 'Error general' },
    })

    const result = await crearEmpleado(dto)

    expect(result).toEqual({ ok: false, message: 'El email ya está registrado' })
  })

  it('retorna ok:false con message cuando no hay errors', async () => {
    vi.mocked(apiBar.post).mockResolvedValue({
      data: { error: true, errors: [], message: 'Error general' },
    })

    const result = await crearEmpleado(dto)

    expect(result).toEqual({ ok: false, message: 'Error general' })
  })

  it('retorna ok:false con mensaje de axios cuando la petición falla', async () => {
    const axiosError = new AxiosError('Request failed')
    axiosError.response = {
      data: { errors: ['Cargo inválido'] },
      status: 400,
      statusText: 'Bad Request',
      headers: {},
      config: {} as never,
    }
    vi.mocked(apiBar.post).mockRejectedValue(axiosError)

    const result = await crearEmpleado(dto)

    expect(result).toEqual({ ok: false, message: 'Cargo inválido' })
  })

  it('retorna mensaje genérico cuando no es un error de axios', async () => {
    vi.mocked(apiBar.post).mockRejectedValue(new Error('fallo desconocido'))

    const result = await crearEmpleado(dto)

    expect(result).toEqual({ ok: false, message: 'Sin conexión con el servidor' })
  })
})

describe('editarEmpleado', () => {
  beforeEach(() => vi.clearAllMocks())

  const dto = { salario: 60000 }

  it('retorna ok:true con el empleado actualizado', async () => {
    const actualizado = { ...mockEmpleado, salario: 60000 }
    vi.mocked(apiBar.put).mockResolvedValue({ data: { error: false, data: actualizado } })

    const result = await editarEmpleado(1, dto)

    expect(apiBar.put).toHaveBeenCalledWith('/api/empleados/1', dto)
    expect(result).toEqual({ ok: true, empleado: actualizado })
  })

  it('retorna ok:false con el primer error del array cuando error:true', async () => {
    vi.mocked(apiBar.put).mockResolvedValue({
      data: { error: true, errors: ['Salario inválido'], message: 'Error general' },
    })

    const result = await editarEmpleado(1, dto)

    expect(result).toEqual({ ok: false, message: 'Salario inválido' })
  })

  it('retorna ok:false con mensaje de axios cuando la petición falla', async () => {
    const axiosError = new AxiosError('Request failed')
    axiosError.response = {
      data: { message: 'Empleado no encontrado' },
      status: 404,
      statusText: 'Not Found',
      headers: {},
      config: {} as never,
    }
    vi.mocked(apiBar.put).mockRejectedValue(axiosError)

    const result = await editarEmpleado(999, dto)

    expect(result).toEqual({ ok: false, message: 'Empleado no encontrado' })
  })

  it('retorna mensaje genérico cuando no es un error de axios', async () => {
    vi.mocked(apiBar.put).mockRejectedValue(new Error('fallo desconocido'))

    const result = await editarEmpleado(1, dto)

    expect(result).toEqual({ ok: false, message: 'Sin conexión con el servidor' })
  })
})

describe('eliminarEmpleado', () => {
  beforeEach(() => vi.clearAllMocks())

  it('retorna ok:true cuando la eliminación es exitosa', async () => {
    vi.mocked(apiBar.delete).mockResolvedValue({ data: { error: false } })

    const result = await eliminarEmpleado(1)

    expect(apiBar.delete).toHaveBeenCalledWith('/api/empleados/1')
    expect(result).toEqual({ ok: true })
  })

  it('retorna ok:false con el message cuando error:true', async () => {
    vi.mocked(apiBar.delete).mockResolvedValue({ data: { error: true, message: 'No se pudo eliminar' } })

    const result = await eliminarEmpleado(1)

    expect(result).toEqual({ ok: false, message: 'No se pudo eliminar' })
  })

  it('retorna ok:false con mensaje de axios cuando la petición falla', async () => {
    const axiosError = new AxiosError('Request failed')
    axiosError.response = {
      data: { message: 'Empleado tiene beneficios asociados' },
      status: 409,
      statusText: 'Conflict',
      headers: {},
      config: {} as never,
    }
    vi.mocked(apiBar.delete).mockRejectedValue(axiosError)

    const result = await eliminarEmpleado(1)

    expect(result).toEqual({ ok: false, message: 'Empleado tiene beneficios asociados' })
  })

  it('retorna mensaje genérico cuando no es un error de axios', async () => {
    vi.mocked(apiBar.delete).mockRejectedValue(new Error('fallo desconocido'))

    const result = await eliminarEmpleado(1)

    expect(result).toEqual({ ok: false, message: 'Sin conexión con el servidor' })
  })
})