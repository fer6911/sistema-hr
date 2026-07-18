<script setup lang="ts">
const props = defineProps<{ city: string }>()
const { loading, error, result, lookupCity } = useNominatim()

onMounted(() => lookupCity(props.city))
</script>

<template>
  <div class="card relative overflow-hidden p-5">
    <AtlasRidge class="absolute -bottom-2 left-0 w-full h-20 opacity-25" />

    <div class="relative flex items-center gap-2 mb-4">
      <Icon name="ph:map-pin-fill" class="w-4 h-4 text-accent" />
      <h4 class="text-sm font-heading font-semibold text-white">Ubicación (Nominatim)</h4>
    </div>

    <div v-if="loading" class="relative flex items-center gap-2 text-sm text-gray py-3">
      <Icon name="ph:spinner-gap-bold" class="w-4 h-4 animate-spin" />
      Consultando OpenStreetMap para "{{ city }}"…
    </div>

    <div v-else-if="error" class="relative text-sm text-warning py-2">
      No se pudo geolocalizar {{ city }}: {{ error }}
      <button class="block mt-2 text-xs text-accent-light underline" @click="lookupCity(city)">
        Reintentar
      </button>
    </div>

    <div v-else-if="result" class="relative space-y-3">
      <p class="text-xs text-silver leading-relaxed">{{ result.displayName }}</p>
      <div class="flex gap-2">
        <span class="px-3 py-1.5 rounded-lg bg-primary-dark/60 border border-white/5 text-xs font-mono text-accent-light">
          lat {{ Number(result.lat).toFixed(4) }}
        </span>
        <span class="px-3 py-1.5 rounded-lg bg-primary-dark/60 border border-white/5 text-xs font-mono text-accent-light">
          lon {{ Number(result.lon).toFixed(4) }}
        </span>
      </div>
    </div>
  </div>
</template>
