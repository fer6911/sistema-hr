export interface GeoResult {
  lat: string
  lon: string
  displayName: string
}

export const useNominatim = () => {
  const loading = ref(false)
  const error = ref<string | null>(null)
  const result = ref<GeoResult | null>(null)

  const lookupCity = async (city: string) => {
    if (!import.meta.client) return
    loading.value = true
    error.value = null
    result.value = null

    try {
      const url = `https://nominatim.openstreetmap.org/search?city=${encodeURIComponent(
        city
      )}&format=xml&limit=1`
      const res = await fetch(url, {
        headers: { Accept: 'application/xml' },
      })
      if (!res.ok) throw new Error(`Nominatim respondió ${res.status}`)

      const xmlText = await res.text()
      const doc = new DOMParser().parseFromString(xmlText, 'text/xml')
      const place = doc.querySelector('place')

      if (!place) throw new Error('Sin coincidencias para esta ciudad')

      result.value = {
        lat: place.getAttribute('lat') ?? '',
        lon: place.getAttribute('lon') ?? '',
        displayName: place.getAttribute('display_name') ?? city,
      }
    } catch (e) {
      error.value = e instanceof Error ? e.message : 'No fue posible consultar Nominatim'
    } finally {
      loading.value = false
    }
  }

  return { loading, error, result, lookupCity }
}
