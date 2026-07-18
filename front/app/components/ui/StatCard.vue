<script setup lang="ts">
defineProps<{
  label: string
  value: string
  icon: string
  accent?: 'primary' | 'accent' | 'success'
}>()

const accentClasses: Record<string, string> = {
  primary: 'from-primary to-primary-light text-primary-light',
  accent: 'from-accent-dark to-accent text-accent-light',
  success: 'from-success to-accent text-success',
}
</script>

<template>
  <div class="card p-5 animate-slide-up">
    <div class="flex items-center justify-between mb-4">
      <div
        class="w-10 h-10 rounded-xl flex items-center justify-center bg-gradient-to-br"
        :class="accentClasses[accent ?? 'primary']"
      >
        <Icon :name="icon" class="w-5 h-5 text-white" />
      </div>
    </div>
    <p class="text-2xl font-heading font-extrabold text-white">{{ value }}</p>
    <p class="text-xs text-gray mt-1">{{ label }}</p>
  </div>
</template>
