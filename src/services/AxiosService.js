import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'
});

/**
 * Call this with your username & password.
 * It applies them to every request, then pings a protected endpoint.
 * If that GET succeeds (200), your credentials are good.
 * If it fails (401), you'll get an exception.
 */
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
