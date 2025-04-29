<!-- src/components/LoginForm.vue -->
<template>
  <div class="login-form">
    <form @submit.prevent="handleLogin">
      <div>
        <label>Email:</label>
        <input v-model="email" type="email" required />
      </div>
      <div>
        <label>Password:</label>
        <input v-model="password" type="password" required />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? 'Logging in...' : 'Login' }}
      </button>
      <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axiosInstance from '@/services/AxiosService'

const router = useRouter()
const email = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')

async function handleLogin() {
  loading.value = true
  errorMessage.value = ''

  try {
    // 1) configure Basic-Auth on the shared Axios instance
    axiosInstance.defaults.auth = {
      username: email.value,
      password: password.value
    }

    // 2) verify by hitting a protected endpoint
    const res = await axiosInstance.get('/crewMember/me')
    const me = res.data.data

    // 3) store a dummy token+role for your router guard
    localStorage.setItem('token', 'basic')
    localStorage.setItem('role', me.role.toLowerCase())
    localStorage.setItem('userId', me.id)

    // 4) go to dashboard
    router.push('/dashboard')
  } catch (err) {
    console.error('Login error', err)
    errorMessage.value = 'Login failed. Please check your credentials.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
}
.error {
  margin-top: 1rem;
  color: red;
}
</style>
