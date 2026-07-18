<template>
  <section class="relative overflow-hidden contour-bg">
    <div class="mx-auto max-w-7xl px-4 sm:px-6 pt-8 pb-20 sm:pt-14 sm:pb-28">
      <div class="max-w-2xl animate-fade-in">
        <span class="inline-flex items-center gap-2 text-xs font-medium text-accent-light bg-accent/10 border border-accent/20 rounded-full px-3 py-1.5">
          <Icon name="ph:mountains-fill" class="w-3.5 h-3.5" />
          Dashboard unificado de nómina
        </span>
        <h1 class="mt-5 text-4xl sm:text-5xl font-heading font-extrabold text-white leading-[1.05]">
          La cima de tu gestión de <span class="text-accent">talento humano</span>
        </h1>
        <p class="mt-5 text-base text-silver max-w-lg leading-relaxed">
          Empleados, beneficios y ubicación geográfica en un solo lugar. AtlasHR conecta
          tu backend de nómina con datos externos en tiempo real para que nunca pierdas altura.
        </p>
        <div class="mt-8 flex flex-wrap gap-3">
          <NuxtLink to="/empleados" class="btn-primary text-sm font-semibold px-5 py-3 rounded-xl inline-flex items-center gap-2">
            Ver empleados
            <Icon name="ph:arrow-up-right-bold" class="w-4 h-4" />
          </NuxtLink>
          <a href="#contacto" class="btn-secondary text-sm font-medium px-5 py-3 rounded-xl">
            Solicitar demo
          </a>
        </div>
      </div>
    </div>

    <AtlasRidge class="absolute bottom-0 left-0 w-full h-24 opacity-40" />
  </section>
</template>
