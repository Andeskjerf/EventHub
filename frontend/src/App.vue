<script setup lang="ts">
import { computed } from 'vue';
import { logout } from './services/authentication';
import { userModule } from './stores/auth/module';

const authed = computed(() => userModule.state.isAuthenticated);

</script>

<template>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav id="navigation">
    <RouterLink to="/">Go to Home</RouterLink>
    <RouterLink v-if="!authed" to="/auth">Go to Auth</RouterLink>
    <RouterLink v-if="authed" to="/create">Create Activity</RouterLink>
    <a v-if="authed" @click="logout" href="#">Logout</a>
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
