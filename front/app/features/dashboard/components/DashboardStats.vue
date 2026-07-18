<script setup lang="ts">
defineProps<{
  total: number
  activos: number
  nominaMensual: number
}>()

const currency = (n: number) =>
  new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n)
</script>

<template>
  <section class="mx-auto max-w-7xl px-4 sm:px-6 -mt-10 relative z-10">
    <div class="grid grid-cols-2 lg:grid-cols-3 gap-4">
      <StatCard label="Empleados totales" :value="String(total)" icon="ph:users-three-fill" accent="primary" />
      <StatCard label="Empleados activos" :value="String(activos)" icon="ph:check-circle-fill" accent="success" />
      <StatCard label="Nómina mensual" :value="currency(nominaMensual)" icon="ph:coins-fill" accent="primary" />
    </div>
  </section>
</template>
