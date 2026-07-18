<script setup lang="ts">
defineProps<{
  open: boolean
  title: string
  message: string
  confirmText?: string
  variant?: 'danger' | 'primary'
}>()

const emit = defineEmits<{ confirm: []; cancel: [] }>()
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
      <div v-if="open" class="fixed inset-0 z-[70] flex items-center justify-center p-4">
        <div class="absolute inset-0 bg-black/70 backdrop-blur-sm" @click="emit('cancel')" />

        <div class="relative card w-full max-w-md p-6 sm:p-7 animate-slide-up">
          <div class="flex items-center gap-3 mb-4">
            <div
              class="w-10 h-10 rounded-xl flex items-center justify-center shrink-0"
              :class="variant === 'danger' ? 'bg-error/15' : 'bg-primary/15'"
            >
              <Icon
                :name="variant === 'danger' ? 'ph:warning-fill' : 'ph:info-fill'"
                class="w-5 h-5"
                :class="variant === 'danger' ? 'text-error' : 'text-info'"
              />
            </div>
            <h3 class="text-lg font-heading font-bold text-white">{{ title }}</h3>
          </div>

          <p class="text-sm text-silver leading-relaxed mb-6">{{ message }}</p>

          <div class="flex justify-end gap-3">
            <button
              class="btn-secondary text-sm font-medium px-4 py-2.5 rounded-lg"
              @click="emit('cancel')"
            >
              Cancelar
            </button>
            <button
              class="text-sm font-semibold px-5 py-2.5 rounded-lg text-white transition-all"
              :class="variant === 'danger'
                ? 'bg-error hover:bg-error/80'
                : 'btn-primary'"
              @click="emit('confirm')"
            >
              {{ confirmText || 'Confirmar' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
