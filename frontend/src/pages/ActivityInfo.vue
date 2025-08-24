<script setup lang="ts">
import type { ActivityInstance } from '@/models/activity_instance';
import type { CreateParticipant } from '@/models/create_participant';
import LabelValue from '@/components/LabelValue.vue';
import { activityService } from '@/services/activity';
import { activityUtils } from '@/utils/activity_utils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const activity = ref()
const error = ref("")
const loading = ref(true)

const name = ref("");
const phone = ref("");

onMounted(async () => {
  const response = await activityService.getActivityInfo(route.params["id"])
  if (response.success) {
    activity.value = response.data.activity as ActivityInstance
  } else {
    error.value = response.message
  }
  loading.value = false
})

function submitHandler(e: Event) {
  e.preventDefault()
  const participant: CreateParticipant = {
    activityId: activity.value.instanceId,
    name: name.value,
    phoneNumber: phone.value,
  }

  activityService.registerParticipant(participant)
}
</script>

<template>
  <h1 v-if="loading"></h1>
  <div v-else id="container">
    <h1 v-if="error.length != 0">ERROR: {{ error }}</h1>
    <div id="header" class="flex space-between h-center">
      <div id="activityName">{{ activity.name }}</div>
      <div id="participantCount">{{ activityUtils.formatParticipationCount(activity) }}</div>
    </div>
    <div id="activityValues">
      <LabelValue label="Tid" :value="activityUtils.formatEventTime(activity.eventDate)" />
      <LabelValue label="Dag" :value="activityUtils.formatEventDate(activity.eventDate)" />
      <LabelValue label="Sted" :value="activity.location" />
      <LabelValue label="Oppmøte" :value="activity.meetLocation" />
    </div>
    <div id="description">
      {{ activity.description }}
    </div>
    <form @submit="submitHandler" id="inputs">
      <div>
        <label>Navn</label>
        <input v-model="name"></input>
      </div>
      <div>
        <label>Telefon</label>
        <input v-model="phone"></input>
      </div>
      <button :disabled="name.length == 0">Meld meg på</button>
    </form>
  </div>
</template>

<style scoped>
#container {
  width: 400px;
  margin: 12px;
}

#header {
  padding-bottom: 12px;
}

#activityName {
  font-size: 24px;
}

#body {
  padding-top: 6px;
}

#activityValues {
  display: grid;
  grid-template-columns: auto max-content;

  padding-bottom: 12px;
}

#activityValues>div {
  padding-bottom: 8px;
}

#inputs {
  padding: 20px 0px 0px 0px;
}

#inputs>div {
  display: grid;
}

.h-center {
  align-items: center;
}

.flex {
  display: flex;
}

.space-between {
  justify-content: space-between;
}
</style>
