<script setup lang="ts">
import type { EmpleadoDto } from '../interfaces'

const props = defineProps<{ open: boolean; employee: EmpleadoDto | null }>()
const emit = defineEmits<{ close: []; updated: [] }>()

const store = useEmployeesStore()

const form = reactive({
  nombre: '',
  apellido: '',
  cargo: '',
  ciudad: '',
  email: '',
  salario: null as number | null,
})

const errors = ref<string | null>(null)
const submitting = ref(false)

watch(
  () => props.employee,
  (emp) => {
    if (emp) {
      form.nombre = emp.nombre
      form.apellido = emp.apellido
      form.cargo = emp.cargo
      form.ciudad = emp.ciudad
      form.email = emp.email
      form.salario = emp.salario
      errors.value = null
    }
  },
  { immediate: true }
)

const submit = async () => {
  if (!props.employee) return
  if (!form.nombre || !form.apellido || !form.cargo || !form.ciudad || !form.salario || !form.email) {
    errors.value = 'Completa nombre, apellido, cargo, ciudad, correo y salario.'
    return
  }
  submitting.value = true
  const result = await store.updateEmployee(props.employee.id, {
    nombre: form.nombre,
    apellido: form.apellido,
    cargo: form.cargo,
    ciudad: form.ciudad,
    email: form.email,
    salario: form.salario,
  })
  submitting.value = false

  if (result.ok) {
    emit('updated')
    emit('close')
  } else {
    errors.value = result.message
  }
}
</script>

<template>
  <Teleport to="body">
    <Transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div v-if="props.open" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
        <div class="absolute inset-0 bg-black/70 backdrop-blur-sm" @click="emit('close')" />

        <div class="relative card w-full max-w-lg p-6 sm:p-7 animate-slide-up">
          <div class="flex items-center justify-between mb-5">
            <h3 class="text-lg font-heading font-bold text-white">Editar empleado</h3>
            <button class="text-gray hover:text-white" @click="emit('close')">
              <Icon name="ph:x-bold" class="w-5 h-5" />
            </button>
          </div>

          <form class="space-y-4" @submit.prevent="submit">
            <div class="grid grid-cols-2 gap-4">
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Nombre</span>
                <input v-model="form.nombre" type="text" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Apellido</span>
                <input v-model="form.apellido" type="text" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Cargo</span>
                <input v-model="form.cargo" type="text" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Ciudad</span>
                <input v-model="form.ciudad" type="text" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Correo</span>
                <input v-model="form.email" type="email" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
              <label class="col-span-2 sm:col-span-1 block">
                <span class="text-xs text-gray mb-1.5 block">Salario (COP)</span>
                <input v-model.number="form.salario" type="number" class="w-full px-3.5 py-2.5 text-sm" />
              </label>
            </div>

            <p v-if="errors" class="text-error text-xs">{{ errors }}</p>

            <div class="flex justify-end gap-3 pt-2">
              <button type="button" class="btn-secondary text-sm font-medium px-4 py-2.5 rounded-lg" @click="emit('close')">
                Cancelar
              </button>
              <button type="submit" class="btn-primary text-sm font-semibold px-5 py-2.5 rounded-lg" :disabled="submitting">
                {{ submitting ? 'Guardando…' : 'Guardar cambios' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
