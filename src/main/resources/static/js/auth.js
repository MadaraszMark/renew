// auth.js
function getAuthHeaders() {
  const token = localStorage.getItem("token");
  return token ? { "Authorization": `Bearer ${token}` } : {};
}

// Segédfüggvény fetch-hez, ami automatikusan hozzáadja a JWT-t
async function authFetch(url, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(url, { ...options, headers });

  if (res.status === 401 || res.status === 403) {
    alert("⚠️ Nincs jogosultságod vagy lejárt a munkamenet. Jelentkezz be újra!");
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/auth/login.html";
    return null;
  }

  return res;
}
