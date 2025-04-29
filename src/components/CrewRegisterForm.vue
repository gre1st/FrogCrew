<template>
  <div class="register-form">
    <h2>Register</h2>

    <form @submit.prevent="register">
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
        <input v-model.trim="form.email"
               type="email"
               :readonly="hasToken"
               required />
      </div>

      <div class="field">
        <label>Phone (999-999-9999)</label>
        <input v-model.trim="form.phoneNumber"
               pattern="\d{3}-\d{3}-\d{4}"
               required />
      </div>

      <div class="field">
        <label>Password</label>
        <input v-model="form.password" type="password" required />
      </div>

      <div class="field">
        <label>Positions (comma-separated)</label>
        <input v-model="form.positions" placeholder="Camera, Graphics Op" />
      </div>

      <button type="submit">Register</button>
    </form>

    <p v-if="success" class="success">{{ success }}</p>
    <p v-if="error"   class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute, useRouter }      from 'vue-router'
import { fetchInvite }              from '@/api/inviteApi'
import { CrewApi }                  from '@/api/crewApi'

/* ───────── variables ───────── */
const route     = useRoute()
const router    = useRouter()
const token     = route.query.token       // ?token=…  if present
const hasToken  = !!token

const form = reactive({
  firstName   : '',
  lastName    : '',
  email       : '',
  phoneNumber : '',
  password    : '',
  positions   : ''
})

const success = ref('')
const error   = ref('')

/* ───── auto-fill from invite token ───── */
onMounted(async () => {
  if (!token) return

  try {
    const { data } = await fetchInvite(token)   // Result wrapper
    if (data.flag) {
      const invite = data.data                  // unwrap
      form.email = invite.email
      // If you stored positions/roles in the invite, set them here.
    } else {
      error.value = data.message || 'Invite token not found or expired.'
    }
  } catch (e) {
    console.error(e)
    error.value = 'Invite token not found or expired.'
  }
})

/* ───────── submit registration ───────── */
async function register () {
  success.value = ''
  error.value   = ''

  const phoneRegex = /^\d{3}-\d{3}-\d{4}$/
  if (!phoneRegex.test(form.phoneNumber)) {
    error.value = 'Phone must match 999-999-9999'
    return
  }

  try {
    const payload = {
      username           : form.email,
      email              : form.email,
      password           : form.password,
      phoneNumber        : form.phoneNumber,
      firstName          : form.firstName,
      lastName           : form.lastName,
      role               : 'CREW',
      qualifiedPositions : form.positions.split(',').map(p => p.trim())
    }

    await CrewApi.createCrewMember(payload)
    success.value = '✅ Registered! Redirecting to login…'
    setTimeout(() => router.push('/'), 1500)

  } catch (e) {
    console.error(e)
    // Show backend message if available
    error.value =
      e?.response?.data?.message || '❌ Registration failed.'
  }
}
</script>

<style scoped>
.register-form { max-width:500px; margin:auto; padding:1rem; }
.field         { display:flex; flex-direction:column; gap:.25rem; margin-bottom:.75rem; }
input          { padding:.5rem; border:1px solid #ccc; border-radius:4px; }
button         { padding:.5rem 1rem; background:#4caf50; color:#fff;
                 border:none; border-radius:4px; cursor:pointer; }
.success       { margin-top:.75rem; color:green; }
.error         { margin-top:.75rem; color:red; }
</style>
