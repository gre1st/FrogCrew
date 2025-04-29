// src/api/crewScheduleApi.js
import axiosInstance from '@/services/AxiosService';

export async function getCrewScheduleByGameId(gameId) {
  const res = await axiosInstance.get(`/crewSchedule/${gameId}`);
  return res.data.data;
}

export async function getMyScheduledGames(userId) {
  const res = await axiosInstance.get(`/scheduledGames/get/${userId}`);
  return res.data.data;
}

export default {
  getCrewScheduleByGameId,
  getMyScheduledGames
};
