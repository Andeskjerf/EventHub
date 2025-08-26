<script setup lang="ts">
import type { ActivityInstance } from '@/models/activity_instance';
import type { CreateParticipant } from '@/models/create_participant';
import LabelValue from '@/components/LabelValue.vue';
import { activityService } from '@/services/activity';
import { activityUtils } from '@/utils/activity_utils';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userModule } from '@/stores/auth/module';

const route = useRoute()
const router = useRouter()

const activity = ref()
const participants = ref([])
const error = ref("")
const loading = ref(true)

const name = ref("");
const phone = ref("");

const options = ref<string[]>([])

onMounted(async () => {
  await getActivityInfo()
  await getParticipantInfo()
  loading.value = false
})

async function getActivityInfo() {
  const response = await activityService.getActivityInfo(route.params["id"])
  if (response.success) {
    activity.value = response.data.activity as ActivityInstance
  } else {
    error.value = response.message
  }
}

async function getParticipantInfo() {
  if (!userModule.state.isAuthenticated) {
    return
  }

  participants.value = await activityService.getParticipants(activity.value.instanceId);
}

function onOptionToggled(e: Event) {
  const id = e.target.id
  if (options.value.some((val) => val == id)) {
    options.value = options.value.filter((val) => val != id)
  } else {
    options.value.push(id)
  }
  console.log(options.value)
}

async function submitHandler(e: Event) {
  e.preventDefault()
  const participant: CreateParticipant = {
    activityInstanceId: activity.value.instanceId,
    name: name.value,
    phoneNumber: phone.value,
    activityOptionIds: options.value
  }

  await activityService.registerParticipant(participant)
  router.push("/")
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
      <div>
        <div v-for="opt in activity.options">
          <input type="checkbox" :id="opt.id" @change="onOptionToggled" />
          <label :for="opt.id">{{ opt.name }}</label>
        </div>
      </div>
      <button :disabled="name.length == 0">Meld meg på</button>
    </form>
    <div v-if="participants.length > 0">
      <h2 id="participantsTitle">Påmeldte</h2>
      <div class="flex space-between" v-for="participant in participants">
        <div>{{ participant.name }}</div>
        <div>{{ participant.phoneNumber }}</div>
      </div>
    </div>
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

#participantsTitle {
  padding-bottom: 12px;
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
