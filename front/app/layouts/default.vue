<template>
  <div class="min-h-screen flex flex-col bg-background">
    <Navbar />
    <main class="flex-1 pt-28">
      <slot />
    </main>
    <Footer />
  </div>
</template>
