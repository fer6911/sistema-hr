<template>
  <div class="p-8">
    <!-- Header -->
    <div class="text-center mb-8">
      <div class="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-accent/10 border border-accent/20 mb-4">
        <Icon name="ph:user-plus-bold" class="w-7 h-7 text-accent" />
      </div>
      <h2 class="text-2xl font-heading font-bold text-white">Crear cuenta</h2>
      <p class="text-sm text-gray mt-1.5">Regístrate para empezar a usar AtlasHR</p>
    </div>

    <!-- Form -->
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Nombre completo</label>
        <div class="relative">
          <Icon name="ph:user" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.nombre"
            type="text"
            placeholder="Juan Pérez"
            required
            class="w-full pl-10 pr-4 py-2.5 text-sm rounded-xl border border-white/5 bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:border-accent focus:ring-2 focus:ring-accent/15 transition-all"
          />
        </div>
      </div>

      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Correo electrónico</label>
        <div class="relative">
          <Icon name="ph:envelope-simple" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.email"
            type="email"
            placeholder="juan@atlashr.co"
            required
            class="w-full pl-10 pr-4 py-2.5 text-sm rounded-xl border border-white/5 bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:border-accent focus:ring-2 focus:ring-accent/15 transition-all"
          />
        </div>
      </div>

      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Contraseña</label>
        <div class="relative">
          <Icon name="ph:lock-simple" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="Mínimo 6 caracteres"
            required
            minlength="6"
            class="w-full pl-10 pr-11 py-2.5 text-sm rounded-xl border border-white/5 bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:border-accent focus:ring-2 focus:ring-accent/15 transition-all"
          />
          <button
            type="button"
            class="absolute right-3 top-1/2 -translate-y-1/2 text-gray hover:text-white transition-colors"
            @click="showPassword = !showPassword"
          >
            <Icon :name="showPassword ? 'ph:eye-slash' : 'ph:eye'" class="w-4.5 h-4.5" />
          </button>
        </div>
      </div>

      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Confirmar contraseña</label>
        <div class="relative">
          <Icon name="ph:lock-simple" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.confirmPassword"
            :type="showPassword ? 'text' : 'password'"
            placeholder="Repite tu contraseña"
            required
            class="w-full pl-10 pr-4 py-2.5 text-sm rounded-xl border border-white/5 bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:border-accent focus:ring-2 focus:ring-accent/15 transition-all"
          />
        </div>
      </div>

      <div v-if="error" class="flex items-center gap-2 text-sm text-error bg-error/10 border border-error/20 rounded-xl px-4 py-2.5">
        <Icon name="ph:warning-circle" class="w-4 h-4 shrink-0" />
        {{ error }}
      </div>

      <button
        type="submit"
        :disabled="loading"
        class="w-full btn-primary py-2.5 rounded-xl text-sm font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <span v-if="loading" class="inline-flex items-center gap-2">
          <Icon name="ph:spinner" class="w-4 h-4 animate-spin" />
          Creando cuenta...
        </span>
        <span v-else>Crear cuenta</span>
      </button>
    </form>

    <!-- Footer -->
    <p class="text-center text-sm text-gray mt-6">
      ¿Ya tienes una cuenta?
      <button class="text-accent-light hover:text-accent font-medium transition-colors" @click="$emit('switchView', 'login')">
        Inicia sesión
      </button>
    </p>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  switchView: [view: 'login' | 'register']
  success: []
}>()

const auth = useAuthStore()

const form = ref({ nombre: '', email: '', password: '', confirmPassword: '' })
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')

const handleSubmit = async () => {
  loading.value = true
  error.value = ''

  if (form.value.password !== form.value.confirmPassword) {
    error.value = 'Las contraseñas no coinciden'
    loading.value = false
    return
  }

  await new Promise((r) => setTimeout(r, 600))

  const resultado = auth.register(form.value.nombre, form.value.email, form.value.password)

  if (resultado.exito) {
    emit('success')
  } else {
    error.value = resultado.mensaje
  }

  loading.value = false
}
</script>
