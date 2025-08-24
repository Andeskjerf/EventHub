<script setup lang="ts">
import { type ActivityInstance } from '@/models/activity_instance';

const { activity } = defineProps<{
  activity: ActivityInstance,
}>()

function formatParticipationCount(): string {
  if (activity.maxParticipants == 0) {
    return `${activity.participants} deltakere`
  }
  return `${activity.participants} / ${activity.maxParticipants} deltakere`
}

function formatEventTime(eventDate: string): string {
  const date = new Date(eventDate)
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

function formatEventDate(eventDate: string): string {
  const date = new Date(eventDate)
  const weekday = date.toLocaleDateString('nb-NO', { weekday: 'long' })
  const month = date.toLocaleDateString('nb-NO', { month: 'long' })
  return `${weekday}, ${date.getDate()} ${month}`
}
</script>

<template>
  <div class="card no-select" id="container">
    <div class="flex space-between" id="header">
      <div id="activityName">{{ activity.name }}</div>
      <div id="participantCount">{{ formatParticipationCount() }}</div>
    </div>
    <div class="flex space-between" id="body">
      <div>{{ formatEventTime(activity.eventDate) }}</div>
      <div>{{ formatEventDate(activity.eventDate) }}</div>
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
  /* Subtle everyday shadow */
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);

  /* Smooth transitions */
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.card:hover {
  /* Lift it up with a bigger shadow */
  transform: translateY(-2px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.card:active {
  /* Press down effect */
  transform: translateY(0);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}
</style>
