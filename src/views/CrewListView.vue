<!-- src/views/CrewListView.vue -->
<template>
  <div class="crew-list-page">
    <div class="crew-list-card">
      <h1>Crew List Viewer</h1>
      <CrewList />
    </div>
  </div>
</template>

<script setup>
import CrewList from '@/components/CrewList.vue'
</script>

<style scoped>
.crew-list-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  margin: 0;
  padding: 2rem;
  box-sizing: border-box;
  background-color: #f3e5f5; /* light purple */
}

.crew-list-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 100%;
  max-width: 700px;      
}

.crew-list-card h1 {
  color: #4a148c;             /* dark purple */
  text-align: center;
  margin-top: 0;
  margin-bottom: 1.5rem;
}
</style>
