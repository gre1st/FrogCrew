<template>
  <div class="login-page">
    <h1>Login</h1>
    <LoginForm />

    <div class="register-link">
      <p>Don't have an account? <router-link to="/register">Register here</router-link></p>
    </div>
  </div>
</template>

<script setup>
import LoginForm from '@/components/LoginForm.vue'
</script>

<style scoped>
.login-page {
  max-width: 400px;
  margin: auto;
  text-align: center;
  padding: 2rem;
}
.register-link {
  margin-top: 1rem;
}
.register-link a {
  color: #007bff;
  text-decoration: none;
}
.register-link a:hover {
  text-decoration: underline;
}
</style>
