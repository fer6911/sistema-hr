import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import LocationCard from './LocationCard.vue'

const { lookupCityMock, nominatimState } = vi.hoisted(() => {
  return {
    lookupCityMock: vi.fn(),
    nominatimState: {
      loading: false,
      error: null as string | null,
      result: null as { displayName: string; lat: string; lon: string } | null,
    },
  }
})

mockNuxtImport('useNominatim', () => {
  return () => ({
    ...nominatimState,
    lookupCity: lookupCityMock,
  })
})

describe('LocationCard', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    nominatimState.loading = false
    nominatimState.error = null
    nominatimState.result = null
  })

  it('llama a lookupCity con la ciudad recibida al montar', () => {
    mount(LocationCard, { props: { city: 'Bogota' } })

    expect(lookupCityMock).toHaveBeenCalledWith('Bogota')
  })

  it('muestra el estado de carga cuando loading es true', () => {
    nominatimState.loading = true

    const wrapper = mount(LocationCard, { props: { city: 'Bogota' } })

    expect(wrapper.text()).toContain('Consultando OpenStreetMap para "Bogota"…')
  })

  it('muestra el mensaje de error cuando error tiene valor', () => {
    nominatimState.error = 'Ciudad no encontrada'

    const wrapper = mount(LocationCard, { props: { city: 'CiudadInexistente' } })

    expect(wrapper.text()).toContain('No se pudo geolocalizar CiudadInexistente: Ciudad no encontrada')
    expect(wrapper.find('button').text()).toBe('Reintentar')
  })

  it('llama de nuevo a lookupCity al hacer click en Reintentar', async () => {
    nominatimState.error = 'Error de red'

    const wrapper = mount(LocationCard, { props: { city: 'Bogota' } })
    lookupCityMock.mockClear() // limpiar la llamada inicial de onMounted

    await wrapper.find('button').trigger('click')

    expect(lookupCityMock).toHaveBeenCalledWith('Bogota')
  })

  it('muestra el resultado con nombre y coordenadas formateadas', () => {
    nominatimState.result = {
      displayName: 'Bogotá, Colombia',
      lat: '4.60971',
      lon: '-74.08175',
    }

    const wrapper = mount(LocationCard, { props: { city: 'Bogota' } })

    expect(wrapper.text()).toContain('Bogotá, Colombia')
    expect(wrapper.text()).toContain('lat 4.6097')
    expect(wrapper.text()).toContain('lon -74.0817')
  })

  it('no muestra loading, error ni resultado si todos están vacíos/nulos', () => {
    const wrapper = mount(LocationCard, { props: { city: 'Bogota' } })

    expect(wrapper.text()).not.toContain('Consultando')
    expect(wrapper.text()).not.toContain('No se pudo geolocalizar')
    expect(wrapper.find('button').exists()).toBe(false)
  })
})