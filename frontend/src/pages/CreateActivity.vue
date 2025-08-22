<script setup lang="ts">
import type { CreateActivityRequest } from '@/models/create_activity_request';
import { activityService } from '@/services/activity';
import { ref } from 'vue';

const name = ref("Golf")
const eventDate = ref("2025-08-29T09:00:00")
const registerBefore = ref(24)
const location = ref("Golfklubb")
const meetupLocation = ref("Golfklubb parkering")
const description = ref("Hyggelig golf treff med mulighet for spill på bane og range")
const maxParticipants = ref(100)
const repeatInterval = ref(7)

async function submitActivity(e: Event) {
  e.preventDefault()
  const activity: CreateActivityRequest = {
    name: name.value,
    eventDate: `${eventDate.value}+02:00`,
    registerBefore: registerBefore.value,
    location: location.value,
    meetupLocation: meetupLocation.value,
    description: description.value,
    maxParticipants: maxParticipants.value,
    repeatInterval: repeatInterval.value,
  }

  await activityService.createActivity(activity)
}
</script>

<template>
  <h1>Create activity</h1>
  <form @submit="submitActivity">
    <input v-model="name" placeholder="name" />
    <input v-model="eventDate" placeholder="dato" type="datetime-local" />
    <input v-model="registerBefore" placeholder="registration deadline, hours before date" />
    <input v-model="location" placeholder="where the activity takes place" />
    <input v-model="meetupLocation" placeholder="where participants should meet" />
    <input v-model="maxParticipants" placeholder="the max allowed participants" />
    <input v-model="repeatInterval" placeholder="when the activity should repeat, in days" />
    <textarea rows="5" v-model="description" placeholder="a description for the activity" />
    <button>Create</button>
  </form>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
}
</style>
