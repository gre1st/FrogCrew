<template>
  <div class="add-game">
    <h2>Add Game to Existing Schedule</h2>

    <!-- Season picker -->
    <div>
      <label>Season:</label>
      <select v-model="selectedSeason" @change="fetchSchedules" required>
        <option disabled value="">-- Select Season --</option>
        <option>Spring 2025</option>
        <option>Fall 2025</option>
        <option>2025</option>
      </select>
    </div>

    <form @submit.prevent="addGame">
      <div>
        <label>Select Schedule:</label>
        <select v-model="selectedScheduleId" required>
          <option disabled value="">-- Select a Schedule --</option>
          <option v-for="schedule in schedules" :key="schedule.id" :value="schedule.id">
            {{ schedule.sport }} - {{ schedule.season }}
          </option>
        </select>
      </div>

      <div>
        <label>Opponent:</label>
        <input v-model="form.opponent" required />
      </div>

      <div>
        <label>Venue:</label>
        <input v-model="form.venue" required />
      </div>

      <div>
        <label>Game Date and Time:</label>
        <input v-model="form.gameDate" type="datetime-local" required />
      </div>

      <button type="submit">Add Game</button>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ScheduleApi } from '@/api/scheduleApi'
import { GameApi } from '@/api/gameApi'

const router = useRouter()
const selectedSeason = ref('')
const selectedScheduleId = ref('')
const schedules = ref([])

const form = reactive({
  opponent: '',
  venue: '',
  gameDate: ''
})

async function fetchSchedules() {
  if (!selectedSeason.value) return
  try {
    schedules.value = await ScheduleApi.findSchedulesBySeason(selectedSeason.value)
  } catch (error) {
    console.error('Failed to load schedules', error)
  }
}

async function addGame() {
  if (!selectedScheduleId.value) {
    alert('Please select a schedule.')
    return
  }
  try {
    const payload = {
      scheduleId: selectedScheduleId.value,
      gameDate: form.gameDate,
      venue: form.venue,
      opponent: form.opponent,
      finalized: false
    }
    await GameApi.addGameToSchedule(selectedScheduleId.value, payload)
    // Redirect back to schedule list to refresh
    router.push('/schedule')
  } catch (error) {
    console.error('Failed to add game', error)
    alert('Something went wrong adding the game.')
  }
}

onMounted(() => {
  // Optionally set a default season
  // selectedSeason.value = 'Spring 2025'
  // fetchSchedules()
})
</script>

<style scoped>
.add-game {
  padding: 1rem;
  max-width: 600px;
}
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
input, select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 0.5rem;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
