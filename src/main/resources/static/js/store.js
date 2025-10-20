(function () {
  const productList = document.getElementById("product-list");

  // === 1) Termék kártya ===
  function card(l) {
    const ar = (l.ar ?? 0).toLocaleString("hu-HU");
    const os = l.operatingSystemName || "—";
    const title = `${l.gyarto || ""} ${l.tipus || ""}`.trim();
    return `
      <div class="col-md-4 col-xs-6">
        <div class="product" style="cursor:pointer;" onclick="showProductDetails(${l.id})">
          <div class="product-img">
            <img src="/img/${l.id}.png" alt="${l.tipus}"
                 onerror="this.src='/img/logo.png'">
            <div class="product-label"><span class="new">NEW</span></div>
          </div>
          <div class="product-body">
            <p class="product-category">${os}</p>
            <h3 class="product-name">${title}</h3>
            <h4 class="product-price">${ar} Ft</h4>
            <div class="product-rating">
              <i class="fa fa-star"></i><i class="fa fa-star"></i>
              <i class="fa fa-star"></i><i class="fa fa-star"></i>
              <i class="fa fa-star-o"></i>
            </div>
            <div class="product-btns">
              <button class="add-to-wishlist">
                <i class="fa fa-heart-o"></i>
                <span class="tooltipp">Kívánságlistára</span>
              </button>
            </div>
          </div>
          <div class="add-to-cart">
            <button class="add-to-cart-btn">
              <i class="fa fa-shopping-cart"></i> Kosárba
            </button>
          </div>
        </div>
      </div>`;
  }

  // === 2) Termékek betöltése ===
  async function load() {
    try {
      const res = await fetch("/api/laptops?page=0&size=12");
      if (!res.ok) throw new Error(`Szerver hiba: ${res.status}`);
      const data = await res.json();

      const list = data?.content ?? [];
      if (!Array.isArray(list) || list.length === 0) {
        productList.innerHTML = `
          <div class="col-md-12 text-center" style="padding: 50px 0;">
            <h4>Jelenleg nincs megjeleníthető termék.</h4>
            <p>A termékek feltöltés alatt állnak. Kérjük, nézz vissza később!</p>
          </div>`;
        return;
      }

      productList.innerHTML = list.map(card).join("");
    } catch (err) {
      console.error(err);
      productList.innerHTML = `
        <div class="col-md-12 text-center" style="padding: 50px 0;">
          <h4>Hiba történt a termékek betöltésekor!</h4>
          <p>${err.message}</p>
        </div>`;
    }
  }

  // === 3) Termék részletei (Bootstrap modal) ===
  window.showProductDetails = function (id) {
    fetch(`/api/laptops/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Nem sikerült betölteni a részleteket");
        return res.json();
      })
      .then((l) => {
        document.getElementById("modalTitle").textContent = `${l.gyarto} ${l.tipus}`;
        document.getElementById("modalCpu").textContent = l.processorName || "–";
        document.getElementById("modalOs").textContent = l.operatingSystemName || "–";
        document.getElementById("modalDisplay").textContent = l.kijelzo || "–";
        document.getElementById("modalRam").textContent = l.memoria || "–";
        document.getElementById("modalStorage").textContent = l.merevlemez || "–";
        document.getElementById("modalGpu").textContent = l.videoezelo || "–";
        document.getElementById("modalStock").textContent = l.db || "–";
        document.getElementById("modalPrice").textContent = `${(l.ar ?? 0).toLocaleString("hu-HU")} Ft`;
        document.getElementById("modalImage").src = `/img/${l.id}.png`;

        $("#productModal").modal("show");
      })
      .catch((err) => {
        alert("Hiba: " + err.message);
        console.error(err);
      });
  };

  document.addEventListener("DOMContentLoaded", load);
})();

