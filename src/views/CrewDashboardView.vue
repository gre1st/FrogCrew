<!-- src/views/CrewDashboardView.vue -->
<template>
  <div class="dashboard-page">
    <div class="dashboard-card">
      <h1 class="dashboard-title">Crew Dashboard</h1>
      <ul class="dashboard-links">
        <li>
          <router-link to="/profile">Update Profile</router-link>
        </li>
        <li>
          <router-link to="/availability">Submit Availability</router-link>
        </li>
        <li>
          <router-link to="/schedule">View Schedule</router-link>
        </li>
        <li>
          <router-link to="/crew-list">View Crew List</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
/* full-bleed purple background, center everything */
.dashboard-page {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  min-height: 100vh;
  background-color: #f3e5f5;
  box-sizing: border-box;
  margin: 0;
}

/* white card wrapper */
.dashboard-card {
  background: #fff;
  padding: 2.5rem 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
  text-align: center;
}

/* dark-purple heading */
.dashboard-title {
  margin-bottom: 1.5rem;
  color: #4a148c;
  font-size: 2rem;
}

/* list resets */
.dashboard-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

/* spacing */
.dashboard-links li + li {
  margin-top: 1rem;
}

/* styled links */
.dashboard-links a {
  font-size: 1.25rem;
  font-weight: 500;
  color: #7e57c2;
  text-decoration: none;
  transition: color 0.2s;
}

.dashboard-links a:hover {
  color: #4a148c;
  text-decoration: underline;
}
</style>
