<template>
  <div class="admin-crew">
    <h2>Manage Crew Members</h2>

    <div v-if="role !== 'admin'">
      <p>🚫 You do not have permission to view this page.</p>
      <router-link to="/dashboard">Go Back</router-link>
    </div>

    <div v-else>
      <!-- <button @click="createNewMember" class="new-btn">+ Add New Crew Member</button> -->

      <label>Filter by Role or Position:</label>
      <input v-model="roleFilter" placeholder="e.g. crew, admin, Camera" />

      <table v-if="filteredCrew.length">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Qualified Positions</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in filteredCrew" :key="member.id">
            <td>{{ member.firstName }} {{ member.lastName }}</td>
            <td>{{ member.email }}</td>
            <td>{{ member.phoneNumber }}</td>
            <td>{{ member.role }}</td>
            <td>{{ member.positions?.join(', ') }}</td>
            <td>
              <button v-if="member.role === 'CREW'" @click="editMember(member)">Edit</button>
              <button v-if="member.role === 'CREW'" @click="onDelete(member)">Delete</button>
              <span v-else style="color:#999">🔒 no edit</span>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>No crew members match your filter.</p>

      <!-- modal -->
      <div v-if="editing" class="modal">
        <div class="modal-content">
          <h3>{{ isNew ? 'New Crew Member' : 'Edit Crew Member' }}</h3>
          <form @submit.prevent="saveMember">
            <input v-model="editForm.firstName" placeholder="First Name" required />
            <input v-model="editForm.lastName"  placeholder="Last Name"  required />
            <input v-model="editForm.email"     placeholder="Email"      type="email" required />
            <input v-model="editForm.phoneNumber" placeholder="Phone Number" pattern="\d{3}-\d{3}-\d{4}" required />
            <select v-model="editForm.role" required>
              <option value="" disabled>Select Role</option>
              <option value="CREW">Crew</option>
            </select>
            <input v-model="positionsInput" placeholder="Qualified Positions (comma-separated)" required />
            <div class="modal-actions">
              <button type="submit">Save</button>
              <button type="button" @click="cancelEdit">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import {
  getAllCrewMembers,
  createCrewMember,
  updateCrewMember,
  deleteCrewMember
} from '@/api/crewApi'

const role = localStorage.getItem('role')

/* ── state ─────────────────── */
const crew         = ref([])
const roleFilter   = ref('')
const editing      = ref(false)
const isNew        = ref(false)
const positionsInput = ref('')

const editForm = reactive({
  id: null,
  firstName: '',
  lastName:  '',
  email:     '',
  phoneNumber: '',
  role: '',
  positions: []
})

/* ── computed ──────────────── */
const filteredCrew = computed(() => {
  const term = roleFilter.value.trim().toLowerCase()
  if (!term) return crew.value
  return crew.value.filter(m =>
    m.role.toLowerCase().includes(term) ||
    (m.positions || []).some(p => p.toLowerCase().includes(term))
  )
})

/* ── crud helpers ──────────── */
async function loadCrew() {
  try { crew.value = await getAllCrewMembers() }
  catch (err) { console.error('Failed to load crew', err) }
}

function editMember(m) {
  Object.assign(editForm, m)
  positionsInput.value = (m.positions || []).join(', ')
  isNew.value = false
  editing.value = true
}

function createNewMember() {
  Object.assign(editForm, { id:null, firstName:'', lastName:'', email:'', phoneNumber:'', role:'', positions:[] })
  positionsInput.value = ''
  isNew.value = true
  editing.value = true
}

async function saveMember() {
  try {
    const payload = {
      firstName: editForm.firstName,
      lastName:  editForm.lastName,
      email:     editForm.email,
      phoneNumber: editForm.phoneNumber,
      role:      editForm.role,
      positions: positionsInput.value.split(',').map(s => s.trim()).filter(Boolean)
    }

    if (isNew.value) {
      const newMember = await createCrewMember(payload)
      crew.value.push(newMember)
    } else {
      const updated = await updateCrewMember(editForm.id, payload)
      const idx = crew.value.findIndex(c => c.id === updated.id)
      if (idx !== -1) crew.value[idx] = updated
    }
    editing.value = false
  } catch (err) {
    console.error('Failed to save member', err)
    alert('Could not save crew member')
  }
}

function cancelEdit() { editing.value = false }

async function onDelete(member) {
  if (!confirm(`Delete ${member.firstName} ${member.lastName}?`)) return
  try {
    await deleteCrewMember(member.id)
    crew.value = crew.value.filter(c => c.id !== member.id)
  } catch (err) {
    console.error('Delete failed', err)
    alert('Could not delete crew member.')
  }
}

onMounted(loadCrew)
</script>

<style scoped>
.admin-crew { padding: 1rem; }
.new-btn    { margin-bottom:1rem; background:#4caf50; color:#fff; border:none; padding:0.5rem 1rem; border-radius:4px; cursor:pointer; }
table       { width:100%; border-collapse:collapse; margin:1rem 0; }
th, td      { border:1px solid #ccc; padding:0.5rem; }
th          { background:#f4f4f4; }
.modal      { position:fixed; inset:0; background:rgba(0,0,0,0.5); display:flex; align-items:center; justify-content:center; }
.modal-content { background:#fff; padding:2rem; border-radius:8px; width:90%; max-width:400px; }
.modal-actions { margin-top:1rem; display:flex; justify-content:space-between; }
input, select { width:100%; margin:0.5rem 0; padding:0.5rem; }
button      { padding:0.4rem 0.8rem; border:none; border-radius:4px; cursor:pointer; }
</style>
