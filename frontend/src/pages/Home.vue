<script setup lang="ts">
import { activityService } from '@/services/activity';

activityService.getNextInstanceOfAllActiveActivities()
</script>

<template>
  <h1>i am the home page</h1>
</template>

<style scoped></style>
