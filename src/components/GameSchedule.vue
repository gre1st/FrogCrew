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
          <th>Positions</th>
          <th v-if="isAdmin">Actions</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="g in games" :key="g.gameId">
          <td>{{ formatDate(g.gameDate) }}</td>
          <td>{{ formatTime(g.gameDate) }}</td>
          <td>{{ g.opponent }}</td>
          <td>{{ g.venue }}</td>

          <td>
            <span v-if="displayAssignments(g).length">
              {{ displayAssignments(g).join(', ') }}
            </span>
            <span v-else>—</span>
          </td>

          <td v-if="isAdmin">
            <button class="del-btn" @click="removeGame(g)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { GameApi }         from '@/api/gameApi'
import crewScheduleApi     from '@/api/crewScheduleApi'

/* role check */
const isAdmin = (localStorage.getItem('role') || '')
  .replace(/[^a-z]/gi, '')
  .toUpperCase() === 'ADMIN'

const games       = ref([])
const scheduleMap = new Map() // gameId → scheduleId (needed for DELETE)

onMounted(loadGames)

/* ── loaders ─────────────────────────────────────────── */
async function loadGames () {
  try {
    const all = await GameApi.getAllGames()

    const withAssignments = await Promise.all(
      all.map(async g => {
        try {
          const schedRaw = await crewScheduleApi.getCrewScheduleByGameId(g.gameId)
          console.log('scheduleDto for game', g.gameId, schedRaw)

          // find the array that actually contains crew members
          const list = schedRaw.crewMembers ?? schedRaw.members ?? schedRaw.schedule ?? schedRaw.assignments ?? []

          const assignments = list.map(m => {
            const pos  = m.position ?? m.role ?? m.positionName ?? ''
            const name = (
              m.fullName ||
              [m.firstName, m.lastName].filter(Boolean).join(' ') ||
              m.name ||
              m.username ||
              `#${m.userId ?? m.id ?? ''}`
            ).trim()
            return pos && name ? `${pos} (${name})` : pos || name
          })

          return { ...g, assignments }
        } catch (e) {
          console.warn('No crew schedule for game', g.gameId)
          return { ...g, assignments: [] }
        }
      })
    )

    games.value = withAssignments
    withAssignments.forEach(g => scheduleMap.set(g.gameId, g.scheduleId))
  } catch (err) {
    console.error('Failed to load games:', err)
  }
}

/* helpers */
function displayAssignments (g) {
  // prefer populated assignments; fallback to plain positions list
  if (g.assignments && g.assignments.length) return g.assignments
  return g.requiredPositions ?? g.positions ?? []
}

async function removeGame (g) {
  if (!confirm('Delete this game?')) return
  try {
    await GameApi.deleteGame(scheduleMap.get(g.gameId), g.gameId)
    games.value = games.value.filter(x => x.gameId !== g.gameId)
  } catch (err) {
    console.error('Delete failed:', err)
    alert('Could not delete game.')
  }
}

const formatDate = dt => new Date(dt).toLocaleDateString()
const formatTime = dt => new Date(dt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
</script>

<style scoped>
.schedule { padding: 1rem; }

table      { width: 100%; border-collapse: collapse; margin-top: 1rem; }
th, td     { border: 1px solid #ddd; padding: 0.75rem; text-align: left; }
th         { background: #f4f4f4; }

.del-btn   { padding: 0.4rem 0.75rem; background: #e74c3c; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
</style>