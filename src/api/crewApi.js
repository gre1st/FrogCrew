// src/api/crewApi.js
import axiosInstance from '@/services/AxiosService'

/* ───────── base calls ───────── */

export async function getAllCrewMembers () {
  const res = await axiosInstance.get('/crewMember')
  return res.data.data
}

export async function getCrewMemberById (userId) {
  const res = await axiosInstance.get(`/crewMember/${userId}`)
  return res.data.data
}

export async function getCurrentCrewMember () {
  const res = await axiosInstance.get('/crewMember/me')
  return res.data.data
}

export async function createCrewMember (data) {
  const res = await axiosInstance.post('/crewMember', data)
  return res.data.data
}

export async function updateCrewMember (userId, data) {
  const res = await axiosInstance.put(`/crewMember/${userId}`, data)
  return res.data.data
}

/* ───────── delete helper ───────── */
export async function deleteCrewMember (userId) {
  return axiosInstance.delete(`/crewMember/${userId}`)
}

/* ───────── filter helper ───────── */
export async function getCrewByPosition (position) {
  const res = await axiosInstance.get('/crewMember', { params: { position } })
  return res.data.data
}

/* ───────── bundled object ───────── */
export const CrewApi = {
  getAllCrewMembers,
  getCrewMemberById,
  getCurrentCrewMember,
  createCrewMember,
  updateCrewMember,
  deleteCrewMember,  
  getCrewByPosition
}

export default CrewApi