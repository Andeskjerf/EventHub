<script setup lang="ts">
import { activityService } from '@/services/activity';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const activity = ref()
const loading = ref(true)

onMounted(async () => {
  activity.value = activityService.getActivityInfo(route.params["id"])
  loading.value = false
})
</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-else id="container">
    <h1>hello, {{ route.path }}</h1>
  </div>
</template>

<style scoped></style>
