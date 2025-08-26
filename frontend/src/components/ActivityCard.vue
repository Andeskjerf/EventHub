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
  <div @click="clickHandler" class="activity-card">
    <div class="card-header">
      <h3 class="activity-name">{{ activity.name }}</h3>
      <div class="participant-count">{{ activityUtils.formatParticipationCount(activity) }}</div>
    </div>
    <div class="card-body">
      <div class="time-info">{{ activityUtils.formatEventTime(activity.eventDate) }}</div>
      <div class="date-info">{{ activityUtils.formatEventDate(activity.eventDate) }}</div>
    </div>
  </div>
</template>

<style scoped>
.activity-card {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 20px;
  margin: 12px;
  width: 320px;
  min-width: 280px;
  cursor: pointer;
  user-select: none;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.activity-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-color: #dee2e6;
}

.activity-card:active {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f8f9fa;
}

.activity-name {
  font-size: 20px;
  font-weight: 600;
  color: #212529;
  margin: 0;
  line-height: 1.3;
  flex: 1;
  margin-right: 12px;
}

.participant-count {
  background: #f8f9fa;
  color: #495057;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time-info {
  font-size: 16px;
  color: #495057;
  font-weight: 500;
}

.date-info {
  font-size: 14px;
  color: #6c757d;
}

@media (max-width: 600px) {
  .activity-card {
    width: 100%;
    margin: 8px 0;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .activity-name {
    margin-right: 0;
    font-size: 18px;
  }

  .card-body {
    flex-direction: column;
    align-items: flex-start;
    gap: 6px;
  }
}
</style>
