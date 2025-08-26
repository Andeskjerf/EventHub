<script setup lang="ts">
import ActivityOption from '@/components/ActivityOption.vue';
import type { CreateActivityRequest } from '@/models/create_activity_request';
import { activityService } from '@/services/activity';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const name = ref("")
const eventDate = ref("")
const registerBefore = ref(24)
const location = ref("")
const meetLocation = ref("")
const description = ref("")
const maxParticipants = ref(0)
const repeatInterval = ref(7)
const extraOptionValue = ref<string>("")
const activityOptions = ref<string[]>([])
const error = ref("")
const fieldErrors = ref({})
const loading = ref(false)

async function submitActivity(e: Event) {
  e.preventDefault()
  loading.value = true
  error.value = ""
  fieldErrors.value = {}
  
  try {
    const activity: CreateActivityRequest = {
      name: name.value,
      eventDate: `${eventDate.value}+02:00`, // FIXME: hardcoded timezone
      registerBefore: registerBefore.value,
      location: location.value,
      meetLocation: meetLocation.value,
      description: description.value,
      maxParticipants: maxParticipants.value,
      repeatInterval: repeatInterval.value,
      options: activityOptions.value,
    }
    
    const result = await activityService.createActivity(activity)
    if (result.success) {
      router.replace("/")
    } else {
      if (result.errors) {
        fieldErrors.value = result.errors
      } else {
        error.value = result.message
      }
    }
  } catch (err) {
    error.value = "Det oppstod en feil ved opprettelse av aktivitet"
  } finally {
    loading.value = false
  }
}

function addActivityOption() {
  if (extraOptionValue.value.trim().length > 0) {
    if (!activityOptions.value.includes(extraOptionValue.value.trim())) {
      activityOptions.value.push(extraOptionValue.value.trim())
      extraOptionValue.value = ""
    }
  }
}

function removeOption(option: string) {
  activityOptions.value = activityOptions.value.filter((val) => val !== option)
}

function handleKeyDown(event: KeyboardEvent) {
  if (event.key === 'Enter') {
    event.preventDefault()
    addActivityOption()
  }
}
</script>

