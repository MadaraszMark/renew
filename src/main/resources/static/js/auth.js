// ---------------------------
// 🔽 Menü megjelenítés jogosultság alapján
// ---------------------------
document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  const menuMessages = document.getElementById("menuMessages");
  const menuAdmin = document.getElementById("menuAdmin");

  // alapállapot: minden rejtve
  if (menuMessages) menuMessages.style.display = "none";
  if (menuAdmin) menuAdmin.style.display = "none";

  if (!token) {
    return; // nincs bejelentkezve
  }

  try {
    // JWT payload kinyerése biztonságosan
    const payload = JSON.parse(atob(token.split(".")[1]));
    const role = payload.role || payload.roles || "";

    // USER szerepkör → csak Üzenetek
    if (role === "USER" && menuMessages) {
      menuMessages.style.display = "inline-block";
    }

    // ADMIN szerepkör → Admin menü megjelenítése (nem közvetlen link!)
    if (role === "ADMIN" && menuAdmin) {
      menuAdmin.style.display = "inline-block";
      menuAdmin.innerHTML = `<a href="#" id="openAdmin"><i class="fa fa-cog"></i> Admin</a>`;
      
      // JS-ből kezeljük a navigációt
      const openAdminBtn = document.getElementById("openAdmin");
      if (openAdminBtn) {
        openAdminBtn.addEventListener("click", (e) => {
          e.preventDefault();
          window.location.href = "/admin.html"; // vagy /admin ha Controllerből jön
        });
      }
    }

  } catch (error) {
    console.error("JWT dekódolási hiba:", error);
  }
});

// ---------------------------
// 🔒 Admin oldal védelme – ha valaki közvetlenül beírja az URL-t
// ---------------------------
if (window.location.pathname === "/admin.html" || window.location.pathname === "/admin") {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("🚫 Ehhez az oldalhoz be kell jelentkezned!");
    window.location.href = "/";
  } else {
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const role = payload.role || payload.roles || "";
      if (role !== "ADMIN") {
        alert("🚫 Nincs jogosultságod az admin felülethez!");
        window.location.href = "/";
      }
    } catch (error) {
      console.error("JWT ellenőrzési hiba:", error);
      window.location.href = "/";
    }
  }
}


