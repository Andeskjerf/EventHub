<script setup lang="ts">
import { activityService } from '@/services/activity';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const activity = ref()

onMounted(async () => {
  activity.value = activityService.getActivityInfo()
})
</script>

<template>
  <div id="container">
    <h1>hello, {{ route.path }}</h1>
  </div>
</template>

<style scoped></style>
