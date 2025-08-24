<script setup lang="ts">
import { type ActivityInstance } from '@/models/activity_instance';
import { activityUtils } from '@/utils/activity_utils';
import { useRouter } from 'vue-router';

const router = useRouter()

const { activity } = defineProps<{
  activity: ActivityInstance,
}>()

function clickHandler() {
  router.push(`/activities/${activity.instanceId}`)
}
</script>

<template>
  <div @click="clickHandler" class="card no-select" id="container">
    <div class="flex space-between" id="header">
      <div id="activityName">{{ activity.name }}</div>
      <div id="participantCount">{{ activityUtils.formatParticipationCount(activity) }}</div>
    </div>
    <div class="flex space-between" id="body">
      <div>{{ activityUtils.formatEventTime(activity.eventDate) }}</div>
      <div>{{ activityUtils.formatEventDate(activity.eventDate) }}</div>
    </div>
  </div>
</template>

<style scoped>
#container {
  background-color: wheat;
  border-radius: 12px;

  width: 300px;
  min-width: 200px;
  margin: 10px;
  padding: 10px;
}

#header {
  align-items: center;

  padding-bottom: 8px;
}

#activityName {
  font-size: 24px;
}

.flex {
  display: flex;
}

.space-between {
  justify-content: space-between;
}

.no-select {
  user-select: none;
}

.card {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);

  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.card:active {
  transform: translateY(0);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}
</style>
