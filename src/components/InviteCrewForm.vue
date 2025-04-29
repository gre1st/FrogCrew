<template>
  <div class="invite-crew">
    <h2>Invite New Crew Member</h2>

    <form @submit.prevent="submitForm">
      <div>
        <label>First Name:</label>
        <input v-model="form.firstName" required />
      </div>

      <div>
        <label>Last Name:</label>
        <input v-model="form.lastName" required />
      </div>

      <div>
        <label>Email:</label>
        <input v-model="form.email" type="email" required />
      </div>

      <div>
        <label>Phone Number:</label>
        <input v-model="form.phoneNumber" required pattern="\\d{3}-\\d{3}-\\d{4}" />
      </div>

      <div>
        <label>Role:</label>
        <select v-model="form.role" required>
          <option disabled value="">-- Select Role --</option>
          <option value="crew">Crew</option>
          <option value="admin">Admin</option>
        </select>
      </div>

      <div>
        <label>Positions (comma-separated):</label>
        <input v-model="form.positions" required placeholder="e.g. Camera, Audio A1" />
      </div>

      <button type="submit">Send Invite</button>
    </form>

    <div v-if="successMessage" class="success">{{ successMessage }}</div>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { CrewApi } from '@/api/crewApi' // <--- using your real CrewApi!

const form = reactive({
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  role: '',
  positions: ''
})

const successMessage = ref('')
const errorMessage = ref('')

async function submitForm() {
  successMessage.value = ''
  errorMessage.value = ''

  try {
    // prepare payload to match your FrogCrewUser backend
    const newCrewMember = {
      firstName: form.firstName,
      lastName: form.lastName,
      email: form.email,
      phoneNumber: form.phoneNumber,
      role: form.role,
      positions: form.positions.split(',').map(p => p.trim())
    }

    await CrewApi.createCrewMember(newCrewMember)

    successMessage.value = '✅ Crew Member invited successfully!'
    Object.assign(form, { firstName: '', lastName: '', email: '', phoneNumber: '', role: '', positions: '' })
  } catch (error) {
    console.error(error)
    errorMessage.value = '❌ Failed to invite crew member. Please try again.'
  }
}
</script>

<style scoped>
.invite-crew {
  padding: 1rem;
  max-width: 500px;
  margin: auto;
}
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
input, select {
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
.success {
  margin-top: 1rem;
  color: green;
}
.error {
  margin-top: 1rem;
  color: red;
}
</style>
