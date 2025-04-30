// src/api/inviteApi.js
import axios from "@/services/AxiosService";

export function sendInvite(payload) {
  // payload = { name, email, roles }
  return axios.post("/invites", payload);
}

export function fetchInvite(token) {
  return axios.get(`/invites/${token}`);
}
