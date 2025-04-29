<template>
  <div class="schedule">
    <h2>Upcoming Games</h2>

    <table>
      <thead>
        <tr>
          <th>Date</th>
          <th>Time</th>
          <th>Opponent</th>
          <th>Venue</th>
          <th v-if="role === 'admin'">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="game in games" :key="game.gameId">
          <td>{{ formatDate(game.gameDate) }}</td>
          <td>{{ formatTime(game.gameDate) }}</td>
          <td>{{ game.opponent }}</td>
          <td>{{ game.venue }}</td>
          <td v-if="role === 'admin'">
            <button @click="openEditModal(game)">Edit</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Edit Modal -->
    <div v-if="editingGame" class="modal">
      <div class="modal-content">
        <h3>Edit Game</h3>

        <form @submit.prevent="saveChanges">
          <div>
            <label>Game Date:</label>
            <input v-model="editForm.date" type="date" required />
          </div>

          <div>
            <label>Game Time:</label>
            <input v-model="editForm.time" type="time" required />
          </div>

          <div>
            <label>Opponent:</label>
            <input v-model="editForm.opponent" required />
          </div>

          <div>
            <label>Venue:</label>
            <input v-model="editForm.venue" required />
          </div>

          <div class="buttons">
            <button type="submit">Save</button>
            <button type="button" @click="cancelEdit">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { GameApi } from '@/api/gameApi'

const role = localStorage.getItem('role')

const games = ref([])
const editingGame = ref(null)
const editForm = reactive({
  date: '',
  time: '',
  opponent: '',
  venue: ''
})

// Load games when component mounts
onMounted(async () => {
  await loadGames()
})

async function loadGames() {
  try {
    const data = await GameApi.getAllGames()
    games.value = data
  } catch (error) {
    console.error('Failed to load games:', error)
  }
}

function openEditModal(game) {
  editingGame.value = game
  const localDateTime = new Date(game.gameDate)
  editForm.date = localDateTime.toISOString().substring(0, 10)
  editForm.time = localDateTime.toISOString().substring(11, 16)
  editForm.opponent = game.opponent
  editForm.venue = game.venue
}

async function saveChanges() {
  if (!editingGame.value) return

  try {
    const updatedGame = {
      scheduleId: editingGame.value.scheduleId,
      gameDate: `${editForm.date}T${editForm.time}:00`,
      venue: editForm.venue,
      opponent: editForm.opponent,
      finalized: editingGame.value.finalized
    }
    await GameApi.updateGame(editingGame.value.gameId, updatedGame)

    alert('✅ Game updated successfully!')
    editingGame.value = null
    await loadGames()
  } catch (error) {
    console.error('Failed to save changes:', error)
  }
}

function cancelEdit() {
  editingGame.value = null
}

function formatDate(dateTime) {
  const d = new Date(dateTime)
  return d.toLocaleDateString()
}

function formatTime(dateTime) {
  const d = new Date(dateTime)
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}
</script>

<style scoped>
.schedule {
  padding: 1rem;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
}
th {
  background-color: #f4f4f4;
}
button {
  padding: 0.4rem 0.75rem;
  background-color: #3f51b5;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
}
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
input {
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.buttons {
  display: flex;
  justify-content: space-between;
}
</style>
