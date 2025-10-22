document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("contactForm");
  const responseMessage = document.getElementById("responseMessage");

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    responseMessage.innerHTML = "";

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const subject = document.getElementById("subject").value.trim();
    const message = document.getElementById("message").value.trim();

    // 🧩 Frontendes validáció
    const errors = [];
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!name || name.length < 3)
      errors.push("A név legalább 3 karakter hosszú legyen.");
    if (!emailRegex.test(email))
      errors.push("Adj meg egy érvényes e-mail címet!");
    if (!subject || subject.length < 3)
      errors.push("A tárgy legalább 3 karakter legyen.");
    if (!message || message.length < 5)
      errors.push("Az üzenet legalább 5 karakter hosszú legyen.");

    if (errors.length > 0) {
      responseMessage.innerHTML = `
        <div class="alert alert-danger text-left">
          <ul style="margin-bottom: 0;">
            ${errors.map(err => `<li>${err}</li>`).join("")}
          </ul>
        </div>`;
      return;
    }

    // 📨 Küldés a backendnek
    try {
      const res = await fetch("/api/contact", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, subject, message })
      });

      // Ha a backend új JWT tokent küld vissza a vendégnek
      const authHeader = res.headers.get("Authorization");
      if (authHeader && authHeader.startsWith("Bearer ")) {
  const token = authHeader.substring(7);
  localStorage.setItem("token", token);

  // Mentjük a vendég felhasználót
  localStorage.setItem(
    "user",
    JSON.stringify({
      email,
      role: "GUEST",
      username: name || "Vendég"
    })
  );

  // 🔁 Újratöltjük az oldalt, hogy a login állapot aktiválódjon
  setTimeout(() => {
    window.location.reload();
  }, 1000);
}


      if (res.ok) {
        responseMessage.innerHTML = `
          <div class="alert alert-success text-center">
            <i class="fa fa-check-circle"></i>
            Üzenetedet sikeresen elküldtük! Hamarosan válaszolunk.
          </div>`;
        form.reset();
      } else {
        const errText = await res.text();
        responseMessage.innerHTML = `
          <div class="alert alert-danger text-center">
            <i class="fa fa-exclamation-circle"></i>
            Hiba történt az üzenet küldésekor: ${errText}
          </div>`;
      }
    } catch (error) {
      console.error(error);
      responseMessage.innerHTML = `
        <div class="alert alert-danger text-center">
          <i class="fa fa-times-circle"></i>
          Nem sikerült elküldeni az üzenetet. Próbáld újra később!
        </div>`;
    }
  });
});
