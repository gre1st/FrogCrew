<!-- src/views/ScheduleView.vue -->
<template>
  <div class="schedule-page">
    <h1>Game Schedule</h1>
    <GameSchedule />
  </div>
</template>

<script setup>
import GameSchedule from '@/components/GameSchedule.vue'
</script>

<style scoped>
.schedule-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  margin: 0;
  padding: 2rem;
  box-sizing: border-box;
  background-color: #f3e5f5;  /* light purple */
  text-align: center;         /* center all text */
}

.schedule-page h1 {
  color: #4a148c;             /* dark purple */
  margin-bottom: 1.5rem;
}
</style>
