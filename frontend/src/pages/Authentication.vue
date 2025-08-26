<script setup lang="ts">
import { login, register } from '@/services/authentication'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const showLogin = ref(true)
const email = ref("")
const username = ref("")
const password = ref("")
const loading = ref(false)
const error = ref("")

async function handleLogin(event: Event) {
  event.preventDefault()
  loading.value = true
  error.value = ""

  try {
    const result = await login(username.value, password.value)
    if (result != undefined) {
      router.back()
    }
  } catch (err) {
    error.value = "Innlogging feilet. Sjekk brukernavn og passord."
  } finally {
    loading.value = false
  }
}

async function handleRegister(event: Event) {
  event.preventDefault()
  loading.value = true
  error.value = ""

  try {
    const result = await register(email.value, username.value, password.value)
    if (result != undefined) {
      router.back()
    }
  } catch (err) {
    console.log(err)
    error.value = `Registrering feilet. Prøv igjen.`
  } finally {
    loading.value = false
  }
}

function toggleForm() {
  showLogin.value = !showLogin.value
  error.value = ""
  // Clear form when switching
  email.value = ""
  username.value = ""
  password.value = ""
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h1 class="auth-title">{{ showLogin ? 'Logg inn' : 'Registrer deg' }}</h1>
        <p class="auth-subtitle">
          {{ showLogin ? '' : 'Opprett en ny konto' }}
        </p>
      </div>

      <div v-if="error" class="error-message">
        {{ error }}
      </div>

      <form @submit="handleLogin" v-if="showLogin" class="auth-form">
        <div class="form-group">
          <label for="login-username">Brukernavn</label>
          <input id="login-username" v-model="username" type="text" placeholder="Skriv inn brukernavn" required
            :disabled="loading" />
        </div>

        <div class="form-group">
          <label for="login-password">Passord</label>
          <input id="login-password" v-model="password" type="password" placeholder="Skriv inn passord" required
            :disabled="loading" />
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? 'Logger inn...' : 'Logg inn' }}
        </button>
      </form>

      <form @submit="handleRegister" v-else class="auth-form">
        <div class="form-group">
          <label for="register-email">E-post</label>
          <input id="register-email" v-model="email" type="email" placeholder="din@epost.no" required
            :disabled="loading" />
        </div>

        <div class="form-group">
          <label for="register-username">Brukernavn</label>
          <input id="register-username" v-model="username" type="text" placeholder="Velg et brukernavn" required
            :disabled="loading" />
        </div>

        <div class="form-group">
          <label for="register-password">Passord</label>
          <input id="register-password" v-model="password" type="password" placeholder="Lag et sikkert passord" required
            :disabled="loading" />
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? 'Registrerer...' : 'Registrer deg' }}
        </button>
      </form>

      <div class="auth-toggle">
        <p>
          {{ showLogin ? 'Har du ikke en konto?' : 'Har du allerede en konto?' }}
          <button @click="toggleForm" class="toggle-btn" :disabled="loading">
            {{ showLogin ? 'Registrer deg' : 'Logg inn' }}
          </button>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 24px;
}

.auth-card {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 32px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-title {
  font-size: 28px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 8px 0;
}

.auth-subtitle {
  color: #6c757d;
  margin: 0;
  font-size: 14px;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 12px 16px;
  border-radius: 4px;
  margin-bottom: 24px;
  border: 1px solid #f5c6cb;
  font-size: 14px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-weight: 500;
  color: #495057;
  font-size: 14px;
}

.form-group input {
  padding: 12px 16px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.2s, box-shadow 0.2s;
  background: #fff;
}

.form-group input:focus {
  outline: none;
  border-color: #80bdff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.form-group input:disabled {
  background: #f8f9fa;
  color: #6c757d;
  cursor: not-allowed;
}

.form-group input::placeholder {
  color: #adb5bd;
}

.submit-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  background: #0056b3;
}

.submit-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  opacity: 0.6;
}

.auth-toggle {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #f8f9fa;
}

.auth-toggle p {
  margin: 0;
  color: #6c757d;
  font-size: 14px;
}

.toggle-btn {
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
  font-weight: 500;
  text-decoration: underline;
  font-size: 14px;
  margin-left: 4px;
  transition: color 0.2s;
}

.toggle-btn:hover:not(:disabled) {
  color: #0056b3;
}

.toggle-btn:disabled {
  color: #6c757d;
  cursor: not-allowed;
  text-decoration: none;
}

@media (max-width: 480px) {
  .auth-container {
    padding: 16px;
  }

  .auth-card {
    padding: 24px 20px;
  }

  .auth-title {
    font-size: 24px;
  }

  .form-group input,
  .submit-btn {
    font-size: 16px;
  }
}
</style>
