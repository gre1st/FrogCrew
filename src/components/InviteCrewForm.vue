<template>
  <div class="invite-crew">
    <h2>Invite New Crew Member</h2>

    <form @submit.prevent="submit">
      <div class="field">
        <label>First Name</label>
        <input v-model.trim="form.firstName" required />
      </div>

      <div class="field">
        <label>Last Name</label>
        <input v-model.trim="form.lastName" required />
      </div>

      <div class="field">
        <label>Email</label>
        <input v-model.trim="form.email" type="email" required />
      </div>

      <div class="field">
        <label>Role</label>
        <select v-model="form.role" required>
          <option disabled value="">-- Select Role --</option>
          <option value="CREW">Crew</option>
          <option value="ADMIN">Admin</option>
        </select>
      </div>

      <div class="field">
        <label>Positions (comma-separated)</label>
        <input v-model="form.positions" placeholder="Camera, Audio A1" />
      </div>

      <button type="submit">Send Invite</button>
    </form>

    <p v-if="success" class="success">{{ success }}</p>
    <p v-if="error"   class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { sendInvite } from '@/api/inviteApi'      // << the wrapper created earlier

const form = reactive({
  firstName : '',
  lastName  : '',
  email     : '',
  role      : '',
  positions : ''
})

const success = ref('')
const error   = ref('')

async function submit () {
  success.value = ''
  error.value   = ''

  try {
    // Minimal payload expected by back-end InviteController
    const payload = {
      name  : `${form.firstName} ${form.lastName}`.trim(),
      email : form.email,
      roles : [form.role]                           // you can pass more later
    }

    await sendInvite(payload)

    success.value = '✅ Invitation sent! Check the server console for the link.'
    Object.assign(form, { firstName:'', lastName:'', email:'', role:'', positions:'' })
  } catch (e) {
    console.error(e)
    error.value = '❌ Could not send invite.'
  }
}
</script>

<style scoped>
.invite-crew { max-width: 480px; margin: auto; padding: 1rem; }
.field       { display: flex; flex-direction: column; gap: .25rem; margin-bottom: .75rem; }
input,select { padding: .5rem; border: 1px solid #ccc; border-radius: 4px; }
button       { padding: .5rem 1rem; cursor: pointer; background:#4caf50; color:#fff; border:none; border-radius:4px; }
.success     { margin-top: .75rem; color: green; }
.error       { margin-top: .75rem; color: red; }
</style>
