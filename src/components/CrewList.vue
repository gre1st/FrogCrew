<template>
  <div class="crew-directory">
    <h2>Crew Directory</h2>

    <ul v-if="crewMembers.length">
      <li v-for="c in crewMembers" :key="c.userId">
        <button class="name-btn" @click="selectCrew(c.userId)">
          {{ c.fullName }}
        </button>
      </li>
    </ul>
    <p v-else>Loading crew members…</p>

    <!-- Inline profile display -->
    <div v-if="selectedCrew" class="profile-display">
      <h3>Profile</h3>
      <p><strong>Name:</strong> {{ selectedCrew.firstName }} {{ selectedCrew.lastName }}</p>
      <p><strong>Email:</strong> {{ selectedCrew.email }}</p>
      <p><strong>Phone:</strong> {{ selectedCrew.phoneNumber }}</p>
      <p><strong>Positions:</strong> {{ selectedCrew.positions.join(', ') }}</p>
      <button @click="selectedCrew = null">Close</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllCrewMembers, getCrewMemberById } from '@/api/crewApi'

const crewMembers = ref([])
const selectedCrew = ref(null)

onMounted(async () => {
  try {
    crewMembers.value = await getAllCrewMembers()
  } catch (err) {
    console.error('Failed to load crew members:', err)
  }
})

async function selectCrew(userId) {
  try {
    // Clear any previous selection
    selectedCrew.value = null
    // Fetch full profile for that user
    const profile = await getCrewMemberById(userId)
    selectedCrew.value = profile
  } catch (err) {
    console.error('Failed to load profile:', err)
  }
}
</script>

<style scoped>
.crew-directory {
  padding: 1rem;
}

ul {
  list-style: none;
  padding: 0;
}

li + li {
  margin-top: 0.5rem;
}

.name-btn {
  background: none;
  border: none;
  padding: 0;
  font: inherit;
  color: #2c3e50;
  cursor: pointer;
  text-decoration: underline;
}

.name-btn:hover {
  color: #1a242f;
}

.profile-display {
  margin-top: 1.5rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: #f9f9f9;
}

.profile-display p {
  margin: 0.5rem 0;
}

.profile-display button {
  margin-top: 1rem;
  padding: 0.4rem 0.8rem;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
