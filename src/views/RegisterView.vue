<template>
  <div class="register-page">
    <div class="register-card">
      <CrewRegisterForm />
    </div>
  </div>
</template>

<script setup>
import CrewRegisterForm from '@/components/CrewRegisterForm.vue'
</script>

<style scoped>
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  margin: 0;
  padding: 1rem;
  background-color: #f3e5f5; /* light purple */
  box-sizing: border-box;
}

.register-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 500px;
}
</style>
