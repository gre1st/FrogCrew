<!-- src/views/ProfileView.vue -->
<template>
  <div class="profile-page">
    <div class="profile-card">
      <h1>Profile</h1>
      <CrewProfileForm />
    </div>
  </div>
</template>

<script setup>
import CrewProfileForm from '@/components/CrewProfileForm.vue'
</script>

<style scoped>
/* full-bleed light purple background */
.profile-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  margin: 0;
  padding: 1rem;                  /* small padding for mobile */
  background-color: #f3e5f5;      /* same light purple */
  box-sizing: border-box;
}

/* white card around the form */
.profile-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;               /* constrain width on larger screens */
  text-align: center;             /* center all text inside */
}

/* dark purple heading */
.profile-card h1 {
  color: #4a148c;
  margin-bottom: 1.5rem;
}


</style>
