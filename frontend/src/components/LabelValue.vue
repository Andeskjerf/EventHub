<script setup lang="ts">

const { label, value } = defineProps<{
  label: string,
  value: string,
}>()

</script>

<template>
  <div id="container">
    <div id="label">{{ label }}</div>
    <div id="value">{{ value }}</div>
  </div>
</template>

<style scoped></style>
