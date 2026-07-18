import { defineStore } from 'pinia'

export const useAuthModalStore = defineStore('authModal', {
  state: () => ({
    loginOpen: false,
    registerOpen: false,
  }),

  actions: {
    openLogin() {
      this.registerOpen = false
      this.loginOpen = true
    },

    closeLogin() {
      this.loginOpen = false
    },

    openRegister() {
      this.loginOpen = false
      this.registerOpen = true
    },

    closeRegister() {
      this.registerOpen = false
    },

    closeAll() {
      this.loginOpen = false
      this.registerOpen = false
    },
  },
})
