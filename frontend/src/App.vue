<script setup lang="ts"></script>

<template>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav id="navigation">
    <RouterLink to="/">Go to Home</RouterLink>
    <RouterLink to="/auth">Go to Auth</RouterLink>
  </nav>
  <main>
    <RouterView />
  </main>
</template>

<style scoped>
#navigation {
  display: flex;
  flex-direction: column;
}
</style>
