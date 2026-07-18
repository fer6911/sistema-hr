import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import LocationMap from './LocationMap.vue'

const {
  mapFn,
  mapInstanceMock,
  controlZoomFn,
  controlZoomInstanceMock,
  tileLayerFn,
  tileLayerInstanceMock,
  divIconFn,
  markerFn,
  markerInstanceMock,
} = vi.hoisted(() => {
  const mapInstanceMock = { remove: vi.fn(), invalidateSize: vi.fn() }
  const controlZoomInstanceMock = { addTo: vi.fn() }
  const tileLayerInstanceMock = { addTo: vi.fn() }
  const markerInstanceMock = {
    addTo: vi.fn(function (this: any) { return this }),
    bindPopup: vi.fn(function (this: any) { return this }),
    openPopup: vi.fn(function (this: any) { return this }),
  }

  return {
    mapFn: vi.fn(() => mapInstanceMock),
    mapInstanceMock,
    controlZoomFn: vi.fn(() => controlZoomInstanceMock),
    controlZoomInstanceMock,
    tileLayerFn: vi.fn(() => tileLayerInstanceMock),
    tileLayerInstanceMock,
    divIconFn: vi.fn((opts) => opts),
    markerFn: vi.fn(() => markerInstanceMock),
    markerInstanceMock,
  }
})

vi.mock('leaflet', () => ({
  map: mapFn,
  control: { zoom: controlZoomFn },
  tileLayer: tileLayerFn,
  divIcon: divIconFn,
  marker: markerFn,
}))

vi.mock('leaflet/dist/leaflet.css', () => ({}))

describe('LocationMap', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('inicializa el mapa con el centro y zoom correctos al montar', async () => {
    mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    expect(mapFn).toHaveBeenCalledWith(
      expect.anything(),
      expect.objectContaining({
        center: [4.6097, -74.0817],
        zoom: 12,
        zoomControl: false,
        attributionControl: false,
      })
    )
  })

  it('agrega el control de zoom al mapa', async () => {
    mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    expect(controlZoomFn).toHaveBeenCalledWith({ position: 'bottomright' })
    expect(controlZoomInstanceMock.addTo).toHaveBeenCalledWith(mapInstanceMock)
  })

  it('agrega la capa de tiles al mapa', async () => {
    mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    expect(tileLayerFn).toHaveBeenCalledWith(
      'https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png',
      { maxZoom: 19 }
    )
    expect(tileLayerInstanceMock.addTo).toHaveBeenCalledWith(mapInstanceMock)
  })

  it('crea el marcador en las coordenadas correctas y abre el popup con el nombre de la ciudad', async () => {
    mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    expect(markerFn).toHaveBeenCalledWith([4.6097, -74.0817], expect.anything())
    expect(markerInstanceMock.addTo).toHaveBeenCalledWith(mapInstanceMock)
    expect(markerInstanceMock.bindPopup).toHaveBeenCalledWith(
      expect.stringContaining('Bogota')
    )
    expect(markerInstanceMock.openPopup).toHaveBeenCalledOnce()
  })

  it('reinicializa el mapa cuando cambian lat/lon', async () => {
    const wrapper = mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    expect(mapFn).toHaveBeenCalledTimes(1)

    await wrapper.setProps({ lat: 6.2442, lon: -75.5812, city: 'Medellin' })
    await flushPromises()

    expect(mapInstanceMock.remove).toHaveBeenCalledOnce()
    expect(mapFn).toHaveBeenCalledTimes(2)
    expect(mapFn).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({ center: [6.2442, -75.5812] })
    )
  })

  it('remueve la instancia del mapa al desmontar', async () => {
    const wrapper = mount(LocationMap, { props: { lat: 4.6097, lon: -74.0817, city: 'Bogota' } })
    await flushPromises()

    wrapper.unmount()

    expect(mapInstanceMock.remove).toHaveBeenCalled()
  })
})