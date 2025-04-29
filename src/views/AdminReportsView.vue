<template>
    <div>
      <h1>Admin Reports + Schedule Tools</h1>
  
      <!-- Section 1: Create a New Schedule -->
      <CreateGameScheduleForm />
  
      <hr />
  
      <!-- Section 2: Add Game to Existing Schedule -->
      <AddGameToScheduleForm />
    </div>
  </template>
  
  <script setup>
  import CreateGameScheduleForm from '../components/CreateGameScheduleForm.vue'
  import AddGameToScheduleForm from '../components/AddGameToScheduleForm.vue'
  </script>
  