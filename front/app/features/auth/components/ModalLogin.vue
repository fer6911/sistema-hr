<template>
  <div class="p-8">
    <!-- Header -->
    <div class="text-center mb-8">
      <div class="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-primary/10 border border-primary/20 mb-4">
        <Icon name="ph:mountains-fill" class="w-7 h-7 text-accent" />
      </div>
      <h2 class="text-2xl font-heading font-bold text-white">Bienvenido de vuelta</h2>
      <p class="text-sm text-gray mt-1.5">Inicia sesión para acceder al dashboard</p>
    </div>

    <!-- Form -->
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Nombre de usuario</label>
        <div class="relative">
          <Icon name="ph:user" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.username"
            type="text"
            placeholder="admin"
            required
            class="w-full pl-10 pr-4 py-2.5 text-sm rounded-xl border bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:ring-2 focus:ring-accent/15 transition-all"
            :class="fieldErrors.username ? 'border-error' : 'border-white/5 focus:border-accent'"
          />
        </div>
        <p v-if="fieldErrors.username" class="text-error text-xs mt-1">{{ fieldErrors.username }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium text-silver mb-1.5">Contraseña</label>
        <div class="relative">
          <Icon name="ph:lock-simple" class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4.5 h-4.5 text-gray" />
          <input
            v-model="form.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="••••••••"
            required
            class="w-full pl-10 pr-11 py-2.5 text-sm rounded-xl border bg-surface-light text-white placeholder:text-gray/60 focus:outline-none focus:ring-2 focus:ring-accent/15 transition-all"
            :class="fieldErrors.password ? 'border-error' : 'border-white/5 focus:border-accent'"
          />
          <button
            type="button"
            class="absolute right-3 top-1/2 -translate-y-1/2 text-gray hover:text-white transition-colors"
            @click="showPassword = !showPassword"
          >
            <Icon :name="showPassword ? 'ph:eye-slash' : 'ph:eye'" class="w-4.5 h-4.5" />
          </button>
        </div>
        <p v-if="fieldErrors.password" class="text-error text-xs mt-1">{{ fieldErrors.password }}</p>
      </div>

      <div v-if="serverError" class="flex items-center gap-2 text-sm text-error bg-error/10 border border-error/20 rounded-xl px-4 py-2.5">
        <Icon name="ph:warning-circle" class="w-4 h-4 shrink-0" />
        {{ serverError }}
      </div>

      <button
        type="submit"
        :disabled="loading"
        class="w-full btn-primary py-2.5 rounded-xl text-sm font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <span v-if="loading" class="inline-flex items-center gap-2">
          <Icon name="ph:spinner" class="w-4 h-4 animate-spin" />
          Iniciando sesión...
        </span>
        <span v-else>Iniciar sesión</span>
      </button>
    </form>

    <!-- Footer -->
    <p class="text-center text-sm text-gray mt-6">
      ¿No tienes una cuenta?
      <button class="text-accent-light hover:text-accent font-medium transition-colors" @click="$emit('switchView', 'register')">
        Regístrate
      </button>
    </p>
  </div>
</template>

<script setup lang="ts">
import { LoginSchema, type LoginInput } from '~/shared/validation/auth.schema'

const emit = defineEmits<{
  switchView: [view: 'login' | 'register']
  success: []
}>()

const auth = useAuthStore()

const form = reactive<LoginInput>({ username: '', password: '' })
const showPassword = ref(false)
const loading = ref(false)
const serverError = ref('')
const fieldErrors = ref<Record<string, string>>({})

const clearErrors = () => {
  serverError.value = ''
  fieldErrors.value = {}
}

const handleSubmit = async () => {
  clearErrors()

  const result = LoginSchema.safeParse(form)
  if (!result.success) {
    for (const issue of result.error.issues) {
      const field = issue.path[0] as string
      if (!fieldErrors.value[field]) {
        fieldErrors.value[field] = issue.message
      }
    }
    return
  }

  loading.value = true

  const success = await auth.login(result.data.username, result.data.password)

  if (success) {
    emit('success')
  } else {
    serverError.value = 'Usuario o contraseña incorrectos'
  }

  loading.value = false
}
</script>
