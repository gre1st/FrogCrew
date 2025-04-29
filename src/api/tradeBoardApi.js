// src/api/tradeBoardApi.js

import axiosInstance from '@/services/AxiosService'

export async function dropShift(tradeBoardData) {
  const res = await axiosInstance.post('/scheduledGames/drop', tradeBoardData)
  return res.data.data
}

export async function requestPickup(tradeId, userId) {
  const res = await axiosInstance.put(
    `/scheduledGames/pickup/${tradeId}/${userId}`
  )
  return res.data.data
}

export async function approveSwap(tradeId) {
  const res = await axiosInstance.put(`/scheduledGames/approve/${tradeId}`)
  return res.data.data
}

export async function denySwap(tradeId) {
  const res = await axiosInstance.put(`/scheduledGames/deny/${tradeId}`)
  return res.data.data
}

export async function getAllTradeBoards() {
  const res = await axiosInstance.get('/scheduledGames/tradeboard')
  return res.data.data
}

export async function findGamesByUserId(userId) {
  const res = await axiosInstance.get(`/scheduledGames/get/${userId}`)
  return res.data.data
}

/** Named export for backwards compatibility **/
export const TradeBoardApi = {
  dropShift,
  requestPickup,
  approveSwap,
  denySwap,
  getAllTradeBoards,
  findGamesByUserId
}

export default TradeBoardApi
