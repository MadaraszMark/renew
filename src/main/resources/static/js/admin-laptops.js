document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");
  const userData = localStorage.getItem("user");
  const tableBody = document.getElementById("laptopTableBody");
  let form = document.getElementById("addLaptopForm");
  const errorDiv = document.getElementById("laptopError");
  const contextPath = window.contextPath || '/';

  // 🔒 Csak admin láthatja
  if (!token || !userData) {
    alert("🔐 Be kell jelentkezned az admin felülethez!");
    window.location.href = `${contextPath}login`;
    return;
  }

  const user = JSON.parse(userData);
  if (user.role !== "ADMIN") {
    alert("🚫 Nincs jogosultságod a laptopkezelő eléréséhez!");
    window.location.href = contextPath;
    return;
  }

  const headers = {
    "Authorization": "Bearer " + token,
    "Content-Type": "application/json"
  };

  // ========================
  // 🔽 Processzor + OS legördülők betöltése
  // ========================
  async function loadDropdowns() {
    const procSelect = document.getElementById("processorSelect");
    const osSelect = document.getElementById("osSelect");

    try {
      const [procRes, osRes] = await Promise.all([
        fetch(`${contextPath}api/processors`),
        fetch(`${contextPath}api/os`)
      ]);

      if (!procRes.ok || !osRes.ok) throw new Error("Nem sikerült betölteni a legördülőket.");

      const procsData = await procRes.json();
      const osData = await osRes.json();

      const procs = procsData.content || procsData;
      const osList = osData.content || osData;

      procSelect.innerHTML = '<option value="">Válassz processzort...</option>';
      procs.forEach(p => {
        procSelect.insertAdjacentHTML(
          "beforeend",
          `<option value="${p.id}">${p.gyarto} ${p.tipus}</option>`
        );
      });

      osSelect.innerHTML = '<option value="">Válassz operációs rendszert...</option>';
      osList.forEach(o => {
        osSelect.insertAdjacentHTML(
          "beforeend",
          `<option value="${o.id}">${o.nev}</option>`
        );
      });
    } catch (err) {
      console.error("❌ Legördülők betöltése sikertelen:", err);
    }
  }

  await loadDropdowns();

  // ========================
  // 🟢 LAPTOPOK BETÖLTÉSE
  // ========================
  async function loadLaptops() {
    try {
      const res = await fetch(`${contextPath}api/laptops`);
      if (!res.ok) throw new Error("Nem sikerült betölteni a laptopokat.");
      const data = await res.json();
      const laptops = data.content || data;

      tableBody.innerHTML = "";
      if (!Array.isArray(laptops) || laptops.length === 0) {
        tableBody.innerHTML = `
          <tr><td colspan="7" class="text-center text-muted py-4">
            <i class="fa fa-info-circle"></i> Nincsenek laptopok az adatbázisban.
          </td></tr>`;
        return;
      }

      laptops.forEach((l) => {
        const row = `
          <tr>
            <td>${l.id}</td>
            <td>${l.gyarto}</td>
            <td>${l.tipus}</td>
            <td>${l.kijelzo || "-"}"</td>
            <td>${l.memoria} GB</td>
            <td>${l.merevlemez || "-"} GB</td>
            <td>${l.videoVezerlo || "-"}</td>
            <td>${l.processorName || "-"}</td>
            <td>${l.operatingSystemName || "-"}</td>
            <td>${l.ar} Ft</td>
            <td>${l.db}</td>
            <td>
              <button class="btn btn-sm btn-warning me-2" onclick="editLaptop(${l.id})">
                <i class="fa fa-pencil"></i>
              </button>
              <button class="btn btn-sm btn-danger" onclick="deleteLaptop(${l.id})">
                <i class="fa fa-trash"></i>
              </button>
            </td>
          </tr>`;
        tableBody.insertAdjacentHTML("beforeend", row);
      });
    } catch (err) {
      console.error(err);
      errorDiv.innerHTML = `<p class="text-danger">${err.message}</p>`;
    }
  }

  // ========================
  // ➕ ÚJ LAPTOP HOZZÁADÁSA
  // ========================
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const newLaptop = {
      gyarto: form.gyarto.value,
      tipus: form.tipus.value,
      kijelzo: parseFloat(form.kijelzo.value),
      memoria: parseInt(form.memoria.value),
      merevlemez: parseInt(form.merevlemez.value),
      videoVezerlo: form.videoVezerlo.value || "Integrált GPU",
      ar: parseInt(form.ar.value),
      db: parseInt(form.db.value),
      processorId: parseInt(form.processorSelect.value),
      operatingSystemId: parseInt(form.osSelect.value)
    };

    try {
      const res = await fetch(`${contextPath}api/laptops`, {
        method: "POST",
        headers,
        body: JSON.stringify(newLaptop)
      });

      if (!res.ok) throw new Error("Hiba történt a laptop mentésekor.");
      form.reset();
      await loadLaptops();
      alert("✅ Laptop sikeresen hozzáadva!");
    } catch (err) {
      errorDiv.innerHTML = `<p class="text-danger">${err.message}</p>`;
    }
  });
  
  // ========================
  // ✏️ LAPTOP MÓDOSÍTÁSA
  // ========================
  window.editLaptop = async (id) => {
    try {
      const res = await fetch(`${contextPath}api/laptops/${id}`);
      if (!res.ok) throw new Error("Nem található a laptop ID: " + id);
      const l = await res.json();

      const procSelect = document.getElementById("processorSelect");
      const osSelect = document.getElementById("osSelect");

      form.gyarto.value = l.gyarto ?? "";
      form.tipus.value = l.tipus ?? "";
      form.kijelzo.value = l.kijelzo ?? "";
      form.memoria.value = l.memoria ?? "";
      form.merevlemez.value = l.merevlemez ?? "";
      form.videoVezerlo.value = l.videoVezerlo ?? "";
      form.ar.value = l.ar ?? "";
      form.db.value = l.db ?? "";
      procSelect.value = l.processorId || "";
      osSelect.value = l.operatingSystemId || "";

      const submitBtn = form.querySelector("button[type='submit']");
      submitBtn.innerHTML = '<i class="fa fa-save"></i> Mentés';
      submitBtn.classList.remove("btn-success");
      submitBtn.classList.add("btn-primary");

      const newForm = form.cloneNode(true);
      form.parentNode.replaceChild(newForm, form);

      newForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const updatedLaptop = {
          gyarto: newForm.gyarto.value,
          tipus: newForm.tipus.value,
          kijelzo: parseFloat(newForm.kijelzo.value),
          memoria: parseInt(newForm.memoria.value),
          merevlemez: parseInt(newForm.merevlemez.value),
          videoVezerlo: newForm.videoVezerlo.value,
          ar: parseInt(newForm.ar.value),
          db: parseInt(newForm.db.value),
          processorId: parseInt(newForm.processorSelect.value),
          operatingSystemId: parseInt(newForm.osSelect.value)
        };

        try {
          const updateRes = await fetch(`${contextPath}api/laptops/${id}`, {
            method: "PUT",
            headers,
            body: JSON.stringify(updatedLaptop)
          });

          if (!updateRes.ok) throw new Error("Nem sikerült frissíteni a laptopot.");

          alert("✅ Laptop sikeresen módosítva!");
          newForm.reset();

          submitBtn.innerHTML = '<i class="fa fa-plus"></i> Hozzáadás';
          submitBtn.classList.remove("btn-primary");
          submitBtn.classList.add("btn-success");

          await loadLaptops();
          form = newForm;

        } catch (err) {
          errorDiv.innerHTML = `<p class="text-danger">${err.message}</p>`;
        }
      });
    } catch (err) {
      console.error("Szerkesztési hiba:", err);
      errorDiv.innerHTML = `<p class="text-danger">${err.message}</p>`;
    }
  };

  // ========================
  // 🗑️ LAPTOP TÖRLÉSE
  // ========================
  window.deleteLaptop = async (id) => {
    if (!confirm("Biztosan törlöd ezt a laptopot?")) return;

    try {
      const res = await fetch(`${contextPath}api/laptops/${id}`, {
        method: "DELETE",
        headers
      });
      if (!res.ok) throw new Error("Hiba történt a törlés közben.");
      await loadLaptops();
    } catch (err) {
      errorDiv.innerHTML = `<p class="text-danger">${err.message}</p>`;
    }
  };

  // Első betöltés
  await loadLaptops();
});
