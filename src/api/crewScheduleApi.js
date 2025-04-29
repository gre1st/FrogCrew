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

// Add this function to fetch qualified crew for a given game and position
export async function getQualifiedCrew(gameId, position) {
  const res = await axiosInstance.get(
    `/CrewedUser/${gameId}/${encodeURIComponent(position)}`
  );
  return res.data.data;
}

export default {
  getCrewScheduleByGameId,
  getMyScheduledGames,
  getQualifiedCrew  // newly added
};
