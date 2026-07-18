<script setup lang="ts">
import "leaflet/dist/leaflet.css"

const props = defineProps<{
  lat: number
  lon: number
  city: string
}>()

const mapContainer = ref<HTMLElement | null>(null)
let mapInstance: any = null

const initMap = async () => {
  if (!import.meta.client || !mapContainer.value) return

  const L = await import("leaflet")

  if (mapInstance) {
    mapInstance.remove()
    mapInstance = null
  }

  mapInstance = L.map(mapContainer.value, {
    center: [props.lat, props.lon],
    zoom: 12,
    zoomControl: false,
    attributionControl: false,
  })

  L.control.zoom({ position: "bottomright" }).addTo(mapInstance)

  L.tileLayer("https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png", {
    maxZoom: 19,
  }).addTo(mapInstance)

  const icon = L.divIcon({
    className: "custom-marker",
    html: `<div style="width:14px;height:14px;border-radius:50%;background:var(--color-accent);border:3px solid rgba(255,255,255,.9);box-shadow:0 0 12px rgba(22,198,201,.6)"></div>`,
    iconSize: [14, 14],
    iconAnchor: [7, 7],
  })

  L.marker([props.lat, props.lon], { icon })
    .addTo(mapInstance)
    .bindPopup(`<span style="font-weight:600">${props.city}</span>`)
    .openPopup()

  setTimeout(() => mapInstance.invalidateSize(), 100)
}

watch(
  () => [props.lat, props.lon],
  () => initMap()
)

onMounted(() => initMap())

onUnmounted(() => {
  if (mapInstance) {
    mapInstance.remove()
    mapInstance = null
  }
})
</script>

<template>
  <div ref="mapContainer" class="w-full h-56 rounded-xl overflow-hidden" />
</template>
