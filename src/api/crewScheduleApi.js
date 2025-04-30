// src/api/crewScheduleApi.js
import axiosInstance from '@/services/AxiosService';

/**
 * Get the crew schedule (assignments) for a specific game
 */
export async function getCrewScheduleByGameId(gameId) {
  const res = await axiosInstance.get(`/crewSchedule/${gameId}`);
  return res.data.data;
}

/**
 * Get all games scheduled on (for “My Schedule” views)
 */
export async function getMyScheduledGames(userId) {
  const res = await axiosInstance.get(`/scheduledGames/get/${userId}`);
  return res.data.data;
}

/**
 * Fetch crew members qualified to cover a given position on a given game
 */
export async function getQualifiedCrew(gameId, positionName) {
  const res = await axiosInstance.get(
    `/CrewedUser/${gameId}/${encodeURIComponent(positionName)}`
  );
  return res.data.data;
}

/**
 * Assign (create) crew for a game.
 *   @PostMapping("/{gameId}")
 *   public Result addNewCrewSchedule(@PathVariable Integer gameId,
 *                                    @RequestBody List<CrewedMemberDto> crewScheduleDto)
 *
 * @param {number|string} gameId
 * @param {{ [positionId: string]: number }} assignments   map of positionId → userId
 * @returns {Promise<CrewedMemberDto[]>}
 */
export async function assignCrewToGame(gameId, assignments) {
  const payload = Object.entries(assignments).map(
    ([positionId, userId]) => ({
      userId:   Number(userId),
      position: positionId.toString()
    })
  );

  const res = await axiosInstance.post(
    `/crewSchedule/${gameId}`,
    payload
  );
  return res.data.data;
}

/**
 * Update an existing schedule
 */
export async function updateCrewSchedule(listOfCrewDtos) {
  const res = await axiosInstance.put(`/crewSchedule`, listOfCrewDtos);
  return res.data.data;
}

/**
 * Delete all assignments for a game
 */
export async function deleteCrewSchedule(gameId) {
  const res = await axiosInstance.delete(`/crewSchedule/${gameId}`);
  return res.data.data;
}

// default export for legacy imports
export default {
  getCrewScheduleByGameId,
  getMyScheduledGames,
  getQualifiedCrew,
  assignCrewToGame,
  updateCrewSchedule,
  deleteCrewSchedule
};
