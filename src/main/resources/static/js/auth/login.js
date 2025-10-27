document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const feedback = document.getElementById("loginMessage");
  const basePath = document.querySelector('base')?.getAttribute('href') || '/';

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    feedback.innerHTML = "";

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    if (!email || !password) {
      feedback.innerHTML = `<p class="text-danger">Kérlek, töltsd ki mindkét mezőt!</p>`;
      return;
    }

    try {
      const res = await fetch(`${basePath}auth/login`, {
  	method: "POST",
  	headers: { "Content-Type": "application/json" },
  	body: JSON.stringify({ email, password })
      });

      if (!res.ok) {
        const txt = await res.text();
        feedback.innerHTML = `<p class="text-danger">Hiba: ${txt}</p>`;
        return;
      }

      const data = await res.json();

      // JWT token mentése, ha van
      if (data.token) {
        localStorage.setItem("token", data.token);
      }

      // Felhasználói adatok mentése (email, role, stb.)
      localStorage.setItem(
        "user",
        JSON.stringify({
          email: data.email || email,
          role: data.role || "USER"
        })
      );

      feedback.innerHTML = `<p class="text-success">✅ Sikeres bejelentkezés!</p>`;
      setTimeout(() => (window.location.href = basePath), 1200);
    } catch (err) {
      console.error(err);
      feedback.innerHTML = `<p class="text-danger">⚠️ Hálózati hiba, próbáld újra!</p>`;
    }
  });
});