<template>
  <div class="create-activity-container">
    <div class="create-activity-card">
      <div class="header">
        <h1 class="page-title">Opprett ny aktivitet</h1>
        <p class="page-subtitle">Fyll ut informasjonen nedenfor for å opprette en aktivitet</p>
      </div>

      <div v-if="error" class="error-message">
        {{ error }}
      </div>

      <div v-if="Object.keys(fieldErrors).length > 0" class="field-errors">
        <div v-for="(message, field) in fieldErrors" :key="field" class="field-error">
          <strong>{{ field }}:</strong> {{ message }}
        </div>
      </div>

      <form @submit="submitActivity" class="activity-form">
        <div class="form-section">
          <h3 class="section-title">Grunnleggende informasjon</h3>
          
          <div class="form-group">
            <label for="name">Aktivitetsnavn</label>
            <input 
              id="name"
              v-model="name" 
              type="text"
              placeholder="F.eks. Golf, Fotball, Bowling"
              required
              :disabled="loading"
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="eventDate">Dato og tidspunkt</label>
              <input 
                id="eventDate"
                v-model="eventDate" 
                type="datetime-local"
                required
                :disabled="loading"
              />
            </div>

            <div class="form-group">
              <label for="registerBefore">Påmeldingsfrist (timer før)</label>
              <input 
                id="registerBefore"
                v-model.number="registerBefore" 
                type="number"
                min="0"
                placeholder="24"
                :disabled="loading"
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="location">Sted for aktivitet</label>
              <input 
                id="location"
                v-model="location" 
                type="text"
                placeholder="Hvor aktiviteten finner sted"
                required
                :disabled="loading"
              />
            </div>

            <div class="form-group">
              <label for="meetLocation">Oppmøtested</label>
              <input 
                id="meetLocation"
                v-model="meetLocation" 
                type="text"
                placeholder="Hvor deltakerne skal møte"
                :disabled="loading"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="description">Beskrivelse</label>
            <textarea 
              id="description"
              v-model="description" 
              rows="4"
              placeholder="Beskriv aktiviteten..."
              :disabled="loading"
            ></textarea>
          </div>
        </div>

        <div class="form-section">
          <h3 class="section-title">Avanserte innstillinger</h3>
          
          <div class="form-row">
            <div class="form-group">
              <label for="maxParticipants">Maks deltakere</label>
              <input 
                id="maxParticipants"
                v-model.number="maxParticipants" 
                type="number"
                min="0"
                placeholder="0 for ubegrenset"
                :disabled="loading"
              />
              <small class="form-hint">0 eller tomt for ubegrenset antall deltakere</small>
            </div>

            <div class="form-group">
              <label for="repeatInterval">Gjenta hver (dager)</label>
              <input 
                id="repeatInterval"
                v-model.number="repeatInterval" 
                type="number"
                min="1"
                placeholder="7"
                :disabled="loading"
              />
              <small class="form-hint">F.eks. 7 for ukentlig gjentagelse</small>
            </div>
          </div>
        </div>

        <div class="form-section">
          <h3 class="section-title">Valgmuligheter</h3>
          <p class="section-description">Legg til ekstra valg deltakerne kan velge mellom</p>
          
          <div class="options-container">
            <div class="add-option">
              <div class="form-group">
                <label for="extraOption">Nytt valg</label>
                <div class="option-input-group">
                  <input 
                    id="extraOption"
                    v-model="extraOptionValue"
                    type="text"
                    placeholder="F.eks. Lunsj, Transport"
                    @keydown="handleKeyDown"
                    :disabled="loading"
                  />
                  <button 
                    type="button"
                    @click="addActivityOption"
                    class="add-option-btn"
                    :disabled="!extraOptionValue.trim() || loading"
                  >
                    Legg til
                  </button>
                </div>
              </div>
            </div>

            <div v-if="activityOptions.length > 0" class="options-list">
              <h4 class="options-list-title">Valg som er lagt til:</h4>
              <div class="options-grid">
                <ActivityOption 
                  v-for="option in activityOptions" 
                  :key="option"
                  :value="option"
                  @close="removeOption"
                />
              </div>
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="submit-btn" :disabled="loading || !name || !eventDate || !location">
            {{ loading ? 'Oppretter...' : 'Opprett aktivitet' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.create-activity-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
}

.create-activity-card {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f8f9fa;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 8px 0;
}

.page-subtitle {
  color: #6c757d;
  margin: 0;
  font-size: 16px;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 12px 16px;
  border-radius: 4px;
  margin-bottom: 24px;
  border: 1px solid #f5c6cb;
  font-size: 14px;
}

.field-errors {
  margin-bottom: 24px;
}

.field-error {
  background: #f8d7da;
  color: #721c24;
  padding: 8px 12px;
  border-radius: 4px;
  margin-bottom: 8px;
  border: 1px solid #f5c6cb;
  font-size: 14px;
}

.field-error:last-child {
  margin-bottom: 0;
}

.field-error strong {
  text-transform: capitalize;
}

.activity-form {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.form-section {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 16px 0;
}

.section-description {
  color: #6c757d;
  margin: 0 0 20px 0;
  font-size: 14px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #495057;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.2s, box-shadow 0.2s;
  background: #fff;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #80bdff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.form-group input:disabled,
.form-group textarea:disabled {
  background: #f8f9fa;
  color: #6c757d;
  cursor: not-allowed;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: #adb5bd;
}

.form-hint {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: #6c757d;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.option-input-group {
  display: flex;
  gap: 8px;
}

.option-input-group input {
  flex: 1;
}

.add-option-btn {
  background: #28a745;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.add-option-btn:hover:not(:disabled) {
  background: #218838;
}

.add-option-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  opacity: 0.6;
}

.options-list {
  background: #fff;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  padding: 16px;
}

.options-list-title {
  font-size: 16px;
  font-weight: 500;
  color: #495057;
  margin: 0 0 12px 0;
}

.options-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.form-actions {
  display: flex;
  justify-content: center;
  padding-top: 24px;
  border-top: 1px solid #e9ecef;
}

.submit-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 14px 32px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  min-width: 200px;
}

.submit-btn:hover:not(:disabled) {
  background: #0056b3;
}

.submit-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  opacity: 0.6;
}

@media (max-width: 768px) {
  .create-activity-container {
    padding: 16px;
  }
  
  .create-activity-card {
    padding: 24px 20px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .form-section {
    padding: 20px 16px;
  }
  
  .option-input-group {
    flex-direction: column;
  }
  
  .add-option-btn {
    width: 100%;
  }
  
  .submit-btn {
    width: 100%;
  }
}
</style>
