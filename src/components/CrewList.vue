<template>
  <div class="crew-directory">
    <h2>Crew Directory</h2>

    <!-- crew list -->
    <ul v-if="crewMembers.length">
      <li v-for="c in crewMembers" :key="c.id">
        <button class="name-btn" @click="selectCrew(c)">
          {{ c.firstName }} {{ c.lastName }}
        </button>
        <!-- quick peek at positions right in the list (optional) -->
        <span v-if="c.positions?.length"
              class="pos-chip">
          {{ c.positions.join(', ') }}
        </span>
      </li>
    </ul>
    <p v-else>Loading crew members…</p>

    <!-- inline profile -->
    <div v-if="selectedCrew" class="profile-display">
      <h3>Profile</h3>

      <p><strong>Name:</strong>
         {{ selectedCrew.firstName }} {{ selectedCrew.lastName }}</p>

      <p><strong>Email:</strong> {{ selectedCrew.email }}</p>
      <p><strong>Phone:</strong> {{ selectedCrew.phoneNumber }}</p>

      <p v-if="selectedCrew.positions?.length">
        <strong>Positions:</strong>
        {{ selectedCrew.positions.join(', ') }}
      </p>

      <p v-if="loadingDetails">Fetching full profile…</p>

      <button @click="selectedCrew = null">Close</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllCrewMembers, getCrewMemberById } from '@/api/crewApi'

const crewMembers    = ref([])
const selectedCrew   = ref(null)
const loadingDetails = ref(false)

/* fetch directory */
onMounted(async () => {
  try {
    crewMembers.value = await getAllCrewMembers()
  } catch (err) {
    console.error('Failed to load crew members:', err)
  }
})

/* preview + enrich */
async function selectCrew(c) {
  selectedCrew.value   = { ...c }      // quick preview
  loadingDetails.value = true
  try {
    const full = await getCrewMemberById(c.id)
    selectedCrew.value = full
  } catch (err) {
    console.error('Failed to load profile:', err)
  } finally {
    loadingDetails.value = false
  }
}
</script>

<style scoped>
.crew-directory {
  max-width: 700px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: system-ui, sans-serif;
}

ul {
  list-style: none;
  padding: 0;
  margin: 1rem 0;
  display: flex;
  flex-direction: column;
  gap: .5rem;
}

/* clickable name pill */
.name-btn {
  background: #fafafa;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: .35rem .9rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background .15s;
}
.name-btn:hover {
  background: #f0f0f0;
}

/* positions chip shown next to each crew member */
.pos-chip {
  margin-left: .5rem;
  font-size: .85rem;
  color: #555;
}

/* profile preview card */
.profile-display {
  margin-top: 2rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.25rem;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,.04);
}

.profile-display h3 {
  margin-top: 0;
  margin-bottom: .75rem;
  font-size: 1.2rem;
}

.profile-display p {
  margin: .4rem 0;
}

.profile-display button {
  margin-top: 1rem;
  padding: .4rem .9rem;
  border: none;
  border-radius: 4px;
  background: #e74c3c;
  color: #fff;
  cursor: pointer;
  transition: background .15s;
}
.profile-display button:hover {
  background: #cf3f33;
}
</style>
