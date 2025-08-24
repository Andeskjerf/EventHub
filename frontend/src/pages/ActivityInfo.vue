<script setup lang="ts">
import type { ActivityInstance } from '@/models/activity_instance';
import { activityService } from '@/services/activity';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const activity = ref()
const error = ref("")
const loading = ref(true)

onMounted(async () => {
  const response = await activityService.getActivityInfo(route.params["id"])
  if (response.success) {
    activity.value = response.data.activity as ActivityInstance
  } else {
    error.value = response.message
  }
  loading.value = false
})
</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-else id="container">
    <h1 v-if="error.length != 0">ERROR: {{ error }}</h1>
    <h1>{{ activity.name }}</h1>
  </div>
</template>

<style scoped></style>
