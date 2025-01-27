const BASE_URL = "http://localhost:8080/api";

const headers = {
  "Content-Type": "application/json",
};

const api = {
  get: async (url) => {
    const response = await fetch(`${BASE_URL}${url}`, {
      method: "GET",
      headers,
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`GET request failed: ${response.status} ${error}`);
    }
    return await response.json();
  },

  post: async (url, data) => {
    const response = await fetch(`${BASE_URL}${url}`, {
      method: "POST",
      headers,
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`POST request failed: ${response.status} ${error}`);
    }
    return await response.json();
  },

  delete: async (url) => {
    const response = await fetch(`${BASE_URL}${url}`, {
      method: "DELETE",
      headers,
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`DELETE request failed: ${response.status} ${error}`);
    }
    return await response.json();
  },
};

export default api;
