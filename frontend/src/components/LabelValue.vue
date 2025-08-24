<script setup lang="ts">

const { label, value } = defineProps<{
  label: string,
  value: string,
}>()

</script>

<template>
  <div>
    <div id="label">{{ label }}</div>
    <div id="value">{{ value }}</div>
  </div>
</template>

<style scoped>
#label {
  color: gray;
  font-size: 12px;
}
</style>
