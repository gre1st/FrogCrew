<template>
  <div class="invite-page">
    <div class="invite-card">
      <h1>Invite Crew</h1>
      <InviteCrewForm />
    </div>
  </div>
</template>

<script setup>
import InviteCrewForm from '@/components/InviteCrewForm.vue'
</script>

<style scoped>
.invite-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  margin: 0;
  padding: 1rem;
  background-color: #f3e5f5; /* light purple */
  box-sizing: border-box;
}

.invite-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 500px;
  text-align: center;
}

.invite-card h1 {
  color: #4a148c;          /* dark purple */
  margin: 0 0 1.5rem;
  font-size: 1.75rem;
}
</style>
