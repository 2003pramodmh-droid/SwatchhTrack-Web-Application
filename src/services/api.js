const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export async function apiRequest(path, options = {}, token) {
  const headers = options.headers ? { ...options.headers } : {};
  if (!options.body || !(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers
  });

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || 'Request failed');
  }
  return data;
}
