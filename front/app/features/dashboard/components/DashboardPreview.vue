<script setup lang="ts">
import type { Employee } from '../interfaces'

defineProps<{ employees: Employee[]; loading: boolean }>()
</script>

<template>
  <section class="mx-auto max-w-7xl px-4 sm:px-6 py-16">
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-heading font-bold text-white">Empleados recientes</h2>
        <p class="text-sm text-gray mt-1">Un vistazo rápido a tu equipo</p>
      </div>
      <NuxtLink to="/empleados" class="text-sm text-accent-light hover:text-accent flex items-center gap-1">
        Ver todos
        <Icon name="ph:arrow-right-bold" class="w-3.5 h-3.5" />
      </NuxtLink>
    </div>

    <div class="card overflow-hidden">
      <table>
        <thead>
          <tr>
            <th class="text-left">Empleado</th>
            <th class="text-left hidden sm:table-cell">Cargo</th>
            <th class="text-left hidden md:table-cell">Ciudad</th>
            <th class="text-left">Estado</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="loading">
            <tr v-for="n in 4" :key="n">
              <td colspan="4" class="py-4">
                <div class="h-4 bg-white/5 rounded animate-pulse w-3/4" />
              </td>
            </tr>
          </template>
          <template v-else>
            <tr
              v-for="e in employees"
              :key="e.id"
              class="cursor-pointer"
              @click="navigateTo(`/empleados/${e.id}`)"
            >
              <td class="font-medium text-white">{{ e.nombre }}</td>
              <td class="hidden sm:table-cell">{{ e.cargo }}</td>
              <td class="hidden md:table-cell">{{ e.ciudad }}</td>
              <td>
                <span
                  class="text-xs font-medium px-2.5 py-1 rounded-full"
                  :class="e.activo ? 'text-success bg-success/10' : 'text-gray bg-white/5'"
                >
                  {{ e.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
            </tr>
            <tr v-if="employees.length === 0">
              <td colspan="4" class="text-center py-10 text-gray text-sm">
                No hay empleados registrados.
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </section>
</template>
