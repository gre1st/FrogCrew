<template>
    <div class="register-form">
      <h2>Register</h2>
      <form @submit.prevent="register">
        <div>
          <label>First Name:</label>
          <input v-model="form.firstName" required />
        </div>
        <div>
          <label>Last Name:</label>
          <input v-model="form.lastName" required />
        </div>
        <div>
          <label>Email:</label>
          <input v-model="form.email" type="email" required />
        </div>
        <div>
          <label>Phone Number (e.g. 123-456-7890):</label>
          <input v-model="form.phoneNumber" required pattern="\d{3}-\d{3}-\d{4}" />
        </div>
        <div>
          <label>Password:</label>
          <input v-model="form.password" type="password" required />
        </div>
        <div>
          <label>Positions (comma-separated):</label>
          <input v-model="form.positions" required placeholder="e.g. Camera, Graphics Operator" />
        </div>
        <button type="submit">Register</button>
  
        <div v-if="successMessage" class="success">{{ successMessage }}</div>
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
      </form>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue'
  import { CrewApi } from '@/api/crewApi'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const form = reactive({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
    positions: ''
  })
  
  const successMessage = ref('')
  const errorMessage = ref('')
  
  async function register() {
    successMessage.value = ''
    errorMessage.value = ''
  
    const phoneRegex = /^\d{3}-\d{3}-\d{4}$/
    if (!phoneRegex.test(form.phoneNumber)) {
      alert('Phone number must be in format 999-999-9999')
      return
    }
  
    try {
      const newUser = {
        username: form.email, // 🚀 New: required by backend
        email: form.email,
        password: form.password, // 🚀 New: must send password
        phoneNumber: form.phoneNumber,
        firstName: form.firstName,
        lastName: form.lastName,
        role: 'CREW', // Always 'crew'
        qualifiedPositions: form.positions.split(',').map(p => p.trim()) // 🚀 Fixed field name
      }
  
      await CrewApi.createCrewMember(newUser)
      successMessage.value = '✅ Registration successful! Redirecting to login...'
  
      setTimeout(() => {
        router.push('/') // Redirect to login
      }, 1500)
    } catch (error) {
      console.error(error)
      if (error.response && error.response.data && error.response.data.message) {
        errorMessage.value = `❌ ${error.response.data.message}`
      } else {
        errorMessage.value = '❌ Registration failed. Please check your inputs.'
      }
    }
  }
  </script>
  
  <style scoped>
  .register-form {
    max-width: 500px;
    margin: auto;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  label {
    font-weight: bold;
  }
  input {
    padding: 0.5rem;
    border-radius: 4px;
    border: 1px solid #ccc;
  }
  button {
    padding: 0.5rem;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .success {
    margin-top: 1rem;
    color: green;
  }
  .error {
    margin-top: 1rem;
    color: red;
  }
  </style>
  