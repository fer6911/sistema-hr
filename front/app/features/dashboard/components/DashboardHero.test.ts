import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import DashboardHero from './DashboardHero.vue'

const mountHero = () =>
  mount(DashboardHero, {
    global: {
      stubs: {
        NuxtLink: {
          template: '<a :href="to"><slot /></a>',
          props: ['to'],
        },
        Icon: true,
        AtlasRidge: true,
      },
    },
  })

describe('DashboardHero', () => {
  it('renderiza el título principal', () => {
    const wrapper = mountHero()

    expect(wrapper.text()).toContain('La cima de tu gestión de')
    expect(wrapper.text()).toContain('talento humano')
  })

  it('renderiza el badge superior', () => {
    const wrapper = mountHero()

    expect(wrapper.text()).toContain('Dashboard unificado de nómina')
  })

  it('renderiza la descripción', () => {
    const wrapper = mountHero()

    expect(wrapper.text()).toContain('AtlasHR conecta')
  })

  it('el link "Ver empleados" apunta a /empleados', () => {
    const wrapper = mountHero()

    const link = wrapper.findAll('a').find((a) => a.text().includes('Ver empleados'))
    expect(link?.attributes('href')).toBe('/empleados')
  })

  it('el link "Solicitar demo" apunta a #contacto', () => {
    const wrapper = mountHero()

    const link = wrapper.findAll('a').find((a) => a.text().includes('Solicitar demo'))
    expect(link?.attributes('href')).toBe('#contacto')
  })

  it('renderiza el componente AtlasRidge decorativo', () => {
    const wrapper = mountHero()

    expect(wrapper.findComponent({ name: 'AtlasRidge' }).exists()).toBe(true)
  })
})