import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useEmployeesStore } from './useEmployeesStore'
import * as actions from '../actions'

vi.mock('../actions', () => ({
  listarEmpleados: vi.fn(),
  obtenerEmpleado: vi.fn(),
  crearEmpleado: vi.fn(),
  editarEmpleado: vi.fn(),
  eliminarEmpleado: vi.fn(),
  listarBeneficiosPorEmpleado: vi.fn(),
  crearBeneficio: vi.fn(),
  eliminarBeneficio: vi.fn(),
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

const mockEmpleado2 = { ...mockEmpleado, id: 2, nombre: 'Maria' }

describe('useEmployeesStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  describe('fetchEmployees', () => {
    it('carga la lista de empleados y actualiza loading', async () => {
      vi.mocked(actions.listarEmpleados).mockResolvedValue([mockEmpleado, mockEmpleado2])

      const store = useEmployeesStore()
      const promise = store.fetchEmployees()

      expect(store.loading).toBe(true)
      await promise

      expect(store.loading).toBe(false)
      expect(store.employees).toEqual([mockEmpleado, mockEmpleado2])
    })
  })

  describe('fetchEmployee', () => {
    it('carga un empleado y lo retorna', async () => {
      vi.mocked(actions.obtenerEmpleado).mockResolvedValue(mockEmpleado)

      const store = useEmployeesStore()
      const result = await store.fetchEmployee(1)

      expect(actions.obtenerEmpleado).toHaveBeenCalledWith(1)
      expect(store.currentEmployee).toEqual(mockEmpleado)
      expect(result).toEqual(mockEmpleado)
      expect(store.loading).toBe(false)
    })

    it('deja currentEmployee en null si no se encuentra', async () => {
      vi.mocked(actions.obtenerEmpleado).mockResolvedValue(null)

      const store = useEmployeesStore()
      const result = await store.fetchEmployee(999)

      expect(store.currentEmployee).toBeNull()
      expect(result).toBeNull()
    })
  })

  describe('addEmployee', () => {
    const dto = {
      nombre: 'Juan',
      apellido: 'Pérez',
      email: 'juan.perez@example.com',
      cargo: 'Desarrollador',
      salario: 50000,
      fechaIngreso: '2026-07-17',
      ciudad: 'Bogota',
    }

    it('agrega el empleado al inicio de la lista cuando ok:true', async () => {
      vi.mocked(actions.crearEmpleado).mockResolvedValue({ ok: true, empleado: mockEmpleado })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado2]
      const result = await store.addEmployee(dto)

      expect(store.employees).toEqual([mockEmpleado, mockEmpleado2])
      expect(result).toEqual({ ok: true, empleado: mockEmpleado })
    })

    it('NO modifica la lista cuando ok:false', async () => {
      vi.mocked(actions.crearEmpleado).mockResolvedValue({ ok: false, message: 'Error' })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado2]
      const result = await store.addEmployee(dto)

      expect(store.employees).toEqual([mockEmpleado2])
      expect(result).toEqual({ ok: false, message: 'Error' })
    })
  })

  describe('updateEmployee', () => {
    it('actualiza el empleado en la lista y en currentEmployee cuando coincide el id', async () => {
      const actualizado = { ...mockEmpleado, salario: 60000 }
      vi.mocked(actions.editarEmpleado).mockResolvedValue({ ok: true, empleado: actualizado })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado, mockEmpleado2]
      store.currentEmployee = mockEmpleado

      const result = await store.updateEmployee(1, { salario: 60000 })

      expect(store.employees[0]).toEqual(actualizado)
      expect(store.currentEmployee).toEqual(actualizado)
      expect(result).toEqual({ ok: true, empleado: actualizado })
    })

    it('NO toca currentEmployee si el id no coincide', async () => {
      const actualizado = { ...mockEmpleado2, salario: 70000 }
      vi.mocked(actions.editarEmpleado).mockResolvedValue({ ok: true, empleado: actualizado })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado, mockEmpleado2]
      store.currentEmployee = mockEmpleado // id: 1

      await store.updateEmployee(2, { salario: 70000 })

      expect(store.currentEmployee).toEqual(mockEmpleado) // sin cambios
      expect(store.employees[1]).toEqual(actualizado)
    })

    it('NO modifica la lista cuando ok:false', async () => {
      vi.mocked(actions.editarEmpleado).mockResolvedValue({ ok: false, message: 'Error' })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado]

      const result = await store.updateEmployee(1, { salario: 60000 })

      expect(store.employees[0]).toEqual(mockEmpleado)
      expect(result).toEqual({ ok: false, message: 'Error' })
    })
  })

  describe('deleteEmployee', () => {
    it('elimina el empleado de la lista y limpia currentEmployee si coincide', async () => {
      vi.mocked(actions.eliminarEmpleado).mockResolvedValue({ ok: true })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado, mockEmpleado2]
      store.currentEmployee = mockEmpleado

      const result = await store.deleteEmployee(1)

      expect(store.employees).toEqual([mockEmpleado2])
      expect(store.currentEmployee).toBeNull()
      expect(result).toEqual({ ok: true })
    })

    it('NO modifica la lista cuando ok:false', async () => {
      vi.mocked(actions.eliminarEmpleado).mockResolvedValue({ ok: false, message: 'Error' })

      const store = useEmployeesStore()
      store.employees = [mockEmpleado]

      await store.deleteEmployee(1)

      expect(store.employees).toEqual([mockEmpleado])
    })
  })

  describe('fetchBenefits', () => {
    it('carga beneficios y ubicacion, y resetea benefitsServiceError', async () => {
      const mockData = {
        beneficios: [{ id: 1, empleadoId: 1, nombreBeneficio: 'Seguro', monto: 100 }],
        ubicacion: { lat: '4.6', lon: '-74.08', display_name: 'Bogota' },
      }
      vi.mocked(actions.listarBeneficiosPorEmpleado).mockResolvedValue(mockData)

      const store = useEmployeesStore()
      const promise = store.fetchBenefits(1)

      expect(store.loadingBenefits).toBe(true)
      await promise

      expect(store.currentBenefits).toEqual(mockData.beneficios)
      expect(store.currentUbicacion).toEqual(mockData.ubicacion)
      expect(store.benefitsServiceError).toBe(false)
      expect(store.loadingBenefits).toBe(false)
    })

    it('marca benefitsServiceError:true cuando el servicio falla', async () => {
      vi.mocked(actions.listarBeneficiosPorEmpleado).mockResolvedValue({
        beneficios: [],
        ubicacion: null,
        serviceError: true,
      })

      const store = useEmployeesStore()
      await store.fetchBenefits(1)

      expect(store.benefitsServiceError).toBe(true)
    })
  })

  describe('addBenefit', () => {
    it('agrega el beneficio a currentBenefits cuando ok:true', async () => {
      const nuevoBeneficio = { id: 1, empleadoId: 1, nombreBeneficio: 'Seguro', monto: 100 }
      vi.mocked(actions.crearBeneficio).mockResolvedValue({ ok: true, beneficio: nuevoBeneficio })

      const store = useEmployeesStore()
      const result = await store.addBenefit(1, 'Seguro', 100)

      expect(actions.crearBeneficio).toHaveBeenCalledWith({
        empleado_id: 1,
        nombre_beneficio: 'Seguro',
        monto: 100,
      })
      expect(store.currentBenefits).toEqual([nuevoBeneficio])
      expect(result).toEqual({ ok: true, beneficio: nuevoBeneficio })
    })

    it('NO modifica currentBenefits cuando ok:false', async () => {
      vi.mocked(actions.crearBeneficio).mockResolvedValue({ ok: false, message: 'Error' })

      const store = useEmployeesStore()
      const result = await store.addBenefit(1, 'Seguro', 100)

      expect(store.currentBenefits).toEqual([])
      expect(result).toEqual({ ok: false, message: 'Error' })
    })
  })

  describe('deleteBenefit', () => {
    it('elimina el beneficio de currentBenefits cuando ok:true', async () => {
      vi.mocked(actions.eliminarBeneficio).mockResolvedValue({ ok: true })

      const store = useEmployeesStore()
      store.currentBenefits = [
        { id: 1, empleadoId: 1, nombreBeneficio: 'Seguro', monto: 100 },
        { id: 2, empleadoId: 1, nombreBeneficio: 'Bono', monto: 200 },
      ]

      const result = await store.deleteBenefit(1)

      expect(store.currentBenefits).toEqual([
        { id: 2, empleadoId: 1, nombreBeneficio: 'Bono', monto: 200 },
      ])
      expect(result).toEqual({ ok: true })
    })

    it('NO modifica currentBenefits cuando ok:false', async () => {
      vi.mocked(actions.eliminarBeneficio).mockResolvedValue({ ok: false, message: 'Error' })

      const store = useEmployeesStore()
      const beneficios = [{ id: 1, empleadoId: 1, nombreBeneficio: 'Seguro', monto: 100 }]
      store.currentBenefits = beneficios

      await store.deleteBenefit(1)

      expect(store.currentBenefits).toEqual(beneficios)
    })
  })
})