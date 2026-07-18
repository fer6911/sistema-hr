<template>
  <header
    class="fixed top-0 inset-x-0 z-50 transition-all duration-300"
    :class="scrolled ? 'py-1' : 'py-3'"
    :style="{
      backdropFilter: 'blur(20px)',
      WebkitBackdropFilter: 'blur(20px)',
      background: 'rgba(17,24,39,.72)'
    }"
  >
    <div class="flex items-center justify-between px-4 sm:px-6">
      <!-- Logo -->
      <NuxtLink to="/" class="flex items-center gap-2.5 shrink-0">
        <img src="/images/logo.png" alt="AtlasHR" class="h-20 w-auto object-contain" />
        <span class="font-heading font-extrabold text-2xl tracking-tight text-white">
          Atlas<span class="text-accent">HR</span>
        </span>
      </NuxtLink>

      <!-- Desktop nav -->
      <nav class="hidden md:flex items-center gap-1">
        <NuxtLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          class="relative px-4 py-2 text-sm font-medium rounded-lg transition-colors"
          :class="isActive(link.to) ? 'text-white' : 'text-gray hover:text-white'"
        >
          {{ link.label }}
          <span
            v-if="isActive(link.to)"
            class="absolute left-4 right-4 -bottom-0.5 h-0.5 rounded-full"
            style="background: linear-gradient(90deg, var(--color-primary-light), var(--color-accent))"
          />
        </NuxtLink>
      </nav>

      <div class="hidden md:flex items-center gap-3">
        <NuxtLink to="/login" class="btn-primary text-sm font-semibold px-4 py-2 rounded-lg">
          Iniciar sesión
        </NuxtLink>
      </div>

      <!-- Mobile toggle -->
      <button
        class="md:hidden p-2 text-white"
        aria-label="Abrir menú"
        @click="mobileOpen = !mobileOpen"
      >
        <Icon :name="mobileOpen ? 'ph:x-bold' : 'ph:list-bold'" class="w-6 h-6" />
      </button>
    </div>

    <!-- Mobile menu -->
    <Transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0 -translate-y-2"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 -translate-y-2"
    >
      <div v-if="mobileOpen" class="px-4 sm:px-6 pb-3 md:hidden">
        <div class="glass mt-2 rounded-2xl p-3 flex flex-col gap-1">
          <NuxtLink
            v-for="link in links"
            :key="link.to"
            :to="link.to"
            class="px-4 py-2.5 rounded-lg text-sm font-medium"
            :class="isActive(link.to) ? 'text-white bg-white/5' : 'text-gray'"
            @click="mobileOpen = false"
          >
            {{ link.label }}
          </NuxtLink>
          <NuxtLink to="/login" class="btn-primary text-center text-sm font-semibold px-4 py-2.5 rounded-lg mt-1" @click="mobileOpen = false">
            Iniciar sesión
          </NuxtLink>
        </div>
      </div>
    </Transition>
  </header>
</template>

<script setup lang="ts">
const route = useRoute()
const mobileOpen = ref(false)

const links = [
  { label: 'Dashboard', to: '/' },
  { label: 'Empleados', to: '/empleados' },
]

const isActive = (to: string) => (to === '/' ? route.path === '/' : route.path.startsWith(to))

const scrolled = ref(false)
onMounted(() => {
  const onScroll = () => (scrolled.value = window.scrollY > 8)
  window.addEventListener('scroll', onScroll)
  onScroll()
  onUnmounted(() => window.removeEventListener('scroll', onScroll))
})
</script>
