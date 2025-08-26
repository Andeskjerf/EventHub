<script setup lang="ts">
import { computed } from 'vue';
import { logout } from './services/authentication';
import { userModule } from './stores/auth/module';

const authed = computed(() => userModule.state.isAuthenticated);
</script>

<template>
  <div class="app">
    <header class="app-header">
      <div class="header-content">
        <RouterLink to="/" class="logo">
          <h1>Aktiviteter</h1>
        </RouterLink>

        <nav class="main-nav">
          <RouterLink to="/" class="nav-item">
            <span class="nav-text">Hjem</span>
          </RouterLink>

          <RouterLink v-if="!authed" to="/auth" class="nav-item">
            <span class="nav-text">Logg inn</span>
          </RouterLink>

          <template v-if="authed">
            <RouterLink to="/create" class="nav-item primary">
              <span class="nav-text">+ Ny aktivitet</span>
            </RouterLink>

            <button @click="logout" class="nav-item logout">
              <span class="nav-text">Logg ut</span>
            </button>
          </template>
        </nav>
      </div>
    </header>

    <main class="app-main">
      <div class="main-content">
        <RouterView />
      </div>
    </main>
  </div>
</template>

<style scoped>
*,
*::before,
*::after {
  box-sizing: border-box;
}

.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
  background: #fafbfc;
}

.app-header {
  background: white;
  border-bottom: 2px solid #f1f3f4;
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  width: 100%;
}

.logo {
  text-decoration: none;
  color: #1a73e8;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
  letter-spacing: -0.5px;
}

.main-nav {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item {
  display: inline-flex;
  align-items: center;
  padding: 10px 16px;
  border-radius: 24px;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
  border: none;
  background: none;
  cursor: pointer;
  color: #5f6368;
  position: relative;
  white-space: nowrap;
}

.nav-item:hover {
  background: #f8f9fa;
  color: #202124;
}

.nav-item.router-link-active {
  background: #e8f0fe;
  color: #1a73e8;
  font-weight: 600;
}

.nav-item.primary {
  background: #1a73e8;
  color: white;
  font-weight: 600;
}

.nav-item.primary:hover {
  background: #1557b0;
  color: white;
}

.nav-item.logout {
  color: #d93025;
}

.nav-item.logout:hover {
  background: #fce8e6;
  color: #d93025;
}

.nav-text {
  position: relative;
}

.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-top: 50px;
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 32px 24px;
}

.debug-info {
  position: fixed;
  top: 80px;
  right: 16px;
  background: #202124;
  color: #e8eaed;
  padding: 8px 12px;
  border-radius: 6px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  opacity: 0.8;
  pointer-events: none;
  z-index: 999;
}

@media (max-width: 768px) {
  .header-content {
    padding: 12px 16px;
  }

  .logo h1 {
    font-size: 20px;
  }

  .main-nav {
    gap: 4px;
  }

  .nav-item {
    padding: 8px 12px;
    font-size: 13px;
  }

  .main-content {
    padding: 20px 16px;
  }

  .debug-info {
    top: 70px;
    right: 12px;
    font-size: 10px;
  }
}

@media (max-width: 480px) {
  .header-content {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .main-nav {
    justify-content: center;
    flex-wrap: wrap;
  }

  .nav-item {
    flex: 1;
    justify-content: center;
    min-width: 0;
  }

  .nav-text {
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>
