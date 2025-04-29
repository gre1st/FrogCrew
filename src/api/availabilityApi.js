// src/api/availabilityApi.js

import axiosInstance from '@/services/AxiosService'

export async function submitAvailability(availabilityData) {
  const res = await axiosInstance.post('/availability', availabilityData)
  return res.data.data
}

export async function updateAvailability(availabilityData) {
  const res = await axiosInstance.put('/availability', availabilityData)
  return res.data.data
}

export async function getAvailabilityByUserAndSchedule(userId, scheduleId) {
  const res = await axiosInstance.get(
    `/availability/${userId}/schedule/${scheduleId}`
  )
  return res.data.data
}

export async function getAvailabilityByUserAndSeason(userId, season) {
  const res = await axiosInstance.get(
    `/availability/${userId}/season/${season}`
  )
  return res.data.data
}

/** Named export for backwards compatibility */
export const AvailabilityApi = {
  submitAvailability,
  updateAvailability,
  getAvailabilityByUserAndSchedule,
  getAvailabilityByUserAndSeason
}

export default AvailabilityApi
