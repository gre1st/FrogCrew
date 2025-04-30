<template>
  <div class="profile-form">
    <form v-if="role === 'crew'" @submit.prevent="submitForm">
      <div class="field">
        <label>First Name:</label>
        <input v-model="form.firstName" required />
      </div>

      <div class="field">
        <label>Last Name:</label>
        <input v-model="form.lastName" required />
      </div>

      <div class="field">
        <label>Email:</label>
        <!-- locked + greyed‑out so crew cannot change their login address -->
        <input v-model="form.email" type="email" readonly />
      </div>

      <div class="field">
        <label>Phone Number (e.g. 123-456-7890):</label>
        <input
          v-model="form.phoneNumber"
          required
          pattern="\d{3}-\d{3}-\d{4}"
        />
      </div>

      <div class="field">
        <label>Positions (comma-separated):</label>
        <input
          v-model="form.positions"
          required
          placeholder="e.g. Camera, Graphics Operator"
        />
      </div>

      <button type="submit">Update Profile</button>

      <div v-if="successMessage" class="success">{{ successMessage }}</div>
      <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    </form>

    <div v-else>
      <p>🚫 You must be a crew member to access this form.</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import {
  getCurrentCrewMember,
  updateCrewMember
} from '@/api/crewApi'

const role = localStorage.getItem('role')

const form = reactive({
  id: null,
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  positions: ''
})

const successMessage = ref('')
const errorMessage = ref('')

onMounted(async () => {
  if (role !== 'crew') return

  try {
    const me = await getCurrentCrewMember()

    form.id = me.id
    form.firstName = me.firstName
    form.lastName = me.lastName
    form.email = me.email
    form.phoneNumber = me.phoneNumber
    form.positions = (me.positions || []).join(', ')
  } catch (err) {
    console.error('Failed to load user profile:', err)
    errorMessage.value = 'Could not load profile.'
  }
})

async function submitForm() {
  successMessage.value = ''
  errorMessage.value = ''

  if (!/^\d{3}-\d{3}-\d{4}$/.test(form.phoneNumber)) {
    alert('Phone must be in format 999-999-9999')
    return
  }

  try {
    const updatedData = {
      firstName: form.firstName,
      lastName: form.lastName,
      email: form.email, // still send it so backend stays consistent
      phoneNumber: form.phoneNumber,
      role: 'CREW',
      positions: form.positions.split(',').map(p => p.trim())
    }

    await updateCrewMember(form.id, updatedData)
    successMessage.value = '✅ Profile updated successfully!'
  } catch (err) {
    console.error('Update failed', err)
    errorMessage.value = '❌ Failed to update profile.'
  }
}
</script>

<style scoped>
.profile-form {
  max-width: 600px;
  margin: auto;
  padding: 1rem;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  margin-bottom: 0.75rem;
}
input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
/* visually indicate read‑only fields */
input[readonly] {
  background-color: #f5f5f5;
  color: #666;
}
button {
  padding: 0.5rem 1rem;
  background: #2196f3;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.success {
  margin-top: 0.75rem;
  color: green;
}
.error {
  margin-top: 0.75rem;
  color: red;
}
</style>
