<template>
  <div class="admin-page">
    <div class="admin-card">
      <h1>Admin Crew Manager</h1>
      <AdminCrewManager />
    </div>

    <hr />

    <div class="admin-card">
      <h1>Schedule Crew Members</h1>
      <ScheduleCrewForm />
    </div>
  </div>
</template>

<script setup>
import AdminCrewManager from '@/components/AdminCrewManager.vue'
import ScheduleCrewForm from '@/components/ScheduleCrewForm.vue'
</script>

<style scoped>
.admin-page {
  background-color: #f3e5f5; /* light purple */
  min-height: 100vh;
  padding: 2rem;
  box-sizing: border-box;
}

.admin-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 2rem;
  max-width: 1000px;
  margin: 0 auto 2rem;
}

.admin-card h1 {
  color: #4a148c;        /* dark purple */
  text-align: center;
  margin-top: 0;
  margin-bottom: 1.5rem;
}

hr {
  border: none;
  border-top: 1px solid #ccc;
  margin: 2rem 0;
}
</style>
