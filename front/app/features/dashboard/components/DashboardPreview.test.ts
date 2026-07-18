import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import DashboardPreview from './DashboardPreview.vue'
import type { Employee } from '../interfaces'

const mockEmployees: Employee[] = [
  {
    id: 1,
    nombre: 'Juan',
    apellido: 'Pérez',
    cargo: 'Desarrollador',
    ciudad: 'Cali',
    activo: true,
  } as Employee,
  {
    id: 2,
    nombre: 'Ana',
    apellido: 'Gómez',
    cargo: 'Diseñadora',
    ciudad: 'Bogota',
    activo: false,
  } as Employee,
]

const mountPreview = (props: { employees: Employee[]; loading: boolean }) =>
  mount(DashboardPreview, {
    props,
    global: {
      stubs: {
        NuxtLink: {
          template: '<a :href="to"><slot /></a>',
          props: ['to'],
        },
        Icon: true,
      },
      mocks: {
        navigateTo: vi.fn(),
      },
    },
  })

describe('DashboardPreview', () => {
  it('renderiza el título y la descripción', () => {
    const wrapper = mountPreview({ employees: [], loading: false })

    expect(wrapper.text()).toContain('Empleados recientes')
    expect(wrapper.text()).toContain('Un vistazo rápido a tu equipo')
  })

  it('el link "Ver todos" apunta a /empleados', () => {
    const wrapper = mountPreview({ employees: [], loading: false })

    const link = wrapper.findAll('a').find((a) => a.text().includes('Ver todos'))
    expect(link?.attributes('href')).toBe('/empleados')
  })

  it('muestra placeholders de carga cuando loading es true', () => {
    const wrapper = mountPreview({ employees: [], loading: true })

    const skeletons = wrapper.findAll('.animate-pulse')
    expect(skeletons.length).toBe(4)
  })

  it('no muestra placeholders de carga cuando loading es false', () => {
    const wrapper = mountPreview({ employees: mockEmployees, loading: false })

    const skeletons = wrapper.findAll('.animate-pulse')
    expect(skeletons.length).toBe(0)
  })

  it('renderiza la lista de empleados con nombre, cargo y ciudad', () => {
    const wrapper = mountPreview({ employees: mockEmployees, loading: false })

    expect(wrapper.text()).toContain('Juan Pérez')
    expect(wrapper.text()).toContain('Desarrollador')
    expect(wrapper.text()).toContain('Cali')

    expect(wrapper.text()).toContain('Ana Gómez')
    expect(wrapper.text()).toContain('Diseñadora')
    expect(wrapper.text()).toContain('Bogota')
  })

  it('muestra el estado "Activo" e "Inactivo" según corresponda', () => {
    const wrapper = mountPreview({ employees: mockEmployees, loading: false })

    const badges = wrapper.findAll('span').map((s) => s.text())
    expect(badges).toContain('Activo')
    expect(badges).toContain('Inactivo')
  })

  it('muestra el mensaje de lista vacía cuando no hay empleados', () => {
    const wrapper = mountPreview({ employees: [], loading: false })

    expect(wrapper.text()).toContain('No hay empleados registrados.')
  })

  it('no muestra el mensaje de lista vacía cuando hay empleados', () => {
    const wrapper = mountPreview({ employees: mockEmployees, loading: false })

    expect(wrapper.text()).not.toContain('No hay empleados registrados.')
  })
})