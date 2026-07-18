import type { Plugin } from 'vite'
import tailwindcss from '@tailwindcss/vite'

// ─── Config desde .env ────────────────────────────────────────
const API_BASE_URL = process.env.NUXT_API_URL || 'http://localhost:8080'

export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  srcDir: 'app/',

  typescript: {
    strict: true,
  },

  runtimeConfig: {
    apiUrl: API_BASE_URL,
    public: {
      apiUrl: API_BASE_URL,
    }
  },

  routeRules: {
    '/uploads/**': { proxy: { to: `${API_BASE_URL}/uploads/**` } },
    '/': { prerender: true },
  },

  app: {
    pageTransition: {
      name: 'page',
      mode: 'out-in'
    },
    head: {
      link: [
        { rel: 'icon', type: 'image/png', href: '/favicon.png' },
        { rel: 'preconnect', href: 'https://fonts.googleapis.com' },
        { rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: '' },
        { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:wght@400;500;600;700&family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap' },
        { rel: 'stylesheet', href: 'https://cdn.jsdelivr.net/npm/remixicon@4.2.0/fonts/remixicon.css' }
      ]
    }
  },

  modules: ['@pinia/nuxt', '@pinia-plugin-persistedstate/nuxt', '@nuxt/icon'],

  css: ['~/assets/css/main.css'],

  vite: {
    plugins: [tailwindcss() as Plugin],
  },

  components: [
    { path: '~/shared/components', pathPrefix: false },
    { path: '~/components', pathPrefix: false },
    { path: '~/features/dashboard/components', pathPrefix: false },
    { path: '~/features/employees/components', pathPrefix: false },
    { path: '~/features/auth/components', pathPrefix: false },
    { path: '~/shared/icons', pathPrefix: false },
  ],

  imports: {
    dirs: [
      'composables',
      'features/*/composables',
      'features/*/store',
      'features/*/actions',
      'features/*/repositories',
      'shared/composables',
      'shared/utils',
    ]
  }
})