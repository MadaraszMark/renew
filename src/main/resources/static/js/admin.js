document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");
  const userData = localStorage.getItem("user");
  const tableBody = document.querySelector("#adminMessages tbody");
  const errorDiv = document.getElementById("adminError");

  // 🔒 Ha nincs token vagy felhasználói adat → átirányítás login oldalra
  if (!token || !userData) {
    alert("🔐 Ehhez az oldalhoz be kell jelentkezned!");
    window.location.href = "/auth/login.html";
    return;
  }

  const user = JSON.parse(userData);

  // 🔒 Ha nem admin → nincs jogosultság
  if (user.role !== "ADMIN") {
    alert("🚫 Nincs jogosultságod az admin felülethez!");
    window.location.href = "/";
    return;
  }

  try {
    // 🔹 Adatok lekérése a backendtől
    const response = await fetch("/api/contact", {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });

    if (!response.ok) {
      if (response.status === 403) {
        throw new Error("🚫 Hozzáférés megtagadva – csak adminok férhetnek hozzá!");
      } else if (response.status === 401) {
        throw new Error("🔑 A munkamenet lejárt, jelentkezz be újra!");
      } else {
        throw new Error(`Hiba a szerver válaszában (${response.status})`);
      }
    }

    // 🔹 JSON feldolgozás
    const data = await response.json();
    const messages = data.content || data; // ha Page objektum, akkor .content

    // 🔹 Adatellenőrzés
    if (!Array.isArray(messages)) {
      console.error("Nem tömb típusú adat:", data);
      throw new Error("Hibás válaszformátum érkezett a szervertől.");
    }

    // 🔹 Tábla törlése és feltöltése
    tableBody.innerHTML = "";

    if (messages.length === 0) {
      tableBody.innerHTML = `
        <tr><td colspan="6" class="text-center text-muted py-4">
          <i class="fa fa-info-circle"></i> Nincsenek üzenetek.
        </td></tr>`;
      return;
    }

    messages.forEach((msg, index) => {
      const date = msg.createdAt
        ? new Date(msg.createdAt).toLocaleString("hu-HU")
        : "-";
      const row = `
        <tr>
          <td>${index + 1}</td>
          <td>${msg.name}</td>
          <td>${msg.email}</td>
          <td>${msg.subject || "-"}</td>
          <td>${msg.message}</td>
          <td>${date}</td>
        </tr>
      `;
      tableBody.insertAdjacentHTML("beforeend", row);
    });

  } catch (error) {
    console.error("Admin üzenetbetöltési hiba:", error);
    errorDiv.innerHTML = `<p class="text-danger">${error.message}</p>`;
  }
});
