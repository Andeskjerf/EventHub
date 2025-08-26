<script setup lang="ts">

const { value } = defineProps<{
  value: string,
}>()

</script>

<template>
  <div id="activityOption">
    <div @click="$emit('close', value)" id="x">X</div>
    <div id="value">{{ value }}</div>
  </div>
</template>

<style scoped>
#activityOption {
  display: flex;
  align-items: center;
}

#x {
  padding-right: 6px;
  font-size: 18px;
  color: gray;
  user-select: none;
  cursor: pointer;
}

#x:hover {
  color: red;
}
</style>
