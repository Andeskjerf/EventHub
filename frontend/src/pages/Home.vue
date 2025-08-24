<script setup lang="ts">
import { activityService } from '@/services/activity';
import ActivityCard from '@/components/ActivityCard.vue';
import { onMounted, ref } from 'vue';

const loading = ref(true);
const activities = ref();

onMounted(async () => {
  activities.value = await activityService.getNextInstanceOfAllActiveActivities()
  loading.value = false
})

</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-else id="activityContainer">
    <ActivityCard v-for="activity in activities" :activity="activity" />
  </div>
</template>

<style scoped>
#activityContainer {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
</style>
