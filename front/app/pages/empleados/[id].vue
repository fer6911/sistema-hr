<script setup lang="ts">
const route = useRoute()
const router = useRouter()
const store = useEmployeesStore()
const authStore = useAuthStore()
const authModal = useAuthModalStore()
const { result: geoResult, lookupCity } = useNominatim()

const editOpen = ref(false)
const deleteOpen = ref(false)

onMounted(async () => {
  const id = Number(route.params.id)
  const emp = await store.fetchEmployee(id)
  if (emp) {
    lookupCity(emp.ciudad)
    store.fetchBenefits(id)
  }
})

const employee = computed(() => store.currentEmployee)

const currency = (n: number) =>
  new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n)

const fullName = computed(() =>
  employee.value ? `${employee.value.nombre} ${employee.value.apellido}` : ''
)

const initials = computed(() => {
  if (!employee.value) return ''
  return `${employee.value.nombre[0] ?? ''}${employee.value.apellido[0] ?? ''}`.toUpperCase()
})

const totalBenefits = computed(
  () => store.currentBenefits.reduce((sum, b) => sum + b.monto, 0) ?? 0
)

const requireAuth = () => {
  if (!authStore.isAuthenticated) {
    authModal.openLogin()
    return false
  }
  return true
}

const openEdit = () => {
  if (!requireAuth()) return
  editOpen.value = true
}

const openDelete = () => {
  if (!requireAuth()) return
  deleteOpen.value = true
}

const confirmDelete = async () => {
  if (!employee.value) return
  const id = employee.value.id
  await store.deleteEmployee(id)
  router.push('/empleados')
}

const toggleEstado = async () => {
  if (!requireAuth()) return
  if (!employee.value) return
  await store.updateEmployee(employee.value.id, {
    activo: !employee.value.activo,
  })
}

watch(
  () => employee.value?.ciudad,
  (city) => { if (city) lookupCity(city) }
)
</script>

