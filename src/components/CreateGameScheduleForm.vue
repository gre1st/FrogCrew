<template>
  <div class="create-schedule">
    <h2>Create New Game Schedule</h2>

    <form @submit.prevent="submitSchedule">
      <div>
        <label>Sport Type:</label>
        <input v-model="form.sport" required />
      </div>

      <div>
        <label>Season:</label>
        <input v-model="form.season" required />
      </div>

      <button type="submit">Create Schedule</button>

      <div v-if="submitted">
        <p>✅ Schedule created successfully!</p>
        <pre>{{ form }}</pre>
      </div>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ScheduleApi } from '@/api/scheduleApi'

const form = reactive({
  sport: '',
  season: ''
})

const submitted = ref(false)

async function submitSchedule() {
  try {
    await ScheduleApi.addSchedule(form)
    submitted.value = true
  } catch (error) {
    console.error('Failed to create schedule', error)
    alert('Error creating schedule.')
  }
}
</script>

<style scoped>
.create-schedule {
  padding: 1rem;
  max-width: 600px;
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
button {
  padding: 0.5rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
