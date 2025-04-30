<template>
  <!-- hide navigation when there is no role *or* we are on an auth‑only page -->
  <nav v-if="showNav">
    <div>
      <!-- Crew navigation (no Trade Board link) -->
      <template v-if="roleLower === 'crew'">
        <router-link to="/dashboard">Crew Dashboard</router-link> |
        <router-link to="/profile">Profile</router-link> |
        <router-link to="/availability">Availability</router-link> |
        <router-link to="/schedule">Schedule</router-link> |
        <router-link to="/crew-list">Crew List</router-link>
      </template>

      <!-- Admin navigation -->
      <template v-else-if="roleLower === 'admin'">
        <router-link to="/admin-dashboard">Admin Dashboard</router-link> |
        <router-link to="/schedule">Schedule</router-link> |
        <router-link to="/admin/crew">Manage Crew</router-link> |
        <router-link to="/admin/reports">Reports</router-link> |
        <router-link to="/invite">Invite</router-link> |
      </template>

      <button @click="logout">Logout</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, watchEffect, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

/* ----------------------------------------------------
 * reactive role (keeps in sync with localStorage)
 * --------------------------------------------------*/
const role = ref(localStorage.getItem('role') ?? '')

watchEffect(() => {
  role.value = localStorage.getItem('role') ?? ''
})

// lower‑cased helper so comparisons are case‑insensitive
const roleLower = computed(() => role.value.toLowerCase())

/* ----------------------------------------------------
 * show / hide logic
 * --------------------------------------------------*/
// pages where the navbar should *not* appear
const hiddenPaths = ['/register', '/login']

const showNav = computed(() => {
  return !!role.value && !hiddenPaths.includes(route.path)
})

/* ----------------------------------------------------
 * actions
 * --------------------------------------------------*/
function logout () {
  localStorage.clear()
  router.push('/')
}
</script>

<style scoped>
nav {
  background-color: #e1bee7;
  padding: 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
nav a {
  margin-right: 1rem;
  text-decoration: none;
  color: #333;
}
button {
  background-color: red;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
}
</style>
