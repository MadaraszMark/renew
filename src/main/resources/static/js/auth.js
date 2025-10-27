const contextPath = window.contextPath || '/';

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

    // ADMIN → admin menü
    if (role === "ADMIN" && menuAdmin) {
      menuAdmin.style.display = "inline-block";
      menuAdmin.innerHTML = `<a href="#" id="openAdmin"><i class="fa fa-cog"></i> Admin</a>`;

      const openAdminBtn = document.getElementById("openAdmin");
      if (openAdminBtn) {
        openAdminBtn.addEventListener("click", (e) => {
          e.preventDefault();
          window.location.href = `${contextPath}admin`;
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
document.addEventListener("DOMContentLoaded", () => {
  const contextPath = window.contextPath || '/';
  if (window.location.pathname.endsWith("/admin") || window.location.pathname.endsWith("/admin.html")) {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("🚫 Ehhez az oldalhoz be kell jelentkezned!");
      window.location.href = contextPath;
      return;
    }
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const role = payload.role || payload.roles || "";
      if (role !== "ADMIN") {
        alert("🚫 Nincs jogosultságod az admin felülethez!");
        window.location.href = contextPath;
      }
    } catch (error) {
      console.error("JWT ellenőrzési hiba:", error);
      window.location.href = contextPath;
    }
  }
});



