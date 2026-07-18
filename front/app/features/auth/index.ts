// Components
export { default as AuthLoginModal } from './components/ModalLogin.vue'
export { default as AuthRegisterModal } from './components/ModalRegister.vue'

// Store
export { useAuthStore } from './store/useAuthStore'
export { useAuthModalStore } from './store/authModal'

// Interfaces
export { AuthStatus } from './interfaces'
export type { User, Authority, AuthResponse } from './interfaces'

// Actions
export { loginAction, checkAuthAction } from './actions'
