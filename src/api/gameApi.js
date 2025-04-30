// src/api/gameApi.js
import axiosInstance from '@/services/AxiosService'

/* ───────── games across ALL schedules ───────── */

export async function getAllGames () {
  const res = await axiosInstance.get('/gameSchedule/games')
  return res.data.data
}

export async function deleteGame (scheduleId, gameId) {
  const res = await axiosInstance.delete(
    `/gameSchedule/${scheduleId}/games/${gameId}`
  )
  return res.data.data
}

/* ───────── schedule-level helpers ───────── */

export async function createGameSchedule (scheduleData) {
  const res = await axiosInstance.post('/gameSchedule', scheduleData)
  return res.data.data
}

export async function addGameToSchedule (scheduleId, payload) {
  const res = await axiosInstance.post(
    `/gameSchedule/${scheduleId}/games`,
    payload
  )
  return res.data.data
}

/* (Optional) keep update for other screens */
export async function updateGame (gameId, payload) {
  const res = await axiosInstance.put(`/games/${gameId}`, payload)
  return res.data.data
}

/* bundled export */
export const GameApi = {
  getAllGames,
  deleteGame,
  createGameSchedule,
  addGameToSchedule,
  updateGame
}

export default GameApi
