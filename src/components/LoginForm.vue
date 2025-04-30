<!-- src/components/LoginForm.vue -->
<template>
  <div class="login-page">
    <div class="login-form">
      <h2>Login</h2>

      <form @submit.prevent="handleLogin">
        <div class="field">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="you@example.com"
            required
          />
        </div>

        <div class="field">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="••••••••"
            required
          />
        </div>

        <button class="btn" type="submit" :disabled="loading">
          {{ loading ? 'Logging in…' : 'Login' }}
        </button>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

        <p class="register">
          Don’t have an account?
          <router-link to="/register">Register here</router-link>
        </p>
      </form>
    </div>
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
    axiosInstance.defaults.auth = {
      username: email.value,
      password: password.value
    }
    const res = await axiosInstance.get('/crewMember/me')
    const me = res.data.data
    localStorage.setItem('token', 'basic')
    localStorage.setItem('role', me.role.toLowerCase())
    localStorage.setItem('userId', me.id)
    router.push('/dashboard')
  } catch (err) {
    console.error(err)
    errorMessage.value = 'Login failed. Please check your credentials.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;         /* span full viewport width  */
  min-height: 100vh;    /* span full viewport height */
  background-color: #f3e5f5;
  margin: 0;            /* ensure no auto-margins shrink it */
  padding: 0;
  box-sizing: border-box;
}

.login-form {
  background: #fff;
  padding: 2.5rem 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #4a148c;
}
.field {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}
.field label {
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}
.field input {
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  transition: border-color 0.2s;
}
.field input:focus {
  outline: none;
  border-color: #7e57c2;
}
.btn {
  width: 100%;
  padding: 0.75rem;
  font-size: 1rem;
  font-weight: 600;
  color: #fff;
  background-color: #7e57c2;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.btn:disabled {
  opacity: 0.6;
  cursor: default;
}
.btn:not(:disabled):hover {
  background-color: #6a1b9a;
}
.error {
  margin-top: 1rem;
  color: #d32f2f;
  text-align: center;
}
.register {
  margin-top: 1.5rem;
  text-align: center;
  font-size: 0.9rem;
}
.register a {
  color: #7e57c2;
  text-decoration: none;
}
.register a:hover {
  text-decoration: underline;
}
</style>
