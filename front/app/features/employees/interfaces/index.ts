export interface ApiResponse<T> {
  error: boolean
  message: string
  data: T
  errors: string[]
}

export interface EmpleadoDto {
  id: number
  nombre: string
  apellido: string
  email: string
  cargo: string
  salario: number
  fechaIngreso: string
  ciudad: string
  activo: boolean
}

export interface CrearEmpleadoDto {
  nombre: string
  apellido: string
  email: string
  cargo: string
  salario: number
  fechaIngreso: string
  ciudad: string
}

export interface EditarEmpleadoDto {
  nombre?: string
  apellido?: string
  email?: string
  cargo?: string
  salario?: number
  fechaIngreso?: string
  ciudad?: string
  activo?: boolean
}

export interface BeneficioDto {
  id: number
  empleado_id: number
  nombre_beneficio: string
  monto: number
}

export interface CrearBeneficioDto {
  empleado_id: number
  nombre_beneficio: string
  monto: number
}

export interface BeneficiosResponse {
  beneficios: BeneficioDto[]
  ubicacion: { lat: string; lon: string; display_name: string } | null
}
