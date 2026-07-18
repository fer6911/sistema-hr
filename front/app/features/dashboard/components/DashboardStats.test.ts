import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import DashboardStats from './DashboardStats.vue'

const mountStats = (props: { total: number; activos: number; nominaMensual: number }) =>
  mount(DashboardStats, {
    props,
    global: {
      stubs: {
        StatCard: {
          template: '<div class="stat-card"><span>{{ label }}</span><span>{{ value }}</span></div>',
          props: ['label', 'value', 'icon', 'accent'],
        },
      },
    },
  })

describe('DashboardStats', () => {
  it('renderiza las tres tarjetas de estadísticas', () => {
    const wrapper = mountStats({ total: 12, activos: 9, nominaMensual: 5400000 })

    const cards = wrapper.findAll('.stat-card')
    expect(cards.length).toBe(3)
  })

  it('muestra el total de empleados', () => {
    const wrapper = mountStats({ total: 12, activos: 9, nominaMensual: 5400000 })

    expect(wrapper.text()).toContain('Empleados totales')
    expect(wrapper.text()).toContain('12')
  })

  it('muestra el número de empleados activos', () => {
    const wrapper = mountStats({ total: 12, activos: 9, nominaMensual: 5400000 })

    expect(wrapper.text()).toContain('Empleados activos')
    expect(wrapper.text()).toContain('9')
  })

  it('formatea la nómina mensual como moneda COP', () => {
    const wrapper = mountStats({ total: 12, activos: 9, nominaMensual: 5400000 })

    expect(wrapper.text()).toContain('Nómina mensual')
    expect(wrapper.text()).toContain('$')
    expect(wrapper.text()).toMatch(/5\.400\.000|5,400,000/)
  })

  it('maneja valores en cero correctamente', () => {
    const wrapper = mountStats({ total: 0, activos: 0, nominaMensual: 0 })

    expect(wrapper.text()).toContain('0')
    expect(wrapper.text()).toContain('$')
  })
})