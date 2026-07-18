import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { EmpleadoDto, BeneficioDto, BeneficiosResponse } from '../interfaces'
import {
  listarEmpleados,
  obtenerEmpleado,
  crearEmpleado,
  editarEmpleado,
  eliminarEmpleado,
  listarBeneficiosPorEmpleado,
  crearBeneficio,
  eliminarBeneficio,
} from '../actions'

export const useEmployeesStore = defineStore('employees', () => {
  const employees = ref<EmpleadoDto[]>([])
  const loading = ref(false)
  const currentEmployee = ref<EmpleadoDto | null>(null)
  const currentBenefits = ref<BeneficioDto[]>([])
  const currentUbicacion = ref<{ lat: string; lon: string; display_name: string } | null>(null)
  const loadingBenefits = ref(false)
  const benefitsServiceError = ref(false)

  const fetchEmployees = async () => {
    loading.value = true
    employees.value = await listarEmpleados()
    loading.value = false
  }

  const fetchEmployee = async (id: number) => {
    loading.value = true
    currentEmployee.value = await obtenerEmpleado(id)
    loading.value = false
    return currentEmployee.value
  }

  const addEmployee = async (dto: {
    nombre: string
    apellido: string
    email: string
    cargo: string
    salario: number
    fechaIngreso: string
    ciudad: string
  }) => {
    const result = await crearEmpleado(dto)
    if (result.ok) {
      employees.value.unshift(result.empleado)
    }
    return result
  }

  const updateEmployee = async (id: number, dto: {
    nombre?: string
    apellido?: string
    email?: string
    cargo?: string
    salario?: number
    fechaIngreso?: string
    ciudad?: string
    activo?: boolean
  }) => {
    const result = await editarEmpleado(id, dto)
    if (result.ok) {
      const idx = employees.value.findIndex((e) => e.id === id)
      if (idx !== -1) employees.value[idx] = result.empleado
      if (currentEmployee.value?.id === id) currentEmployee.value = result.empleado
    }
    return result
  }

  const deleteEmployee = async (id: number) => {
    const result = await eliminarEmpleado(id)
    if (result.ok) {
      employees.value = employees.value.filter((e) => e.id !== id)
      if (currentEmployee.value?.id === id) currentEmployee.value = null
    }
    return result
  }

  const fetchBenefits = async (empleadoId: number) => {
    loadingBenefits.value = true
    const data = await listarBeneficiosPorEmpleado(empleadoId)
    currentBenefits.value = data.beneficios
    currentUbicacion.value = data.ubicacion
    benefitsServiceError.value = data.serviceError ?? false
    loadingBenefits.value = false
  }

  const addBenefit = async (empleadoId: number, nombreBeneficio: string, monto: number) => {
    const result = await crearBeneficio({ empleado_id: empleadoId, nombre_beneficio: nombreBeneficio, monto })
    if (result.ok) {
      currentBenefits.value.push(result.beneficio)
    }
    return result
  }

  const deleteBenefit = async (benefitId: number) => {
    const result = await eliminarBeneficio(benefitId)
    if (result.ok) {
      currentBenefits.value = currentBenefits.value.filter((b) => b.id !== benefitId)
    }
    return result
  }

  return {
    employees,
    loading,
    currentEmployee,
    currentBenefits,
    currentUbicacion,
    loadingBenefits,
    benefitsServiceError,
    fetchEmployees,
    fetchEmployee,
    addEmployee,
    updateEmployee,
    deleteEmployee,
    fetchBenefits,
    addBenefit,
    deleteBenefit,
  }
})