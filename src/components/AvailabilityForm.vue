<!-- src/components/AvailabilityForm.vue -->
<template>
  <div>
    <form
      class="availability-form"
      v-if="role === 'crew'"
      @submit.prevent="submitAvailability"
    >
      <div class="field">
        <label for="game">Select Game:</label>
        <select id="game" v-model="availability.gameId" required>
          <option disabled value="">-- Select a Game --</option>
          <option
            v-for="game in games"
            :key="game.gameId"
            :value="game.gameId"
          >
            {{ game.opponent }} – {{ formatDate(game.gameDate) }}
          </option>
        </select>
      </div>

      <div class="field checkbox-field">
        <input
          id="available"
          type="checkbox"
          v-model="availability.availability"
        />
        <label for="available">I am available for this game</label>
      </div>

      <div class="field">
        <label for="comment">Comment (optional):</label>
        <input id="comment" v-model="availability.comment" />
      </div>

      <div class="field">
        <button type="submit">Submit Availability</button>
      </div>

      <div v-if="submitted" class="success-message">
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
const currentUserId = parseInt(localStorage.getItem('userId'))

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
  return date.toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

onMounted(loadGames)
</script>

<style scoped>
.availability-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* Generic field layout */
.field {
  display: flex;
  flex-direction: column;
}

/* Text inputs & selects only (exclude checkboxes) */
.field select,
.field input:not([type="checkbox"]) {
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  transition: border-color 0.2s;
}
.field select:focus,
.field input:not([type="checkbox"]):focus {
  outline: none;
  border-color: #7e57c2;
}

/* Checkbox row: big box and label on same line */
.field.checkbox-field {
  flex-direction: row;
  align-items: center;
}
.field.checkbox-field input[type="checkbox"] {
  transform: scale(1.3);
  margin-right: 0.75rem;
  cursor: pointer;
  /* reset width so it doesn’t stretch */
  width: auto;
  height: auto;
}
.field.checkbox-field label {
  margin: 0;
  font-weight: 500;
  color: #333;
}

/* Submit button */
button {
  padding: 0.75rem;
  font-size: 1rem;
  font-weight: 600;
  color: #fff;
  background-color: #7e57c2;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}
button:hover {
  background-color: #6a1b9a;
}

/* Success message */
.success-message {
  margin-top: 1rem;
  padding: 1rem;
  background: #e8f5e9;
  border-radius: 6px;
  color: #2e7d32;
  font-weight: 500;
}
</style>
