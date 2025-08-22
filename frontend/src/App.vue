<script setup lang="ts">
import { logout } from './services/authentication';
import { userModule } from './stores/auth/module';

</script>

<template>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav id="navigation">
    <RouterLink to="/">Go to Home</RouterLink>
    <RouterLink v-if="!userModule.state.isAuthenticated" to="/auth">Go to Auth</RouterLink>
    <a v-else="userModule.state.isAuthenticated" @click="logout" href="#">Logout</a>
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

a {
  cursor: pointer;
}
</style>
