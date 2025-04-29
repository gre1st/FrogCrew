// src/api/scheduleApi.js

import axiosInstance from '@/services/AxiosService'

export async function addSchedule(scheduleData) {
  const res = await axiosInstance.post('/gameSchedule', scheduleData)
  return res.data.data
}

export async function findScheduleById(scheduleId) {
  const res = await axiosInstance.get(`/gameSchedule/${scheduleId}`)
  return res.data.data
}

export async function updateSchedule(scheduleId, scheduleData) {
  const res = await axiosInstance.put(
    `/gameSchedule/${scheduleId}`,
    scheduleData
  )
  return res.data.data
}

export async function findSchedulesBySeason(season) {
  const res = await axiosInstance.get(`/gameSchedule/season/${season}`)
  return res.data.data
}

export async function getAllSports() {
  const res = await axiosInstance.get('/gameSchedule/sports')
  return res.data.data
}

export async function publishSchedule(scheduleId, scheduleData) {
  const res = await axiosInstance.put(
    `/gameSchedule/publish/${scheduleId}`,
    scheduleData
  )
  return res.data.data
}

/** Named export for backwards compatibility */
export const ScheduleApi = {
  addSchedule,
  findScheduleById,
  updateSchedule,
  findSchedulesBySeason,
  getAllSports,
  publishSchedule
}

export default ScheduleApi
