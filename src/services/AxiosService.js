import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'
});


export async function login(username, password) {
  // apply Basic auth to all future requests
  axiosInstance.defaults.auth = { username, password };

  // test against a secured endpoint; throws if 401
  await axiosInstance.get('/crewMember');
}

export function getCrewMember(id) {
  return axiosInstance.get(`/crewMember/${id}`);
}

export default axiosInstance;
