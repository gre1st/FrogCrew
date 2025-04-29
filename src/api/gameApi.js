// src/api/gameApi.js

import axiosInstance from '@/services/AxiosService'

/**
 * Fetch all game entries across all schedules
 */
export async function getAllGames() {
  const res = await axiosInstance.get('/gameSchedule/games')
  return res.data.data
}

/**
 * Create a new game schedule
 */
export async function createGameSchedule(scheduleData) {
  const res = await axiosInstance.post('/gameSchedule', scheduleData)
  return res.data.data
}

/**
 * Add one or more games to an existing schedule
 */
export async function addGamesToSchedule(scheduleId, gamesPayload) {
  const res = await axiosInstance.post(
    `/gameSchedule/${scheduleId}/games`,
    gamesPayload
  )
  return res.data.data
}

/**
 * Alias for addGamesToSchedule to support singular import
 */
export const addGameToSchedule = addGamesToSchedule

/**
 * Bundled API methods
 */
export const GameApi = {
  getAllGames,
  createGameSchedule,
  addGamesToSchedule,
  addGameToSchedule
}

export default GameApi
