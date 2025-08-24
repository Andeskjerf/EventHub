<script setup lang="ts">
import { activityService } from '@/services/activity';
import { onMounted, ref } from 'vue';

const loading = ref(true);
const activities = ref();

onMounted(async () => {
  activities.value = await activityService.getNextInstanceOfAllActiveActivities()
  console.log(activities.value)
  loading.value = false
})

</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-else v-for="activity in activities">
    <h1>{{ activity.name }}</h1>
  </div>
</template>

<style scoped></style>
