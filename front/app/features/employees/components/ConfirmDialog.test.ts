import { describe, it, expect, afterEach } from 'vitest'
import { mount, DOMWrapper } from '@vue/test-utils'
import ConfirmDialog from './ConfirmDialog.vue'

const baseProps = {
  open: true,
  title: 'Eliminar empleado',
  message: '¿Estás seguro de que deseas eliminar este empleado?',
}

const body = () => new DOMWrapper(document.body)

afterEach(() => {
  document.body.innerHTML = ''
})

describe('ConfirmDialog', () => {
  it('no renderiza el contenido cuando open es false', () => {
    mount(ConfirmDialog, { props: { ...baseProps, open: false } })

    expect(body().find('h3').exists()).toBe(false)
  })

  it('renderiza título y mensaje cuando open es true', () => {
    mount(ConfirmDialog, { props: baseProps })

    expect(body().find('h3').text()).toBe('Eliminar empleado')
    expect(body().text()).toContain('¿Estás seguro de que deseas eliminar este empleado?')
  })

  it('muestra "Confirmar" por defecto cuando no se pasa confirmText', () => {
    mount(ConfirmDialog, { props: baseProps })

    const confirmButton = body().findAll('button')[1]
    expect(confirmButton.text()).toBe('Confirmar')
  })

  it('muestra el texto de confirmText cuando se provee', () => {
    mount(ConfirmDialog, { props: { ...baseProps, confirmText: 'Eliminar' } })

    const confirmButton = body().findAll('button')[1]
    expect(confirmButton.text()).toBe('Eliminar')
  })

  it('emite "cancel" al hacer click en el botón Cancelar', async () => {
    const wrapper = mount(ConfirmDialog, { props: baseProps })

    const cancelButton = body().findAll('button')[0]
    await cancelButton.trigger('click')

    expect(wrapper.emitted('cancel')).toHaveLength(1)
  })

  it('emite "confirm" al hacer click en el botón de confirmar', async () => {
    const wrapper = mount(ConfirmDialog, { props: baseProps })

    const confirmButton = body().findAll('button')[1]
    await confirmButton.trigger('click')

    expect(wrapper.emitted('confirm')).toHaveLength(1)
  })

  it('emite "cancel" al hacer click en el overlay de fondo', async () => {
    const wrapper = mount(ConfirmDialog, { props: baseProps })

    await body().find('.absolute.inset-0').trigger('click')

    expect(wrapper.emitted('cancel')).toHaveLength(1)
  })

  it('aplica clases de variante "danger" al botón cuando variant es danger', () => {
    mount(ConfirmDialog, { props: { ...baseProps, variant: 'danger' } })

    const confirmButton = body().findAll('button')[1]
    expect(confirmButton.classes()).toContain('bg-error')
  })

  it('aplica clases de variante "primary" por defecto cuando no se especifica variant', () => {
    mount(ConfirmDialog, { props: baseProps })

    const confirmButton = body().findAll('button')[1]
    expect(confirmButton.classes()).toContain('btn-primary')
  })
})