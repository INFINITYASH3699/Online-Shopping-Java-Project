const BASE_URL = "http://localhost:8080/api";

const api = {
  get: async (url) => {
    const response = await fetch(`${BASE_URL}${url}`);
    if (!response.ok) {
      throw new Error(`GET request failed: ${response.statusText}`);
    }
    return response.json();
  },
  post: async (url, data) => {
    const response = await fetch(`${BASE_URL}${url}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error(`POST request failed: ${response.statusText}`);
    }
    return response.json();
  },
  delete: async (url) => {
    const response = await fetch(`${BASE_URL}${url}`, { method: "DELETE" });
    if (!response.ok) {
      throw new Error(`DELETE request failed: ${response.statusText}`);
    }
    return response.json();
  },
};

export default api;
