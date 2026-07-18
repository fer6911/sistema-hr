<script setup>
const year = new Date().getFullYear()

const columns = [
  {
    title: 'Producto',
    items: ['Dashboard', 'Empleados', 'Beneficios'],
  },
  {
    title: 'Empresa',
    items: ['Sobre AtlasHR', 'Prueba técnica', 'Symplifica'],
  },
  {
    title: 'Legal',
    items: ['Términos de servicio', 'Política de privacidad', 'Tratamiento de datos'],
  },
]
</script>

<template>
  <footer class="foot">
    <div class="foot__inner">
      <!-- Top -->
      <div class="foot__grid">
        <!-- Brand -->
        <div class="foot__brand">
          <div class="foot__logo-row">
            <img src="/images/logo.png" alt="AtlasHR" class="h-14 w-auto object-contain" />
            <span class="font-heading font-extrabold text-2xl tracking-tight text-white">
              Atlas<span class="text-accent">HR</span>
            </span>
          </div>
          <p class="foot__desc">
            Dashboard unificado de empleados, beneficios y nómina — con enriquecimiento
            de ubicación en tiempo real.
          </p>
        </div>

        <!-- Columns -->
        <div class="foot__columns">
          <div v-for="col in columns" :key="col.title" class="foot__col">
            <h4 class="foot__col-title">{{ col.title }}</h4>
            <ul class="foot__col-list">
              <li v-for="item in col.items" :key="item">
                <span class="foot__col-link">{{ item }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <!-- Divider -->
      <div class="foot__divider" />

      <!-- Bottom -->
      <div class="foot__bottom">
        <p>&copy; {{ year }} AtlasHR. Todos los derechos reservados.</p>
        <p class="foot__credit">
          Prueba técnica para <strong>Symplifica</strong>
        </p>
      </div>
    </div>
  </footer>
</template>

<style src="~/assets/css/components/layout/footer.css"></style>
