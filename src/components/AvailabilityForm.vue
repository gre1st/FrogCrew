<template>
  <div>
    <form v-if="role === 'crew'" @submit.prevent="submitAvailability">
      <div>
        <label for="game">Select Game:</label>
        <select v-model="availability.gameId" required>
          <option disabled value="">-- Select a Game --</option>
          <option v-for="game in games" :key="game.gameId" :value="game.gameId">
            {{ game.opponent }} - {{ formatDate(game.gameDate) }}
          </option>
        </select>
      </div>

      <div>
        <label>
          <input type="checkbox" v-model="availability.availability" />
          I am available for this game
        </label>
      </div>

      <div>
        <label>Comment (optional):</label>
        <input v-model="availability.comment" />
      </div>

      <div>
        <button type="submit">Submit Availability</button>
      </div>

      <div v-if="submitted">
        <p>✅ Availability submitted successfully!</p>
        <pre>{{ availability }}</pre>
      </div>
    </form>

    <div v-else>
      <p>🚫 You must be a crew member to submit availability.</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { AvailabilityApi } from '@/api/availabilityApi'
import { GameApi } from '@/api/gameApi'

const role = localStorage.getItem('role')
const currentUserId = parseInt(localStorage.getItem('userId')) // assuming userId is saved in localStorage

const games = ref([])
const submitted = ref(false)

const availability = reactive({
  userId: currentUserId,
  gameId: '',
  availability: false,
  comment: ''
})

async function loadGames() {
  try {
    games.value = await GameApi.getAllGames()
  } catch (error) {
    console.error('Failed to fetch games', error)
  }
}

async function submitAvailability() {
  if (!availability.gameId) {
    alert('Please select a game.')
    return
  }
  try {
    await AvailabilityApi.submitAvailability(availability)
    submitted.value = true
  } catch (error) {
    console.error('Failed to submit availability', error)
    alert('Failed to submit availability')
  }
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' })
}

onMounted(loadGames)
</script>

<style scoped>
form {
  max-width: 500px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
}
select, input[type='text'] {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
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
