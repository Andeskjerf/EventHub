<script setup lang="ts">
import type { ActivityInstance } from '@/models/activity_instance';
import type { CreateParticipant } from '@/models/create_participant';
import LabelValue from '@/components/LabelValue.vue';
import { activityService } from '@/services/activity';
import { activityUtils } from '@/utils/activity_utils';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userModule } from '@/stores/auth/module';
import type { Participant } from '@/models/participant';

const route = useRoute()
const router = useRouter()

const id = ref()
const activity = ref()
const nextActivity = ref()
const previousActivity = ref()
const participants = ref<Participant[]>([])
const error = ref("")
const loading = ref(true)

const name = ref("");
const phone = ref("");

const options = ref<string[]>([])

onMounted(async () => {
  id.value = route.params["id"]
  await init()
})

async function init() {
  loading.value = true
  error.value = ""
  await getActivityInfo()
  await getPreviousActivityInfo()
  await getNextActivityInfo()
  await getParticipantInfo()
  loading.value = false
}

// TODO: not DRY
async function getActivityInfo() {
  const response = await activityService.getActivityInfo(id.value)
  if (response.success) {
    activity.value = response.data.activity as ActivityInstance
  } else {
    error.value = response.message
  }
}

async function getNextActivityInfo() {
  nextActivity.value = null
  const response = await activityService.getNextActivityInfo(id.value)
  if (response.success) {
    nextActivity.value = response.data.activity as ActivityInstance
  }
}

async function getPreviousActivityInfo() {
  previousActivity.value = null
  const response = await activityService.getPreviousActivityInfo(id.value)
  if (response.success) {
    previousActivity.value = response.data.activity as ActivityInstance
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

async function deleteHandler() {
  await activityService.deleteActivity(activity.value.instanceId)
  router.push("/")
}

function setId(instanceId: string) {
  id.value = instanceId
  router.push(`/activities/${id.value}`)
  init()
}
</script>

<template>
  <div class="loading-state" v-if="loading">
    <div class="loading-text">Laster...</div>
  </div>

  <div v-else class="container">
    <div v-if="userModule.state.isAuthenticated" class="admin-controls">
      <button @click="deleteHandler" class="delete-btn">Slett aktivitet</button>
    </div>

    <div v-if="error.length != 0" class="error-message">
      Feil: {{ error }}
    </div>

    <div class="navigation">
      <button @click="setId(previousActivity.instanceId)" v-if="previousActivity" class="nav-btn">
        ← Forrige
      </button>
      <button @click="setId(nextActivity.instanceId)" v-if="nextActivity" class="nav-btn">
        Neste →
      </button>
    </div>

    <header class="activity-header">
      <h1 class="activity-name">{{ activity.name }}</h1>
      <div class="participant-count">{{ activityUtils.formatParticipationCount(activity) }}</div>
    </header>

    <div class="activity-details">
      <LabelValue label="Tid" :value="activityUtils.formatEventTime(activity.eventDate)" />
      <LabelValue label="Dag" :value="activityUtils.formatEventDate(activity.eventDate)" />
      <LabelValue label="Sted" :value="activity.location" />
      <LabelValue label="Oppmøte" :value="activity.meetLocation" />
    </div>

    <div class="description">
      {{ activity.description }}
    </div>

    <form @submit="submitHandler" class="registration-form">
      <div class="form-group">
        <label for="name">Navn</label>
        <input id="name" v-model="name" type="text" required />
      </div>

      <div class="form-group">
        <label for="phone">Telefon</label>
        <input id="phone" v-model="phone" type="tel" />
      </div>

      <div v-if="activity.options && activity.options.length > 0" class="options-group">
        <label class="options-label">Velg alternativer:</label>
        <div class="options-list">
          <div v-for="opt in activity.options" :key="opt.id" class="option-item">
            <input type="checkbox" :id="opt.id" @change="onOptionToggled" class="option-checkbox" />
            <label :for="opt.id" class="option-label">{{ opt.name }}</label>
          </div>
        </div>
      </div>

      <button type="submit" :disabled="name.length == 0" class="submit-btn">
        Meld meg på
      </button>
    </form>

    <div v-if="participants.length > 0" class="participants-section">
      <div class="participants-header">
        <h3>Påmeldte</h3>
        <span class="total-count">{{ participants.length }} totalt</span>
      </div>

      <div class="participants-list">
        <div v-for="participant in participants" :key="participant.id" class="participant-item">
          <div class="participant-info">
            <span class="participant-name">{{ participant.name }}</span>
            <span v-if="participant.activityOptionNames.length > 0" class="participant-options">
              {{ participant.activityOptionNames.join(', ') }}
            </span>
          </div>
          <span class="participant-phone">{{ participant.phoneNumber }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.loading-text {
  color: #666;
  font-size: 16px;
}

.container {
  max-width: 500px;
  margin: 0 auto;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
  line-height: 1.5;
  color: #333;
}

.admin-controls {
  margin-bottom: 16px;
}

.delete-btn {
  background: #dc3545;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.delete-btn:hover {
  background: #c82333;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 12px 16px;
  border-radius: 4px;
  margin-bottom: 20px;
  border: 1px solid #f5c6cb;
}

.navigation {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 12px;
}

.nav-btn {
  background: #f8f9fa;
  color: #495057;
  border: 1px solid #dee2e6;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.nav-btn:hover {
  background: #e9ecef;
  border-color: #adb5bd;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.activity-name {
  font-size: 28px;
  font-weight: 600;
  margin: 0;
  color: #212529;
}

.participant-count {
  background: #f8f9fa;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  color: #495057;
  font-weight: 500;
}

.activity-details {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 12px 24px;
  margin-bottom: 24px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.description {
  margin-bottom: 32px;
  padding: 20px;
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  color: #495057;
  line-height: 1.6;
}

.registration-form {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 32px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #495057;
}

.form-group input {
  width: 100%;
  padding: 10px 16px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #80bdff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.options-group {
  margin-bottom: 24px;
}

.options-label {
  display: block;
  margin-bottom: 12px;
  font-weight: 500;
  color: #495057;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.option-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.option-label {
  cursor: pointer;
  color: #495057;
}

.submit-btn {
  width: 100%;
  background: #007bff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.submit-btn:hover:not(:disabled) {
  background: #0056b3;
}

.submit-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  opacity: 0.6;
}

.participants-section {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 24px;
}

.participants-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.participants-header h3 {
  margin: 0;
  color: #495057;
  font-size: 18px;
}

.total-count {
  background: #f8f9fa;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 14px;
  color: #495057;
  font-weight: 500;
}

.participants-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.participant-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f8f9fa;
}

.participant-item:last-child {
  border-bottom: none;
}

.participant-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.participant-name {
  font-weight: 500;
  color: #495057;
}

.participant-options {
  font-size: 12px;
  color: #6c757d;
}

.participant-phone {
  color: #495057;
  font-size: 14px;
}

@media (max-width: 600px) {
  .container {
    padding: 16px;
  }

  .activity-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .activity-name {
    font-size: 24px;
  }

  .activity-details {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .participant-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
