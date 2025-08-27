<script setup lang="ts">
import { activityService } from '@/services/activity';
import ActivityCard from '@/components/ActivityCard.vue';
import { onMounted, ref } from 'vue';
import type { ActivityInstance } from '@/models/activity_instance';

const loading = ref(true);
const activities = ref<ActivityInstance[]>([]);

onMounted(async () => {
  activities.value = await activityService.getNextInstanceOfAllActiveActivities()
  activities.value.sort((a, b) => a.eventDate.localeCompare(b.eventDate))
  loading.value = false
})

</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-if="activities.length == 0" class="background text-align-center">
    Ingen aktiviteter er lagt til
  </div>
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

.background {
  padding: 20px;
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  color: #495057;
  line-height: 1.6;
  width: fit-content;
  margin: auto auto;
}

.text-align-center {
  text-align: center;
}
</style>
