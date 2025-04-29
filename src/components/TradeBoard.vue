<template>
    <div class="trade-board">
      <h2>Available Shifts to Pick Up</h2>
  
      <div v-if="role !== 'crew'">
        <p>🚫 Only crew members can access the trade board.</p>
      </div>
  
      <div v-else>
        <table>
          <thead>
            <tr>
              <th>Game</th>
              <th>Position</th>
              <th>Status</th>
              <th>Dropper</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="trade in tradeBoard" :key="trade.tradeId">
              <td>{{ trade.gameDate }} - {{ trade.opponent }} @ {{ trade.venue }}</td>
              <td>{{ trade.position }}</td>
              <td>{{ trade.status }}</td>
              <td>{{ trade.dropperName }}</td>
              <td>
                <button 
                  v-if="trade.status === 'Available'" 
                  @click="pickupShift(trade.tradeId)">
                  Request Pickup
                </button>
                <span v-else>Already requested</span>
              </td>
            </tr>
          </tbody>
        </table>
  
        <div v-if="successMessage" class="success">{{ successMessage }}</div>
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { TradeBoardApi } from '../api/tradeBoardApi'
  
  const role = localStorage.getItem('role')
  const userId = localStorage.getItem('userId') // Assuming you store this at login!
  
  const tradeBoard = ref([])
  const successMessage = ref('')
  const errorMessage = ref('')
  
  async function loadTradeBoard() {
    try {
      const data = await TradeBoardApi.getAllTradeBoards()
      tradeBoard.value = data
    } catch (error) {
      console.error('Failed to load trade board:', error)
    }
  }
  
  async function pickupShift(tradeId) {
    successMessage.value = ''
    errorMessage.value = ''
  
    try {
      await TradeBoardApi.requestPickup(tradeId, userId)
      successMessage.value = '✅ Pickup request sent!'
      await loadTradeBoard() // Refresh the list
    } catch (error) {
      console.error(error)
      errorMessage.value = '❌ Failed to request pickup.'
    }
  }
  
  onMounted(() => {
    if (role === 'crew') {
      loadTradeBoard()
    }
  })
  </script>
  
  <style scoped>
  .trade-board {
    padding: 1rem;
  }
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  th, td {
    border: 1px solid #ccc;
    padding: 0.75rem;
    text-align: left;
  }
  th {
    background: #f4f4f4;
  }
  button {
    background-color: #4caf50;
    color: white;
    padding: 0.4rem 0.8rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .success {
    color: green;
    margin-top: 1rem;
  }
  .error {
    color: red;
    margin-top: 1rem;
  }
  </style>
  