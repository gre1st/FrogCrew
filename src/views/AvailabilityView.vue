<template>
  <div class="availability-page">
    <div class="availability-card">
      <h1>Submit Availability</h1>
      <AvailabilityForm />
    </div>
  </div>
</template>

<script setup>
import AvailabilityForm from '@/components/AvailabilityForm.vue'
</script>

<style scoped>
.availability-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  margin: 0;
  padding: 1rem;                 /* small padding on mobile */
  background-color: #f3e5f5;     /* light purple */
  box-sizing: border-box;
}

.availability-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;              /* constrain width */
}

.availability-card h1 {
  text-align: center;
  color: #4a148c;                /* dark purple */
  margin-bottom: 1.5rem;
}
</style>
