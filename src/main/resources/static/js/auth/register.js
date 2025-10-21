document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("registerForm");
  const feedback = document.getElementById("registerMessage");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    feedback.innerHTML = "";

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirmPassword").value;

    if (!email || !password || !confirm) {
      feedback.innerHTML = `<p class="text-danger">Kérlek, töltsd ki az összes mezőt!</p>`;
      return;
    }

    if (password !== confirm) {
      feedback.innerHTML = `<p class="text-danger">A két jelszó nem egyezik!</p>`;
      return;
    }

    try {
      const res = await fetch("/auth/register", {
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

      // Mentjük a JWT-t és a user adatait (ha a backend visszaadja)
      if (data.token) {
        localStorage.setItem("token", data.token);
      }

      localStorage.setItem(
        "user",
        JSON.stringify({
          email: data.email || email,
          role: data.role || "USER"
        })
      );

      feedback.innerHTML = `<p class="text-success">✅ Sikeres regisztráció! Átirányítás...</p>`;
      setTimeout(() => (window.location.href = "/"), 1500);
    } catch (err) {
      console.error(err);
      feedback.innerHTML = `<p class="text-danger">⚠️ Hálózati hiba, próbáld újra!</p>`;
    }
  });
});


