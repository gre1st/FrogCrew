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
          <option v-for="s in schedules" :key="s.id" :value="s.id">
            {{ s.sport }} – {{ s.season }}
          </option>
        </select>
      </div>

      <div>
        <label>Opponent:</label>
        <input v-model.trim="form.opponent" required />
      </div>

      <div>
        <label>Venue:</label>
        <input v-model.trim="form.venue" required />
      </div>

      <div>
        <label>Game Date &amp; Time:</label>
        <input v-model="form.gameDate" type="datetime-local" required />
      </div>

      <!-- ★ new: positions -->
      <div>
        <label>Required Positions (comma-separated):</label>
        <input v-model.trim="form.positions"
               placeholder="Camera, TD, Graphics" />
      </div>

      <button type="submit">Add Game</button>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter }                from 'vue-router'
import { ScheduleApi }              from '@/api/scheduleApi'
import { GameApi }                  from '@/api/gameApi'

const router             = useRouter()
const selectedSeason     = ref('')
const selectedScheduleId = ref('')
const schedules          = ref([])

const form = reactive({
  opponent   : '',
  venue      : '',
  gameDate   : '',
  positions  : ''          // ← new
})

/* Fetch schedules for chosen season */
async function fetchSchedules () {
  if (!selectedSeason.value) return
  try {
    schedules.value = await ScheduleApi.findSchedulesBySeason(selectedSeason.value)
  } catch (err) {
    console.error('Failed to load schedules', err)
  }
}

/* Add game to selected schedule */
async function addGame () {
  if (!selectedScheduleId.value) {
    alert('Please select a schedule.')
    return
  }
  try {
    const payload = {
      scheduleId        : selectedScheduleId.value,
      gameDate          : form.gameDate,
      venue             : form.venue,
      opponent          : form.opponent,
      finalized         : false,
      requiredPositions : (form.positions || '')
                          .split(',')
                          .map(p => p.trim())
                          .filter(Boolean)     // => [] if empty
    }

    await GameApi.addGameToSchedule(selectedScheduleId.value, payload)
    router.push('/schedule')                   // refresh list
  } catch (err) {
    console.error('Failed to add game', err)
    alert('Something went wrong adding the game.')
  }
}

/* optional: auto-populate season */
onMounted(() => {
  // selectedSeason.value = 'Spring 2025'
  // fetchSchedules()
})
</script>

<style scoped>
.add-game { padding:1rem; max-width:600px; }
form      { display:flex; flex-direction:column; gap:1rem; }
input,
select    { padding:0.5rem; border:1px solid #ccc; border-radius:4px; }
button    { padding:0.5rem; background:#2196f3; color:#fff; border:none;
            border-radius:4px; cursor:pointer; }
</style>
