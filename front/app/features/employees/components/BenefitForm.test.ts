import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import BenefitForm from './BenefitForm.vue'

const addBenefitMock = vi.fn()
const openLoginMock = vi.fn()
let isAuthenticated = true

mockNuxtImport('useEmployeesStore', () => {
  return () => ({
    addBenefit: addBenefitMock,
  })
})

mockNuxtImport('useAuthStore', () => {
  return () => ({
    get isAuthenticated() {
      return isAuthenticated
    },
  })
})

mockNuxtImport('useAuthModalStore', () => {
  return () => ({
    openLogin: openLoginMock,
  })
})

describe('BenefitForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    isAuthenticated = true
  })

  it('muestra el botón "Añadir beneficio" inicialmente', () => {
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    expect(wrapper.find('button').text()).toContain('Añadir beneficio')
    expect(wrapper.find('form').exists()).toBe(false)
  })

  it('abre el formulario al hacer click si el usuario está autenticado', async () => {
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')

    expect(wrapper.find('form').exists()).toBe(true)
    expect(openLoginMock).not.toHaveBeenCalled()
  })

  it('abre el modal de login en vez del formulario si NO está autenticado', async () => {
    isAuthenticated = false
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')

    expect(wrapper.find('form').exists()).toBe(false)
    expect(openLoginMock).toHaveBeenCalledOnce()
  })

  it('llama a addBenefit con los valores correctos al enviar el formulario', async () => {
    addBenefitMock.mockResolvedValue({ ok: true })
    const wrapper = mount(BenefitForm, { props: { employeeId: 5 } })

    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="text"]').setValue('Seguro médico')
    await wrapper.find('input[type="number"]').setValue(150)
    await wrapper.find('form').trigger('submit')

    expect(addBenefitMock).toHaveBeenCalledWith(5, 'Seguro médico', 150)
  })

  it('limpia y cierra el formulario cuando addBenefit resuelve ok:true', async () => {
    addBenefitMock.mockResolvedValue({ ok: true })
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="text"]').setValue('Bono')
    await wrapper.find('input[type="number"]').setValue(100)
    await wrapper.find('form').trigger('submit')
    await wrapper.vm.$nextTick()

    expect(wrapper.find('form').exists()).toBe(false)
  })

  it('mantiene el formulario abierto si addBenefit resuelve ok:false', async () => {
    addBenefitMock.mockResolvedValue({ ok: false, message: 'Error' })
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="text"]').setValue('Bono')
    await wrapper.find('input[type="number"]').setValue(100)
    await wrapper.find('form').trigger('submit')
    await wrapper.vm.$nextTick()

    expect(wrapper.find('form').exists()).toBe(true)
  })

  it('NO llama a addBenefit si falta el nombre', async () => {
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="number"]').setValue(100)
    await wrapper.find('form').trigger('submit')

    expect(addBenefitMock).not.toHaveBeenCalled()
  })

  it('NO llama a addBenefit si falta el monto', async () => {
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="text"]').setValue('Bono')
    await wrapper.find('form').trigger('submit')

    expect(addBenefitMock).not.toHaveBeenCalled()
  })

  it('cierra el formulario al hacer click en Cancelar', async () => {
    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })

    await wrapper.find('button').trigger('click')
    expect(wrapper.find('form').exists()).toBe(true)

    await wrapper.find('button[type="button"]').trigger('click')

    expect(wrapper.find('form').exists()).toBe(false)
  })

  it('muestra "Guardando…" mientras submitting está activo', async () => {
    let resolvePromise: (v: any) => void
    addBenefitMock.mockReturnValue(new Promise((resolve) => { resolvePromise = resolve }))

    const wrapper = mount(BenefitForm, { props: { employeeId: 1 } })
    await wrapper.find('button').trigger('click')
    await wrapper.find('input[type="text"]').setValue('Bono')
    await wrapper.find('input[type="number"]').setValue(100)

    const submitPromise = wrapper.find('form').trigger('submit')
    await wrapper.vm.$nextTick()

    expect(wrapper.find('button[type="submit"]').text()).toContain('Guardando')

    resolvePromise!({ ok: true })
    await submitPromise
  })
})