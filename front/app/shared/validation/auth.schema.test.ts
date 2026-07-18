import { describe, it, expect } from 'vitest'
import { LoginSchema, RegisterSchema } from './auth.schema'

describe('LoginSchema', () => {
  it('valida correctamente datos completos', () => {
    const result = LoginSchema.safeParse({ username: 'pablo', password: '123456' })

    expect(result.success).toBe(true)
  })

  it('falla si username está vacío', () => {
    const result = LoginSchema.safeParse({ username: '', password: '123456' })

    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues[0].message).toBe('El nombre de usuario es requerido')
    }
  })

  it('falla si password tiene menos de 6 caracteres', () => {
    const result = LoginSchema.safeParse({ username: 'pablo', password: '123' })

    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues[0].message).toBe('Mínimo 6 caracteres')
    }
  })

  it('falla si falta el campo username', () => {
    const result = LoginSchema.safeParse({ password: '123456' })

    expect(result.success).toBe(false)
  })

  it('falla si falta el campo password', () => {
    const result = LoginSchema.safeParse({ username: 'pablo' })

    expect(result.success).toBe(false)
  })
})

describe('RegisterSchema', () => {
  const validData = {
    username: 'pablo',
    email: 'pablo@example.com',
    password: '123456',
    confirmPassword: '123456',
  }

  it('valida correctamente datos completos y consistentes', () => {
    const result = RegisterSchema.safeParse(validData)

    expect(result.success).toBe(true)
  })

  it('falla si username está vacío', () => {
    const result = RegisterSchema.safeParse({ ...validData, username: '' })

    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues.some((i) => i.message === 'El nombre de usuario es requerido')).toBe(true)
    }
  })

  it('falla si username supera los 30 caracteres', () => {
    const result = RegisterSchema.safeParse({ ...validData, username: 'a'.repeat(31) })

    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues.some((i) => i.message === 'Máximo 30 caracteres')).toBe(true)
    }
  })

  it('acepta username de exactamente 30 caracteres (límite superior)', () => {
    const result = RegisterSchema.safeParse({ ...validData, username: 'a'.repeat(30) })

    expect(result.success).toBe(true)
  })

  it('falla si el email es inválido', () => {
    const result = RegisterSchema.safeParse({ ...validData, email: 'correo-invalido' })

    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues.some((i) => i.message === 'Email inválido')).toBe(true)
    }
  })

  it('falla si password tiene menos de 6 caracteres', () => {
    const result = RegisterSchema.safeParse({ ...validData, password: '123', confirmPassword: '123' })

    expect(result.success).toBe(false)
  })

  it('falla si confirmPassword no coincide con password', () => {
    const result = RegisterSchema.safeParse({ ...validData, confirmPassword: 'diferente' })

    expect(result.success).toBe(false)
    if (!result.success) {
      const issue = result.error.issues.find((i) => i.path.includes('confirmPassword'))
      expect(issue?.message).toBe('Las contraseñas no coinciden')
    }
  })

  it('falla si falta algún campo requerido', () => {
    const result = RegisterSchema.safeParse({ username: 'pablo', email: 'pablo@example.com' })

    expect(result.success).toBe(false)
  })
})

describe('Tipos inferidos', () => {
  it('LoginInput tiene la forma esperada', () => {
    const login: import('./auth.schema').LoginInput = { username: 'pablo', password: '123456' }
    expect(login.username).toBe('pablo')
  })

  it('RegisterInput tiene la forma esperada', () => {
    const register: import('./auth.schema').RegisterInput = {
      username: 'pablo',
      email: 'pablo@example.com',
      password: '123456',
      confirmPassword: '123456',
    }
    expect(register.email).toBe('pablo@example.com')
  })
})