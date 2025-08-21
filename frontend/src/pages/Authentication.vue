<script setup lang="ts">
import { login, register } from '@/services/authentication'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const showLogin = ref(true)

const email = ref("test@gmail.com")
const username = ref("test")
const password = ref("Password1")

async function handleLogin(event: Event) {
  event.preventDefault()
  const result = await login(username.value, password.value)
  if (result != undefined) {
    router.back()
  }
}

async function handleRegister(event: Event) {
  event.preventDefault()
  const result = await register(email.value, username.value, password.value)
  if (result != undefined) {
    router.back()
  }
}
</script>

<template>
  <h1>Authentication</h1>
  <button @click="showLogin = !showLogin">{{ showLogin ? "Show register" : "Show login" }}</button>
  <form @submit="handleLogin" v-if="showLogin" id="loginForm">
    <input v-model="username" id="usernameLogin" placeholder="username" />
    <input v-model="password" id="passwordLogin" placeholder="password" />
    <button>Submit</button>
  </form>
  <form @submit="handleRegister" v-else id="registerForm">
    <input v-model="email" id="email" placeholder="email" />
    <input v-model="username" id="usernameRegister" placeholder="username" />
    <input v-model="password" id="passwordRegister" placeholder="password" />
    <button type="submit">Submit</button>
  </form>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
}
</style>
