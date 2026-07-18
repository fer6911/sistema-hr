<script setup lang="ts">
const props = defineProps<{ employeeId: number }>()
const store = useEmployeesStore()
const authStore = useAuthStore()
const authModal = useAuthModalStore()

const nombre = ref('')
const monto = ref<number | null>(null)
const open = ref(false)
const submitting = ref(false)

const openForm = () => {
  if (!authStore.isAuthenticated) {
    authModal.openLogin()
    return
  }
  open.value = true
}

const submit = async () => {
  if (!nombre.value || !monto.value) return
  submitting.value = true
  const result = await store.addBenefit(props.employeeId, nombre.value, monto.value)
  submitting.value = false
  if (result.ok) {
    nombre.value = ''
    monto.value = null
    open.value = false
  }
}
</script>

<template>
  <div>
    <button
      v-if="!open"
      class="btn-secondary text-xs font-medium px-3.5 py-2 rounded-lg inline-flex items-center gap-1.5"
      :title="authStore.isAuthenticated ? 'Añadir beneficio' : 'Inicia sesión para añadir beneficio'"
      @click="openForm"
    >
      <Icon :name="authStore.isAuthenticated ? 'ph:plus-bold' : 'ph:lock-key-bold'" class="w-3.5 h-3.5" />
      Añadir beneficio
    </button>

    <form v-else class="flex flex-col sm:flex-row gap-2.5" @submit.prevent="submit">
      <input v-model="nombre" type="text" placeholder="Nombre del beneficio" class="flex-1 px-3.5 py-2 text-sm" />
      <input v-model.number="monto" type="number" placeholder="Monto" class="w-full sm:w-32 px-3.5 py-2 text-sm" />
      <div class="flex gap-2">
        <button type="submit" class="btn-primary text-xs font-semibold px-3.5 py-2 rounded-lg" :disabled="submitting">
          {{ submitting ? 'Guardando…' : 'Guardar' }}
        </button>
        <button type="button" class="btn-secondary text-xs font-medium px-3.5 py-2 rounded-lg" @click="open = false">
          Cancelar
        </button>
      </div>
    </form>
  </div>
</template>