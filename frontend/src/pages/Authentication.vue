<script setup lang="ts">
import { register } from '@/services/authentication'
import { ref } from 'vue'

const showLogin = ref(true)

const email = ref("")
const username = ref("")
const password = ref("")

document.getElementById("loginForm")?.addEventListener("submit", function (e) {
})

function handleLogin(event: Event) {
  event.preventDefault()
}

function handleRegister(event: Event) {
  event.preventDefault()
  register(email.value, username.value, password.value)
}
</script>

<template>
  <h1>Authentication</h1>
  <button @click="showLogin = !showLogin">{{ showLogin ? "Show register" : "Show login" }}</button>
  <form v-if="showLogin" id="loginForm">
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
