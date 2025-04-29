<template>
  <div class="profile-form">
    <form v-if="role === 'crew'" @submit.prevent="submitForm">
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
        <label>Phone Number (e.g. 123-456-7890):</label>
        <input v-model="form.phoneNumber" required pattern="\d{3}-\d{3}-\d{4}" />
      </div>

      <div>
        <label>Positions (comma-separated):</label>
        <input v-model="form.positions" required placeholder="e.g. Camera, Graphics Operator" />
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
import { getCurrentCrewMember, updateCrewMember } from '@/api/crewApi'

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
    // Fetch your profile via /crewMember/me
    const me = await getCurrentCrewMember()

    // Populate the form
    form.id          = me.id
    form.firstName   = me.firstName
    form.lastName    = me.lastName
    form.email       = me.email
    form.phoneNumber = me.phoneNumber
    form.positions   = me.positions.join(', ')
  } catch (err) {
    console.error('Failed to load user profile:', err)
    errorMessage.value = 'Could not load profile.'
  }
})

async function submitForm() {
  successMessage.value = ''
  errorMessage.value = ''

  const phoneRegex = /^\d{3}-\d{3}-\d{4}$/
  if (!phoneRegex.test(form.phoneNumber)) {
    alert('Phone must be in format 999-999-9999')
    return
  }

  try {
    const updatedData = {
      firstName:   form.firstName,
      lastName:    form.lastName,
      email:       form.email,
      phoneNumber: form.phoneNumber,
      role:        'CREW',
      positions:   form.positions.split(',').map(p => p.trim())
    }

    // Use the ID we pulled from /me
    await updateCrewMember(form.id, updatedData)

    successMessage.value = '✅ Profile updated successfully!'
  } catch (err) {
    console.error('Update failed', err)
    errorMessage.value = '❌ Failed to update profile.'
  }
}
</script>

<style scoped>
/* ... your existing styles ... */
</style>
