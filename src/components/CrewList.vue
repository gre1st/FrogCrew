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
/* unchanged styling */
</style>
