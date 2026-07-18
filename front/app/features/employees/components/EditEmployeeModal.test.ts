import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount, DOMWrapper } from '@vue/test-utils'
import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import EditEmployeeModal from './EditEmployeeModal.vue'

const updateEmployeeMock = vi.fn()

mockNuxtImport('useEmployeesStore', () => {
  return () => ({
    updateEmployee: updateEmployeeMock,
  })
})

const body = () => new DOMWrapper(document.body)

afterEach(() => {
  document.body.innerHTML = ''
})

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

describe('EditEmployeeModal', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('no renderiza el modal cuando open es false', () => {
    mount(EditEmployeeModal, { props: { open: false, employee: mockEmployee } })

    expect(body().find('h3').exists()).toBe(false)
  })

  it('renderiza el formulario cuando open es true', () => {
    mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    expect(body().find('h3').text()).toBe('Editar empleado')
  })

  it('precarga el formulario con los datos del empleado', () => {
    mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const textInputs = body().findAll('input[type="text"]')
    expect((textInputs[0]!.element as HTMLInputElement).value).toBe('Juan')
    expect((textInputs[1]!.element as HTMLInputElement).value).toBe('Pérez')
    expect((textInputs[2]!.element as HTMLInputElement).value).toBe('Desarrollador')
    expect((textInputs[3]!.element as HTMLInputElement).value).toBe('Bogota')
    expect((body().find('input[type="email"]').element as HTMLInputElement).value).toBe('juan.perez@example.com')
    expect((body().find('input[type="number"]').element as HTMLInputElement).value).toBe('50000')
  })

  it('actualiza el formulario cuando cambia la prop employee', async () => {
    const wrapper = mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const otroEmpleado = { ...mockEmployee, id: 2, nombre: 'Maria' }
    await wrapper.setProps({ employee: otroEmpleado })

    const nombreInput = body().find('input[type="text"]').element as HTMLInputElement
    expect(nombreInput.value).toBe('Maria')
  })

  it('muestra error de validación si se vacía un campo requerido', async () => {
    mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const nombreInput = body().find('input[type="text"]')
    await nombreInput.setValue('')
    await body().find('form').trigger('submit')

    expect(body().text()).toContain('Completa nombre, apellido, cargo, ciudad, correo y salario.')
    expect(updateEmployeeMock).not.toHaveBeenCalled()
  })

  it('llama a updateEmployee con el id y los datos correctos', async () => {
    updateEmployeeMock.mockResolvedValue({ ok: true })
    mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const textInputs = body().findAll('input[type="text"]')
    await textInputs[2]!.setValue('Senior Developer') // cambia cargo
    await body().find('form').trigger('submit')

    expect(updateEmployeeMock).toHaveBeenCalledWith(1, {
      nombre: 'Juan',
      apellido: 'Pérez',
      cargo: 'Senior Developer',
      ciudad: 'Bogota',
      email: 'juan.perez@example.com',
      salario: 50000,
    })
  })

  it('NO llama a updateEmployee si employee es null', async () => {
    mount(EditEmployeeModal, { props: { open: true, employee: null } })

    await body().find('form').trigger('submit')

    expect(updateEmployeeMock).not.toHaveBeenCalled()
  })

  it('emite "updated" y "close" cuando updateEmployee resuelve ok:true', async () => {
    updateEmployeeMock.mockResolvedValue({ ok: true })
    const wrapper = mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    await body().find('form').trigger('submit')

    expect(wrapper.emitted('updated')).toHaveLength(1)
    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('muestra el mensaje de error cuando updateEmployee resuelve ok:false', async () => {
    updateEmployeeMock.mockResolvedValue({ ok: false, message: 'El email ya está en uso' })
    const wrapper = mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    await body().find('form').trigger('submit')

    expect(body().text()).toContain('El email ya está en uso')
    expect(wrapper.emitted('updated')).toBeUndefined()
    expect(wrapper.emitted('close')).toBeUndefined()
  })

  it('emite "close" al hacer click en Cancelar', async () => {
    const wrapper = mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const cancelButton = body().findAll('button').find((b) => b.text() === 'Cancelar')
    await cancelButton!.trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('emite "close" al hacer click en el overlay de fondo', async () => {
    const wrapper = mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    await body().find('.absolute.inset-0').trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('muestra "Guardando…" mientras submitting está activo', async () => {
    let resolvePromise: (v: any) => void
    updateEmployeeMock.mockReturnValue(new Promise((resolve) => { resolvePromise = resolve }))

    mount(EditEmployeeModal, { props: { open: true, employee: mockEmployee } })

    const submitPromise = body().find('form').trigger('submit')
    await Promise.resolve()

    const submitButton = body().findAll('button').find((b) => b.text().includes('Guardando'))
    expect(submitButton).toBeTruthy()

    resolvePromise!({ ok: true })
    await submitPromise
  })
})