<script setup lang="ts">
import { ref } from 'vue'

const showLogin = ref(true)

document.getElementById("loginForm")?.addEventListener("submit", function (e) {
  const username = document.getElementById("username").value
  const password = document.getElementById("password").value
})
</script>

<template>
  <h1>Authentication</h1>
  <button @click="showLogin = !showLogin">{{ showLogin ? "Show register" : "Show login" }}</button>
  <form v-if="showLogin" id="loginForm">
    <input id="usernameLogin" placeholder="username" />
    <input id="passwordLogin" placeholder="password" />
    <button>Submit</button>
  </form>
  <form v-else id="registerForm">
    <input id="email" placeholder="email" />
    <input id="usernameRegister" placeholder="username" />
    <input id="passwordRegister" placeholder="password" />
    <button>Submit</button>
  </form>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
}
</style>
