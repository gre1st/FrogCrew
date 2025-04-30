<!-- src/views/LoginView.vue -->
<template>
  <div class="login-page">
    <LoginForm />
  </div>
</template>

<script setup>
import LoginForm from '@/components/LoginForm.vue'
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;        
  height: 100vh;          
  margin: 0;            
  padding: 0;
  background-color: #f3e5f5;  
}
</style>
