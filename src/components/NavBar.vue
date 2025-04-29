<template>
  <nav>
    <div v-if="role">
      <template v-if="role === 'crew'">
        <router-link to="/dashboard">Crew Dashboard</router-link> |
        <router-link to="/profile">Profile</router-link> |
        <router-link to="/availability">Availability</router-link> |
        <router-link to="/schedule">Schedule</router-link> |
        <router-link to="/crew-list">Crew List</router-link> |
        <router-link to="/tradeboard">Trade Board</router-link> |
      </template>

      <template v-else-if="role === 'admin'">
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
import { ref, watchEffect } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const role = ref(localStorage.getItem('role'))

watchEffect(() => {
  role.value = localStorage.getItem('role')
})

function logout() {
  localStorage.clear()
  router.push('/')
}
</script>

<style scoped>
nav {
  background-color: #f0f0f0;
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
