import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { AuthStatus, type User } from '../interfaces'
import { loginAction, registerAction } from '../actions'
import { eventBus } from '~/utils/eventBus'

export const useAuthStore = defineStore('auth', () => {
  const authStatus = ref<AuthStatus>(AuthStatus.UnAuthenticated)
  const user = ref<User | undefined>()
  const token = ref<string>('')

  // --- Helpers ---

  function isTokenExpired(jwtToken: string): boolean {
    try {
      const parts = jwtToken.split('.')
      if (parts.length !== 3) return true
      const payload = JSON.parse(atob(parts[1]))
      return payload.exp * 1000 < Date.now()
    } catch {
      return true
    }
  }

  // --- Actions ---

  const logout = (showMessage: boolean = true): false => {
    authStatus.value = AuthStatus.UnAuthenticated
    user.value = undefined
    token.value = ''

    if (showMessage) {
      eventBus.emit('session-expired')
    }

    navigateTo('/')
    return false
  }

  const login = async (username: string, password: string): Promise<boolean> => {
    authStatus.value = AuthStatus.Checking
    try {
      const loginResp = await loginAction(username, password)

      if (!loginResp.ok) return logout()

      user.value = loginResp.user
      token.value = loginResp.token
      authStatus.value = AuthStatus.Authenticated
      return true
    } catch {
      return logout()
    }
  }

  const register = async (username: string, email: string, password: string): Promise<boolean> => {
    authStatus.value = AuthStatus.Checking
    try {
      const registerResp = await registerAction(username, email, password)

      if (!registerResp.ok) return logout()

      user.value = registerResp.user
      token.value = registerResp.token
      authStatus.value = AuthStatus.Authenticated
      return true
    } catch {
      return logout()
    }
  }

  const checkToken = (): void => {
    if (token.value && isTokenExpired(token.value)) {
      logout()
    }
  }

  // --- Getters ---

  const isChecking = computed(() => authStatus.value === AuthStatus.Checking)
  const isAuthenticated = computed(() => authStatus.value === AuthStatus.Authenticated)
  const isAdmin = computed(
    () =>
      user.value?.rol === 'SUPER' || user.value?.rol === 'ADMIN'
  )

  return {
    authStatus,
    user,
    token,
    isChecking,
    isAuthenticated,
    isAdmin,
    login,
    register,
    logout,
    checkToken,
  }
}, { persist: true })
