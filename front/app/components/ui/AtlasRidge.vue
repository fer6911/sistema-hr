<script setup lang="ts">
defineOptions({ name: 'AtlasRidge', inheritAttrs: true })
</script>

<template>
  <svg
    viewBox="0 0 1200 200"
    preserveAspectRatio="none"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
  >
    <path
      d="M0 200 L120 90 L200 150 L340 40 L430 130 L560 20 L680 140 L800 70 L900 160 L1020 60 L1120 150 L1200 100 L1200 200 Z"
      fill="url(#ridgeFill)"
      opacity="0.35"
    />
    <path
      d="M0 200 L120 90 L200 150 L340 40 L430 130 L560 20 L680 140 L800 70 L900 160 L1020 60 L1120 150 L1200 100"
      fill="none"
      stroke="url(#ridgeStroke)"
      stroke-width="2"
    />
    <defs>
      <linearGradient id="ridgeFill" x1="0" y1="0" x2="1200" y2="0" gradientUnits="userSpaceOnUse">
        <stop offset="0%" stop-color="#0F4C81" />
        <stop offset="100%" stop-color="#16C6C9" />
      </linearGradient>
      <linearGradient id="ridgeStroke" x1="0" y1="0" x2="1200" y2="0" gradientUnits="userSpaceOnUse">
        <stop offset="0%" stop-color="#1B6CA8" />
        <stop offset="100%" stop-color="#52E3E5" />
      </linearGradient>
    </defs>
  </svg>
</template>
