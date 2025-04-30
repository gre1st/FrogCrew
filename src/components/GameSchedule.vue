<template>
  <div class="schedule">
    <h2>Upcoming Games</h2>

    <table>
      <thead>
        <tr>
          <th>Schedule</th>            <!-- new column -->
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
          <!-- sport + season -->
          <td>
            <span v-if="g.scheduleInfo">
              {{ g.scheduleInfo.sport }} – {{ g.scheduleInfo.season }}
            </span>
            <span v-else>—</span>
          </td>

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
import { ref, onMounted }  from 'vue'
import { GameApi }         from '@/api/gameApi'
import crewScheduleApi     from '@/api/crewScheduleApi'
import { ScheduleApi }     from '@/api/scheduleApi'   // <- right file, right name!

/* role check */
const isAdmin = (localStorage.getItem('role') || '')
  .replace(/[^a-z]/gi, '')
  .toUpperCase() === 'ADMIN'

const games        = ref([])
const scheduleMap  = new Map()  // gameId  → scheduleId (for DELETE)
const schedMetaMap = ref({})    // scheduleId → { sport, season }

onMounted(loadGames)

/* ── loaders ─────────────────────────────────────────── */
async function loadGames () {
  try {
    const all = await GameApi.getAllGames()

    /* —— grab schedule meta once per unique scheduleId —— */
    const uniqueIds = [
      ...new Set(
        all
          .map(g => g.scheduleId ?? g.gameScheduleId ?? null)  // <- adjust if backend differs
          .filter(Boolean)
      )
    ]

    if (uniqueIds.length) {
      const meta = await Promise.all(
        uniqueIds.map(id =>
          // IMPORTANT: findScheduleById is the correct name
          ScheduleApi.findScheduleById(id).catch(() => null)
        )
      )
      schedMetaMap.value = Object.fromEntries(
        meta.filter(Boolean).map(s => [s.id, { sport: s.sport, season: s.season }])
      )
    }

    /* —— pull crew assignments —— */
    const withAssignments = await Promise.all(
      all.map(async g => {
        const scheduleId = g.scheduleId ?? g.gameScheduleId ?? null

        let assignments = []
        try {
          const schedRaw = await crewScheduleApi.getCrewScheduleByGameId(g.gameId)
          const list = schedRaw.crewMembers ?? schedRaw.members ??
                       schedRaw.schedule    ?? schedRaw.assignments ?? []

          assignments = list.map(m => {
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
        } catch {
          // no crew schedule – leave assignments empty
        }

        return {
          ...g,
          assignments,
          scheduleInfo: schedMetaMap.value[scheduleId] || null,
          scheduleId                              // keep a clean copy
        }
      })
    )

    games.value = withAssignments
    withAssignments.forEach(g => scheduleMap.set(g.gameId, g.scheduleId))
  } catch (err) {
    console.error('Failed to load games:', err)
    alert('Could not load games list.')
  }
}

/* helpers */
function displayAssignments (g) {
  if (g.assignments?.length) return g.assignments
  return g.requiredPositions ?? g.positions ?? []
}

async function removeGame (g) {
  if (!confirm('Delete this game?')) return
  try {
    await GameApi.deleteGame(g.scheduleId, g.gameId)
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
.schedule {
  
  background: #fff;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);


  width: 100%;
  max-width: 900px;
  margin: 0 auto;
}

table      { width: 100%; border-collapse: collapse; margin-top: 1rem; }
th, td     { border: 1px solid #ddd; padding: 0.75rem; text-align: left; }
th         { background: #f4f4f4; }


th:first-child,
td:first-child { white-space: nowrap; }

.del-btn   { padding: 0.4rem 0.75rem; background: #e74c3c; color: #fff;
             border: none; border-radius: 4px; cursor: pointer; }
</style>
