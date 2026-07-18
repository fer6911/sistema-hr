<script setup lang="ts">
const store = useEmployeesStore()
const authStore = useAuthStore()
const authModal = useAuthModalStore()

const search = ref('')
const statusFilter = ref<'todos' | 'activo' | 'inactivo'>('todos')
const modalOpen = ref(false)
const deleteOpen = ref(false)
const deleteTarget = ref<{ id: number; nombre: string } | null>(null)
const page = ref(1)
const perPage = 8

onMounted(() => {
  if (store.employees.length === 0) store.fetchEmployees()
})

const fullName = (e: { nombre: string; apellido: string }) => `${e.nombre} ${e.apellido}`

const filtered = computed(() =>
  store.employees.filter((e) => {
    const name = fullName(e).toLowerCase()
    const matchesSearch =
      name.includes(search.value.toLowerCase()) ||
      e.cargo.toLowerCase().includes(search.value.toLowerCase()) ||
      e.ciudad.toLowerCase().includes(search.value.toLowerCase())
    const matchesStatus =
      statusFilter.value === 'todos' ||
      (statusFilter.value === 'activo' && e.activo) ||
      (statusFilter.value === 'inactivo' && !e.activo)
    return matchesSearch && matchesStatus
  })
)

const totalPages = computed(() => Math.max(1, Math.ceil(filtered.value.length / perPage)))

const paginated = computed(() => {
  const start = (page.value - 1) * perPage
  return filtered.value.slice(start, start + perPage)
})

watch([search, statusFilter], () => { page.value = 1 })

const initials = (e: { nombre: string; apellido: string }) =>
  `${e.nombre[0] ?? ''}${e.apellido[0] ?? ''}`.toUpperCase()

const openDelete = (e: { id: number; nombre: string }, event: Event) => {
  event.stopPropagation()
  deleteTarget.value = e
  deleteOpen.value = true
}

const confirmDelete = async () => {
  if (!deleteTarget.value) return
  await store.deleteEmployee(deleteTarget.value.id)
  deleteTarget.value = null
  deleteOpen.value = false
}
</script>

<template>
  <div class="mx-auto max-w-7xl px-4 sm:px-6 py-10">
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h1 class="text-2xl font-heading font-extrabold text-white">Empleados</h1>
        <p class="text-sm text-gray mt-1">{{ filtered.length }} de {{ store.employees.length }} registros</p>
      </div>
      <template v-if="authStore.isAuthenticated">
        <button class="btn-primary text-sm font-semibold px-5 py-2.5 rounded-xl inline-flex items-center gap-2" @click="modalOpen = true">
          <Icon name="ph:plus-bold" class="w-4 h-4" />
          Nuevo empleado
        </button>
      </template>
      <button v-else class="text-sm text-gray hover:text-white transition-colors inline-flex items-center gap-2" @click="authModal.openLogin()">
        <Icon name="ph:lock-key-bold" class="w-4 h-4" />
        Inicie sesión para gestionar empleados
      </button>
    </div>

    <!-- Barra de filtros -->
    <div class="card p-4 mb-6 flex flex-col sm:flex-row gap-3 sm:items-center">
      <div class="relative flex-1">
        <Icon name="ph:magnifying-glass-bold" class="w-4 h-4 text-gray absolute left-3.5 top-1/2 -translate-y-1/2" />
        <input v-model="search" type="text" placeholder="Buscar por nombre, cargo o ciudad…" class="w-full pl-10 pr-4 py-2.5 text-sm" />
      </div>
      <div class="flex gap-2">
        <button
          v-for="opt in [
            { key: 'todos', label: 'Todos' },
            { key: 'activo', label: 'Activos' },
            { key: 'inactivo', label: 'Inactivos' },
          ]"
          :key="opt.key"
          class="px-3.5 py-2 rounded-lg text-xs font-medium transition-colors"
          :class="statusFilter === opt.key ? 'bg-primary text-white' : 'text-gray hover:text-white bg-white/5'"
          @click="statusFilter = opt.key as typeof statusFilter"
        >
          {{ opt.label }}
        </button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="store.loading" class="card p-10 text-center text-gray text-sm">
      Cargando empleados…
    </div>

    <!-- Tabla -->
    <div v-else class="card overflow-hidden">
      <table>
        <thead>
          <tr>
            <th class="text-left">Empleado</th>
            <th class="text-left hidden sm:table-cell">Cargo</th>
            <th class="text-left hidden md:table-cell">Ciudad</th>
            <th class="text-left">Estado</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="e in paginated" :key="e.id" class="cursor-pointer" @click="navigateTo(`/empleados/${e.id}`)">
            <td>
              <div class="flex items-center gap-3">
                <div
                  class="w-9 h-9 rounded-full flex items-center justify-center text-xs font-bold text-white shrink-0"
                  style="background: linear-gradient(135deg, var(--color-primary), var(--color-accent))"
                >
                  {{ initials(e) }}
                </div>
                <div>
                  <p class="font-medium text-white">{{ fullName(e) }}</p>
                  <p class="text-xs text-gray sm:hidden">{{ e.cargo }}</p>
                </div>
              </div>
            </td>
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
            <td @click.stop>
              <div class="flex items-center gap-2 justify-end">
                <button
                  class="p-1.5 rounded-lg text-gray hover:text-error hover:bg-error/10 transition-colors"
                  title="Eliminar"
                  @click="openDelete({ id: e.id, nombre: fullName(e) }, $event)"
                >
                  <Icon name="ph:trash-bold" class="w-4 h-4" />
                </button>
                <Icon name="ph:caret-right-bold" class="w-4 h-4 text-gray" />
              </div>
            </td>
          </tr>
          <tr v-if="filtered.length === 0">
            <td colspan="5" class="text-center py-10 text-gray text-sm">
              No hay empleados que coincidan con tu búsqueda.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Paginación -->
    <div v-if="totalPages > 1" class="flex items-center justify-between mt-4">
      <p class="text-xs text-gray">
        Página {{ page }} de {{ totalPages }}
      </p>
      <div class="flex gap-1.5">
        <button
          class="px-3 py-1.5 rounded-lg text-xs font-medium transition-colors"
          :class="page === 1 ? 'text-gray/40 cursor-not-allowed' : 'text-gray hover:text-white bg-white/5 hover:bg-white/10'"
          :disabled="page === 1"
          @click="page--"
        >
          Anterior
        </button>
        <button
          v-for="p in totalPages"
          :key="p"
          class="w-8 h-8 rounded-lg text-xs font-medium transition-colors"
          :class="p === page ? 'bg-primary text-white' : 'text-gray hover:text-white bg-white/5 hover:bg-white/10'"
          @click="page = p"
        >
          {{ p }}
        </button>
        <button
          class="px-3 py-1.5 rounded-lg text-xs font-medium transition-colors"
          :class="page === totalPages ? 'text-gray/40 cursor-not-allowed' : 'text-gray hover:text-white bg-white/5 hover:bg-white/10'"
          :disabled="page === totalPages"
          @click="page++"
        >
          Siguiente
        </button>
      </div>
    </div>

    <CreateEmployeeModal :open="modalOpen" @close="modalOpen = false" />
    <ConfirmDialog
      :open="deleteOpen"
      title="Eliminar empleado"
      :message="`¿Seguro que deseas eliminar a ${deleteTarget?.nombre}? Esta acción no se puede deshacer.`"
      confirm-text="Eliminar"
      variant="danger"
      @confirm="confirmDelete"
      @cancel="deleteOpen = false"
    />
  </div>
</template>