<template>
  <div class="mx-auto max-w-7xl px-4 sm:px-6 py-10">
    <NuxtLink to="/empleados" class="inline-flex items-center gap-1.5 text-sm text-gray hover:text-white mb-6">
      <Icon name="ph:arrow-left-bold" class="w-4 h-4" />
      Volver a empleados
    </NuxtLink>

    <div v-if="store.loading" class="card p-10 text-center text-gray text-sm">
      Cargando empleado…
    </div>

    <div v-else-if="!employee" class="card p-10 text-center text-gray">
      No se encontró este empleado.
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Profile -->
      <div class="lg:col-span-2 space-y-6">
        <div class="card p-6 sm:p-7">
          <div class="flex flex-col sm:flex-row sm:items-center gap-5">
            <div
              class="w-16 h-16 rounded-2xl flex items-center justify-center text-xl font-bold text-white shrink-0"
              style="background: linear-gradient(135deg, var(--color-primary), var(--color-accent))"
            >
              {{ initials }}
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-3 flex-wrap">
                <h1 class="text-xl font-heading font-bold text-white">{{ fullName }}</h1>
                <span
                  class="text-xs font-medium px-2.5 py-1 rounded-full"
                  :class="employee.activo ? 'text-success bg-success/10' : 'text-gray bg-white/5'"
                >
                  {{ employee.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </div>
              <p class="text-sm text-gray mt-1">{{ employee.cargo }} · {{ employee.ciudad }}</p>
            </div>
          </div>

          <!-- Actions -->
          <div class="flex flex-wrap gap-2 mt-5">
            <button
              class="btn-secondary text-xs font-medium px-3.5 py-2 rounded-lg inline-flex items-center gap-1.5"
              :title="authStore.isAuthenticated ? 'Editar' : 'Inicia sesión para editar'"
              @click="openEdit"
            >
              <Icon :name="authStore.isAuthenticated ? 'ph:pencil-simple-bold' : 'ph:lock-key-bold'" class="w-3.5 h-3.5" />
              Editar
            </button>
            <button
              class="text-xs font-medium px-3.5 py-2 rounded-lg inline-flex items-center gap-1.5 transition-colors"
              :class="employee.activo
                ? 'text-warning bg-warning/10 hover:bg-warning/20'
                : 'text-success bg-success/10 hover:bg-success/20'"
              :title="authStore.isAuthenticated ? (employee.activo ? 'Desactivar' : 'Activar') : 'Inicia sesión para cambiar el estado'"
              @click="toggleEstado"
            >
              <Icon :name="authStore.isAuthenticated ? (employee.activo ? 'ph:pause-bold' : 'ph:play-bold') : 'ph:lock-key-bold'" class="w-3.5 h-3.5" />
              {{ employee.activo ? 'Desactivar' : 'Activar' }}
            </button>
            <button
              class="text-xs font-medium px-3.5 py-2 rounded-lg inline-flex items-center gap-1.5 text-error bg-error/10 hover:bg-error/20 transition-colors"
              :title="authStore.isAuthenticated ? 'Eliminar' : 'Inicia sesión para eliminar'"
              @click="openDelete"
            >
              <Icon :name="authStore.isAuthenticated ? 'ph:trash-bold' : 'ph:lock-key-bold'" class="w-3.5 h-3.5" />
              Eliminar
            </button>
          </div>

          <div class="ascent-divider my-6" />

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-5 text-sm">
            <div>
              <p class="text-xs text-gray mb-1">Correo</p>
              <p class="text-silver truncate">{{ employee.email }}</p>
            </div>
            <div>
              <p class="text-xs text-gray mb-1">Ingreso</p>
              <p class="text-silver">{{ employee.fechaIngreso }}</p>
            </div>
            <div>
              <p class="text-xs text-gray mb-1">Salario base</p>
              <p class="text-silver">{{ currency(employee.salario) }}</p>
            </div>
            <div>
              <p class="text-xs text-gray mb-1">Ciudad</p>
              <p class="text-silver">{{ employee.ciudad }}</p>
            </div>
          </div>
        </div>

        <!-- Benefits -->
        <div class="card p-6 sm:p-7">
          <div class="flex items-center justify-between mb-5">
            <div>
              <h3 class="text-base font-heading font-bold text-white">Beneficios</h3>
              <p class="text-xs text-gray mt-1">Total mensual: {{ currency(totalBenefits) }}</p>
            </div>
          </div>

          <div v-if="store.loadingBenefits" class="text-sm text-gray py-3">
            Cargando beneficios…
          </div>
          <div v-else class="space-y-2.5 mb-5">
            <div
              v-for="b in store.currentBenefits"
              :key="b.id"
              class="flex items-center justify-between px-4 py-3 rounded-xl bg-white/[.02] border border-white/5"
            >
              <div class="flex items-center gap-3">
                <Icon name="ph:gift-fill" class="w-4 h-4 text-accent" />
                <span class="text-sm text-white">{{ b.nombre_beneficio }}</span>
              </div>
              <div class="flex items-center gap-4">
                <span class="text-sm text-silver font-mono">{{ currency(b.monto) }}</span>
                <button class="text-gray hover:text-error" @click="store.deleteBenefit(b.id)">
                  <Icon name="ph:trash-bold" class="w-4 h-4" />
                </button>
              </div>
            </div>
            <p v-if="store.currentBenefits.length === 0" class="text-sm text-gray py-3">
              Este empleado aún no tiene beneficios asignados.
            </p>
          </div>

          <BenefitForm :employee-id="employee.id" />
        </div>
      </div>

      <!-- Location -->
      <div class="space-y-6">
        <LocationCard :city="employee.ciudad" />
        <div v-if="geoResult" class="card p-5">
          <div class="flex items-center gap-2 mb-4">
            <Icon name="ph:map-trifold-fill" class="w-4 h-4 text-accent" />
            <h4 class="text-sm font-heading font-semibold text-white">Mapa</h4>
          </div>
          <LocationMap :lat="Number(geoResult.lat)" :lon="Number(geoResult.lon)" :city="employee.ciudad" />
        </div>
      </div>
    </div>

    <!-- Modals -->
    <EditEmployeeModal :open="editOpen" :employee="employee" @close="editOpen = false" @updated="editOpen = false" />
    <ConfirmDialog
      :open="deleteOpen"
      title="Eliminar empleado"
      :message="`¿Seguro que deseas eliminar a ${fullName}? Esta acción no se puede deshacer.`"
      confirm-text="Eliminar"
      variant="danger"
      @confirm="confirmDelete"
      @cancel="deleteOpen = false"
    />
  </div>
</template>