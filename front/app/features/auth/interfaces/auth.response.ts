import type { User } from './user.interface'

export interface AuthResponse {
  error: boolean
  message: string
  data: User
  errors: string[]
}
