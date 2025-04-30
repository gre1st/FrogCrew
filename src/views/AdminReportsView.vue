<!-- src/views/AdminReportsView.vue -->
<template>
  <div class="reports-page">
    <!-- Top heading -->
    <h1>Admin Reports + Schedule Tools</h1>

    <!-- Create New Schedule -->
    <div class="reports-card">
      <CreateGameScheduleForm />
    </div>

    <!-- Add Game to Existing Schedule -->
    <div class="reports-card">
      <AddGameToScheduleForm />
    </div>
  </div>
</template>

<script setup>
import CreateGameScheduleForm from '@/components/CreateGameScheduleForm.vue'
import AddGameToScheduleForm    from '@/components/AddGameToScheduleForm.vue'
</script>

<style scoped>
/* full-page purple background, center everything */
.reports-page {
  min-height: 100vh;
  margin: 0;
  padding: 2rem;
  background-color: #f3e5f5; /* light purple */
  box-sizing: border-box;
  text-align: center;
}

/* main page title */
.reports-page > h1 {
  color: #4a148c;            /* dark purple */
  margin-bottom: 2rem;
}


.reports-card {
  background: #fff;
  max-width: 700px;
  margin: 0 auto 2rem;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
</style>
