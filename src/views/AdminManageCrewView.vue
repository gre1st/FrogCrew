<template>
    <div>
      <h1>Admin Crew Manager</h1>
  
      <!-- Existing component -->
      <AdminCrewManager />
  
      <hr />
  
      <!-- New: Schedule crew members for games -->
      <ScheduleCrewForm />
    </div>
  </template>
  
  <script setup>
  import AdminCrewManager from '../components/AdminCrewManager.vue'
  import ScheduleCrewForm from '../components/ScheduleCrewForm.vue'
  </script>
  