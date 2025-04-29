<template>
  <div class="schedule-crew">
    <h2>Assign Crew to Game</h2>

    <div v-if="role !== 'admin'">
      <p>🚫 Only admins can assign crew members.</p>
    </div>

    <div v-else>
      <label>Select a Game:</label>
      <select v-model="selectedGameId">
        <option disabled value="">-- Select a Game --</option>
        <option
          v-for="game in games"
          :key="game.gameId"
          :value="game.gameId"
        >
          {{ game.venue }} – {{ game.opponent }}
          ({{ formatDate(game.gameDate) }})
        </option>
      </select>

      <div v-if="selectedGame">
        <h3>
          Assign Crew for {{ selectedGame.venue }} vs
          {{ selectedGame.opponent }}
        </h3>

        <!-- if there are any positions to fill -->
        <div
          v-if="selectedGame.requiredPositions.length > 0"
          class="positions-container"
        >
          <div
            v-for="(position, idx) in selectedGame.requiredPositions"
            :key="idx"
            class="assignment-row"
          >
            <label>{{ position }}:</label>
            <select v-model="assignments[position]">
              <option disabled value="">
                -- Select Crew Member --
              </option>
              <option
                v-for="member in qualifiedCrew(position)"
                :key="member.id"
                :value="member.fullName"
              >
                {{ member.fullName }} ({{ member.positions.join(', ') }})
              </option>
            </select>
          </div>
        </div>

        <!-- fallback if no positions defined -->
        <p v-else>No positions available to assign for this game.</p>

        <button
          @click="submitAssignments"
          :disabled="selectedGame.requiredPositions.length === 0"
        >
          Assign Crew
        </button>

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
import { GameApi } from '../api/gameApi'
import { CrewApi } from '../api/crewApi'

const role = localStorage.getItem('role')

const selectedGameId = ref('')
const submitted = ref(false)
const games = ref([])
const crewMembers = ref([])
const assignments = reactive({})

// wrap any missing `requiredPositions` into an empty array
const selectedGame = computed(() => {
  const g = games.value.find((g) => g.gameId === selectedGameId.value)
  if (!g) return null
  return {
    ...g,
    requiredPositions: Array.isArray(g.requiredPositions)
      ? g.requiredPositions
      : []
  }
})

// whenever you pick a new game, make sure `assignments[pos]` exists
watch(
  selectedGame,
  (g) => {
    if (g) {
      g.requiredPositions.forEach((pos) => {
        if (!(pos in assignments)) {
          assignments[pos] = ''
        }
      })
    }
    submitted.value = false
  },
  { immediate: true }
)

function qualifiedCrew(position) {
  return crewMembers.value.filter((member) =>
    (member.positions || []).includes(position)
  )
}

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString()
}

async function loadGames() {
  try {
    const all = await GameApi.getAllGames()
    games.value = all.filter((g) => !g.finalized)
  } catch (e) {
    console.error('Failed to load games', e)
  }
}

async function loadCrew() {
  try {
    crewMembers.value = await CrewApi.getAllCrewMembers()
  } catch (e) {
    console.error('Failed to load crew members', e)
  }
}

async function submitAssignments() {
  if (!selectedGame.value) return alert('Please select a game.')
  const missing = selectedGame.value.requiredPositions.find(
    (pos) => !assignments[pos]
  )
  if (missing) {
    return alert(`Please assign a crew member to: ${missing}`)
  }

  // TODO: call your backend API here
  submitted.value = true
  console.log('Assignments saved (mocked):', assignments)
}

onMounted(() => {
  loadGames()
  loadCrew()
})
</script>

<style scoped>
.schedule-crew {
  padding: 1rem;
  max-width: 600px;
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
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.confirmation {
  margin-top: 1rem;
}
</style>
