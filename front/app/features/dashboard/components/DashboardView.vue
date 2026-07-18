<script setup lang="ts">
import { listarEmpleados } from '../actions'
import type { Employee } from '../interfaces'

const employees = ref<Employee[]>([])
const loading = ref(true)

onMounted(async () => {
  employees.value = await listarEmpleados()
  loading.value = false
})

const preview = computed(() => employees.value.slice(0, 4))

const stats = computed(() => {
  const total = employees.value.length
  const activos = employees.value.filter((e) => e.activo).length
  const nominaMensual = employees.value
    .filter((e) => e.activo)
    .reduce((sum, e) => sum + Number(e.salario), 0)
  return { total, activos, nominaMensual }
})
</script>

<template>
  <div>
    <DashboardHero />

    <DashboardStats
      :total="stats.total"
      :activos="stats.activos"
      :nomina-mensual="stats.nominaMensual"
    />

    <DashboardPreview :employees="preview" :loading="loading" />
  </div>
</template>
