<script setup lang="ts">
import ActivityOption from '@/components/ActivityOption.vue';
import type { CreateActivityRequest } from '@/models/create_activity_request';
import { activityService } from '@/services/activity';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const name = ref("Golf")
const eventDate = ref("2025-08-29T09:00:00")
const registerBefore = ref(24)
const location = ref("Golfklubb")
const meetLocation = ref("Golfklubb parkering")
const description = ref("Hyggelig golf treff med mulighet for spill på bane og range")
const maxParticipants = ref(100)
const repeatInterval = ref(7)

const extraOptionValue = ref<string>("")

const activityOptions = ref<string[]>([])

const error = ref("")

async function submitActivity(e: Event) {
  e.preventDefault()
  const activity: CreateActivityRequest = {
    name: name.value,
    eventDate: `${eventDate.value}+02:00`, // FIXME: hardcoded timezone
    registerBefore: registerBefore.value,
    location: location.value,
    meetLocation: meetLocation.value,
    description: description.value,
    maxParticipants: maxParticipants.value,
    repeatInterval: repeatInterval.value,
  }

  const result = await activityService.createActivity(activity)
  if (result.success) {
    router.replace("/")
  } else {
    error.value = result.message
  }
}

function addActivityOption() {
  if (extraOptionValue.value.length > 0) {
    activityOptions.value.push(extraOptionValue.value)
    extraOptionValue.value = ""
  }
}

function removeOption(option: string) {
  activityOptions.value = activityOptions.value.filter((val) => val != option)
}
</script>

<template>
  <h1>Create activity</h1>
  <div id="container">

    <h3 v-if="error.length > 0">ERROR: {{ error }}</h3>
    <div class="label">Aktivitet navn</div>
    <input v-model="name" placeholder="name" />
    <div class="label">Tid og dato</div>
    <input v-model="eventDate" placeholder="dato" type="datetime-local" />
    <div class="label">Frist, timer før satt dato</div>
    <input v-model="registerBefore" placeholder="registration deadline, hours before date" />
    <div class="label">Sted</div>
    <input v-model="location" placeholder="where the activity takes place" />
    <div class="label">Oppmøte</div>
    <input v-model="meetLocation" placeholder="where participants should meet" />
    <div class="label">Maks antall deltakere. 0 eller tomt for ubegrenset</div>
    <input v-model="maxParticipants" placeholder="the max allowed participants" />
    <div class="label">Repeter aktivitet hver nth dag. F.eks, en verdi på 7 repeteres hver 7 dag</div>
    <input v-model="repeatInterval" placeholder="when the activity should repeat, in days" />
    <div class="label">Beskrivelse</div>
    <textarea rows="5" v-model="description" placeholder="a description for the activity" />
    <div class="flex">
      <div>
        <div class="label">Ekstra valg</div>
        <input v-model="extraOptionValue" @keyup.enter="addActivityOption" placeholder="name of extra option" />
      </div>
      <div>
        <ActivityOption @close="removeOption" v-for="option in activityOptions" :value="option" />
      </div>
    </div>
    <button @click="submitActivity">Skap aktivitet</button>
  </div>
</template>

<style scoped>
#container {
  display: flex;
  flex-direction: column;
}

.label {
  color: gray;
  font-size: 12px;

  padding-top: 10px;
  padding-bottom: 3px;
}

.flex {
  display: flex;
  justify-content: space-between;
}
</style>
