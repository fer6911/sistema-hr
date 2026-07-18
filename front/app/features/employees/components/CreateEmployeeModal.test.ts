import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount, DOMWrapper } from '@vue/test-utils'
import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import CreateEmployeeModal from './CreateEmployeeModal.vue'

const addEmployeeMock = vi.fn()

mockNuxtImport('useEmployeesStore', () => {
  return () => ({
    addEmployee: addEmployeeMock,
  })
})

const body = () => new DOMWrapper(document.body)

afterEach(() => {
  document.body.innerHTML = ''
})

const fillForm = async (overrides: Partial<Record<string, string>> = {}) => {
  const data = {
    nombre: 'Laura',
    apellido: 'Jiménez',
    cargo: 'Analista de Nómina',
    ciudad: 'Cali',
    email: 'laura@atlashr.co',
    salario: '3500000',
    ...overrides,
  }

  await body().find('input[type="text"]').setValue(data.nombre)
  const textInputs = body().findAll('input[type="text"]')
  await textInputs[1]!.setValue(data.apellido)
  await textInputs[2]!.setValue(data.cargo)
  await textInputs[3]!.setValue(data.ciudad)
  await body().find('input[type="email"]').setValue(data.email)
  if (data.salario) {
    await body().find('input[type="number"]').setValue(data.salario)
  }
}

describe('CreateEmployeeModal', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('no renderiza el modal cuando open es false', () => {
    mount(CreateEmployeeModal, { props: { open: false } })

    expect(body().find('h3').exists()).toBe(false)
  })

  it('renderiza el formulario cuando open es true', () => {
    mount(CreateEmployeeModal, { props: { open: true } })

    expect(body().find('h3').text()).toBe('Nuevo empleado')
    expect(body().find('input[type="email"]').exists()).toBe(true)
  })

  it('muestra error de validación si faltan campos requeridos', async () => {
    mount(CreateEmployeeModal, { props: { open: true } })

    await body().find('form').trigger('submit')

    expect(body().text()).toContain('Completa nombre, apellido, cargo, ciudad, correo y salario.')
    expect(addEmployeeMock).not.toHaveBeenCalled()
  })

  it('llama a addEmployee con los datos correctos al enviar el formulario completo', async () => {
    addEmployeeMock.mockResolvedValue({ ok: true })
    mount(CreateEmployeeModal, { props: { open: true } })

    await fillForm()
    await body().find('form').trigger('submit')

    expect(addEmployeeMock).toHaveBeenCalledWith(
      expect.objectContaining({
        nombre: 'Laura',
        apellido: 'Jiménez',
        cargo: 'Analista de Nómina',
        ciudad: 'Cali',
        email: 'laura@atlashr.co',
        salario: 3500000,
      })
    )
  })

  it('emite "created" y "close" cuando addEmployee resuelve ok:true', async () => {
    addEmployeeMock.mockResolvedValue({ ok: true })
    const wrapper = mount(CreateEmployeeModal, { props: { open: true } })

    await fillForm()
    await body().find('form').trigger('submit')

    expect(wrapper.emitted('created')).toHaveLength(1)
    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('limpia el formulario cuando addEmployee resuelve ok:true', async () => {
    addEmployeeMock.mockResolvedValue({ ok: true })
    mount(CreateEmployeeModal, { props: { open: true } })

    await fillForm()
    await body().find('form').trigger('submit')

    const nombreInput = body().find('input[type="text"]').element as HTMLInputElement
    expect(nombreInput.value).toBe('')
  })

  it('muestra el mensaje de error cuando addEmployee resuelve ok:false', async () => {
    addEmployeeMock.mockResolvedValue({ ok: false, message: 'El email ya está registrado' })
    const wrapper = mount(CreateEmployeeModal, { props: { open: true } })

    await fillForm()
    await body().find('form').trigger('submit')

    expect(body().text()).toContain('El email ya está registrado')
    expect(wrapper.emitted('created')).toBeUndefined()
    expect(wrapper.emitted('close')).toBeUndefined()
  })

  it('emite "close" al hacer click en el botón Cancelar', async () => {
    const wrapper = mount(CreateEmployeeModal, { props: { open: true } })

    const cancelButton = body().findAll('button').find((b) => b.text() === 'Cancelar')
    await cancelButton!.trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('emite "close" al hacer click en el botón de la X', async () => {
    const wrapper = mount(CreateEmployeeModal, { props: { open: true } })

    const closeButton = body().findAll('button')[0]
    await closeButton!.trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('emite "close" al hacer click en el overlay de fondo', async () => {
    const wrapper = mount(CreateEmployeeModal, { props: { open: true } })

    await body().find('.absolute.inset-0').trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('muestra "Creando…" mientras submitting está activo', async () => {
    let resolvePromise: (v: any) => void
    addEmployeeMock.mockReturnValue(new Promise((resolve) => { resolvePromise = resolve }))

    mount(CreateEmployeeModal, { props: { open: true } })
    await fillForm()

    const submitPromise = body().find('form').trigger('submit')
    await Promise.resolve()

    const submitButton = body().findAll('button').find((b) => b.text().includes('Creando'))
    expect(submitButton).toBeTruthy()

    resolvePromise!({ ok: true })
    await submitPromise
  })
})