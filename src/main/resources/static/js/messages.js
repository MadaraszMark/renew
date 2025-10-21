document.addEventListener("DOMContentLoaded", async () => {
  const tableBody = document.querySelector("#messageTable tbody");
  const errorBox = document.getElementById("messageError");
  const token = localStorage.getItem("token");

  if (!token) {
    errorBox.innerHTML = `
      <p class="text-danger text-center mt-3">
        <i class="fa fa-lock"></i> Nincs bejelentkezve. 
        <a href="/auth/login.html" style="color:#d10024">Jelentkezz be</a>, hogy megtekinthesd az üzeneteket.
      </p>`;
    return;
  }

  try {
    const res = await fetch("/api/contact?page=0&size=50", {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) {
      if (res.status === 403 || res.status === 401) {
        throw new Error("Nincs jogosultságod az üzenetek megtekintésére.");
      }
      throw new Error(`Hiba történt a lekérdezés során: ${res.status}`);
    }

    const data = await res.json();
    const list = data.content || [];

    if (list.length === 0) {
      tableBody.innerHTML = `
        <tr><td colspan="6" class="text-center text-muted py-4">
        Nincs megjeleníthető üzenet.</td></tr>`;
      return;
    }

    tableBody.innerHTML = list.map((msg, i) => `
      <tr>
        <td>${i + 1}</td>
        <td>${msg.name || "-"}</td>
        <td>${msg.email || "-"}</td>
        <td>${msg.subject || "-"}</td>
        <td>${msg.message || "-"}</td>
        <td>${msg.createdAt ? msg.createdAt.replace("T", " ").slice(0, 19) : "-"}</td>
      </tr>
    `).join("");

  } catch (err) {
    console.error(err);
    tableBody.innerHTML = `
      <tr><td colspan="6" class="text-center text-danger py-4">
      <i class="fa fa-exclamation-circle"></i> Hiba: ${err.message}</td></tr>`;
    errorBox.innerHTML = `
      <p class="text-danger"><i class="fa fa-times-circle"></i> ${err.message}</p>`;
  }
});

