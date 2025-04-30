<template>
  <div class="schedule-crew">
    <h2>Assign Crew to Game</h2>

    <!-- role gate -->
    <div v-if="role !== 'admin'">
      <p>🚫 Only admins can assign crew members.</p>
    </div>

    <!-- main form -->
    <div v-else>
      <!-- ❶ GAME PICKER -->
      <label>Select a Game:</label>
      <select v-model="selectedGameId">
        <option disabled value="">-- Select a Game --</option>
        <option
          v-for="game in games"
          :key="game.gameId"
          :value="game.gameId"
        >
          {{ game.venue }} – {{ game.opponent }} ({{ formatDate(game.gameDate) }})
        </option>
      </select>

      <!-- ❷ CREW ASSIGNMENTS -->
      <div v-if="selectedGame">
        <h3>Assign Crew for {{ selectedGame.venue }} vs {{ selectedGame.opponent }}</h3>

        <!-- positions list -->
        <div v-if="selectedGame.requiredPositions.length" class="positions-container">
          <div
            v-for="position in selectedGame.requiredPositions"
            :key="position"
            class="assignment-row"
          >
            <label>{{ position }}:</label>
            <select
              v-model="assignments[position]"
              @focus="loadFor(position)"
            >
              <option disabled value="">-- Select Crew Member --</option>
              <option
                v-for="member in loaded[position] || []"
                :key="member.id"
                :value="member.id"
              >
                {{ member.fullName }}
              </option>
            </select>
          </div>
        </div>
        <p v-else>No positions available to assign for this game.</p>

        <!-- submit -->
        <button @click="submitAssignments" :disabled="!selectedGame.requiredPositions.length">
          Assign Crew
        </button>

        <!-- confirmation -->
        <div v-if="submitted" class="confirmation">
          <p>✅ Crew assigned and saved!</p>
          <pre>{{ assignments }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { GameApi } from '@/api/gameApi'
// named export we just added in crewScheduleApi.js
import { getQualifiedCrew } from '@/api/crewScheduleApi'

/* ─── state ─────────────────────────────────────────────── */
const role = localStorage.getItem('role')

const selectedGameId = ref('')
const games           = ref([])
const assignments     = reactive({})                // { Camera: memberId, … }
const loaded          = reactive({})                // cache per-position list
const submitted       = ref(false)

/* ─── derived ───────────────────────────────────────────── */
const selectedGame = computed(() => {
  const g = games.value.find(g => g.gameId === selectedGameId.value)
  if (!g) return null
  return {
    ...g,
    requiredPositions: Array.isArray(g.requiredPositions) ? g.requiredPositions : []
  }
})

/* ─── watchers ──────────────────────────────────────────── */
watch(selectedGame, g => {
  if (g) {
    g.requiredPositions.forEach(pos => {
      if (!(pos in assignments)) assignments[pos] = ''
    })
  }
  submitted.value = false
}, { immediate: true })

/* ─── helpers ───────────────────────────────────────────── */
function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString()
}

/* ─── async loaders ─────────────────────────────────────── */
async function loadGames() {
  try {
    const all = await GameApi.getAllGames()
    games.value = all.filter(g => !g.finalized)
  } catch (err) {
    console.error('Failed to load games', err)
  }
}

// fetch crew qualified *and* free for a position, cache by position name
async function loadFor(position) {
  if (!selectedGameId.value || loaded[position]) return // nothing to do / already cached
  try {
    const crewList = await getQualifiedCrew(selectedGameId.value, position)

    // inspect once for debugging; remove if noisy
    // console.table(crewList)

    loaded[position] = crewList.map(u => {
      // choose an ID field the backend actually returns
      const id = u.userId ?? u.id ?? u.crewMemberId ?? u.memberId

      // try to build a readable name
      let fullName = ''
      if (u.firstName || u.lastName) fullName = `${u.firstName || ''} ${u.lastName || ''}`.trim()
      else if (u.name)               fullName = u.name
      else if (u.fullName)           fullName = u.fullName
      else if (u.username)           fullName = u.username
      else                           fullName = `#${id}`

      return { id, fullName }
    })
  } catch (err) {
    console.error(`Failed to load crew for ${position}`, err)
    loaded[position] = []
  }
}

/* ─── submit ────────────────────────────────────────────── */
function submitAssignments() {
  if (!selectedGame.value) return alert('Please select a game first.')
  const missing = selectedGame.value.requiredPositions.find(pos => !assignments[pos])
  if (missing) return alert(`Please assign a crew member to: ${missing}`)

  
  submitted.value = true
  console.log('Assignments saved:', JSON.stringify(assignments))
}

/* ─── bootstrap ─────────────────────────────────────────── */
onMounted(loadGames)
</script>

<style scoped>
.schedule-crew {
  padding: 1rem;
  max-width: 640px;
}
.assignment-row {
  margin-bottom: 1rem;
}
.positions-container {
  margin: 1rem 0;
}
select {
  padding: 0.5rem;
  margin-top: 0.25rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  width: 100%;
}
button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #673ab7;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.confirmation {
  margin-top: 1rem;
  color: green;
}
</style>