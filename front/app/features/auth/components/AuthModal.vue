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
      <div
        v-if="visible"
        class="fixed inset-0 z-[100] flex items-center justify-center p-4"
        @click.self="handleClose"
      >
        <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" />

        <Transition
          enter-active-class="transition duration-200 ease-out"
          enter-from-class="opacity-0 scale-95 translate-y-4"
          enter-to-class="opacity-100 scale-100 translate-y-0"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100 scale-100 translate-y-0"
          leave-to-class="opacity-0 scale-95 translate-y-4"
        >
          <div
            v-if="visible"
            class="relative w-full max-w-md bg-surface border border-white/5 rounded-2xl shadow-2xl overflow-hidden"
          >
            <button
              class="absolute top-4 right-4 p-1.5 rounded-lg text-gray hover:text-white hover:bg-white/5 transition-colors z-10"
              @click="handleClose"
            >
              <Icon name="ph:x-bold" class="w-5 h-5" />
            </button>

            <ModalLogin
              v-if="view === 'login'"
              @switch-view="switchView"
              @success="handleClose"
            />
            <ModalRegister
              v-else
              @switch-view="switchView"
              @success="handleClose"
            />
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
defineProps<{ visible: boolean }>()

const authModal = useAuthModalStore()

const view = ref<'login' | 'register'>('login')

const switchView = (newView: 'login' | 'register') => {
  view.value = newView
  if (newView === 'login') {
    authModal.openLogin()
  } else {
    authModal.openRegister()
  }
}

const handleClose = () => {
  view.value = 'login'
  authModal.closeAll()
}
</script>
