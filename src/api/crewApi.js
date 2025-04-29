// src/api/crewApi.js

import axiosInstance from '@/services/AxiosService';

/** List all crew members (Admin) */
export async function getAllCrewMembers() {
  const res = await axiosInstance.get('/crewMember');
  return res.data.data;
}

/** Fetch a specific crew member by ID */
export async function getCrewMemberById(userId) {
  const res = await axiosInstance.get(`/crewMember/${userId}`);
  return res.data.data;
}

/** Fetch the currently authenticated user’s own profile */
export async function getCurrentCrewMember() {
  const res = await axiosInstance.get('/crewMember/me');
  return res.data.data;
}

/** Register a new crew member */
export async function createCrewMember(data) {
  const res = await axiosInstance.post('/crewMember', data);
  return res.data.data;
}

/** Update an existing crew member’s profile */
export async function updateCrewMember(userId, data) {
  const res = await axiosInstance.put(`/crewMember/${userId}`, data);
  return res.data.data;
}

/** Bundled API object */
export const CrewApi = {
  getAllCrewMembers,
  getCrewMemberById,
  getCurrentCrewMember,
  createCrewMember,
  updateCrewMember,
};

/** Default export for convenience */
export default CrewApi;
