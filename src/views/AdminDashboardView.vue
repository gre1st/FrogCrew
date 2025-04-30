<!-- src/views/AdminDashboardView.vue -->
<template>
  <div class="dashboard-page">
    <div class="dashboard-card">
      <h1 class="dashboard-title">Admin Dashboard</h1>
      <ul class="dashboard-links">
        <li>
          <router-link to="/admin/crew">Manage Crew Members</router-link>
        </li>
        <li>
          <router-link to="/admin/reports">
            Manage Reports / Schedule Games
          </router-link>
        </li>
        <li>
          <router-link to="/invite">Invite New Crew Member</router-link>
        </li>
        <li>
          <router-link to="/schedule">View Game Schedule</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 2rem;
  margin: 0;
  background-color: #f3e5f5; /* light purple */
  box-sizing: border-box;
}

.dashboard-card {
  background: #fff;
  padding: 2.5rem 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
  text-align: center;
}

.dashboard-title {
  margin-bottom: 1.5rem;
  font-size: 2rem;
  color: #4a148c; /* dark purple */
}

.dashboard-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dashboard-links li + li {
  margin-top: 1rem;
}

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
