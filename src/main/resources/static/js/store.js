(function () {
  const productList = document.getElementById("product-list");
  const cartBadge = document.querySelector(".header-ctn .fa-shopping-cart + span + .qty");
  const cartList = document.querySelector(".cart-list");
  const cartSummary = document.querySelector(".cart-summary");

  let cart = JSON.parse(localStorage.getItem("cart") || "[]");
  let allProducts = []; // <<< EZ HIÁNYZOTT

  // === 🛒 Kosár frissítése ===
  function updateCart() {
    const totalQty = cart.reduce((sum, item) => sum + item.qty, 0);
    if (cartBadge) cartBadge.textContent = totalQty;
    localStorage.setItem("cart", JSON.stringify(cart));

    if (cart.length === 0) {
      cartList.innerHTML = `<p class="text-center text-muted">Nincs termék a kosárban</p>`;
      cartSummary.innerHTML = `<small>Összesen: 0 Ft</small>`;
      return;
    }

    let total = 0;
    cartList.innerHTML = cart
      .map(item => {
        const laptop = allProducts.find(p => p.id === item.id);
        if (!laptop) return "";
        const price = laptop.ar * item.qty;
        total += price;

        return `
          <div class="product-widget">
            <div class="product-img">
              <img src="/img/${laptop.id}.png" alt="${laptop.tipus}" onerror="this.src='/img/logo.png'">
            </div>
            <div class="product-body">
              <h3 class="product-name">${laptop.gyarto} ${laptop.tipus}</h3>
              <h4 class="product-price"><span class="qty">${item.qty}x</span> ${price.toLocaleString("hu-HU")} Ft</h4>
            </div>
            <button class="delete" onclick="removeFromCart(${laptop.id})"><i class="fa fa-close"></i></button>
          </div>
        `;
      })
      .join("");

    cartSummary.innerHTML = `
      <small>${cart.length} termék a kosárban</small>
      <h5>Összesen: ${total.toLocaleString("hu-HU")} Ft</h5>
      <button onclick="clearCart()" class="btn btn-sm btn-outline-danger w-100 mt-2">
        <i class="fa fa-trash"></i> Kosár ürítése
      </button>
    `;
  }

  // === 💳 Termék kártya ===
  function card(l) {
    const ar = (l.ar ?? 0).toLocaleString("hu-HU");
    const os = l.operatingSystemName || "—";
    const title = `${l.gyarto || ""} ${l.tipus || ""}`.trim();
    return `
      <div class="col-md-4 col-xs-6">
        <div class="product" style="cursor:pointer;" onclick="showProductDetails(${l.id})">
          <div class="product-img">
            <img src="/img/${l.id}.png" alt="${l.tipus}" onerror="this.src='/img/logo.png'">
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
          </div>
          <div class="add-to-cart">
            <button class="add-to-cart-btn" onclick="addToCart(${l.id}, event)">
              <i class="fa fa-shopping-cart"></i> Kosárba
            </button>
          </div>
        </div>
      </div>`;
  }

  // === 📦 Terméklista betöltése ===
  async function load() {
    try {
      const res = await fetch("/api/laptops?page=0&size=12");
      if (!res.ok) throw new Error(`Szerver hiba: ${res.status}`);
      const data = await res.json();
      const list = data?.content ?? [];

      allProducts = list; // <<< IDE TÖLTJÜK BE A TERMÉKEKET

      if (!Array.isArray(list) || list.length === 0) {
        productList.innerHTML = `
          <div class="col-md-12 text-center" style="padding: 50px 0;">
            <h4>Jelenleg nincs megjeleníthető termék.</h4>
            <p>A termékek feltöltés alatt állnak. Kérjük, nézz vissza később!</p>
          </div>`;
        return;
      }

      productList.innerHTML = list.map(card).join("");
      updateCart(); // <<< itt is frissítjük, ha újraindul az oldal
    } catch (err) {
      console.error(err);
      productList.innerHTML = `
        <div class="col-md-12 text-center" style="padding: 50px 0;">
          <h4>Hiba történt a termékek betöltésekor!</h4>
          <p>${err.message}</p>
        </div>`;
    }
  }

  // === 🧺 Kosár műveletek ===
  window.addToCart = function (id, e) {
    if (e) e.stopPropagation();
    const idx = cart.findIndex(item => item.id === id);
    if (idx === -1) {
      cart.push({ id, qty: 1 });
    } else {
      cart[idx].qty++;
    }
    updateCart();

    const btn = e?.target?.closest("button");
    if (btn) {
      btn.disabled = true;
      const oldText = btn.innerHTML;
      btn.innerHTML = "✅ Hozzáadva!";
      setTimeout(() => {
        btn.disabled = false;
        btn.innerHTML = oldText;
      }, 1500);
    }
  };

  window.removeFromCart = function (id) {
    cart = cart.filter(item => item.id !== id);
    updateCart();
  };

  window.clearCart = function () {
    cart = [];
    updateCart();
  };
  
  // === 💡 Termék részletek (felugró ablak) ===
window.showProductDetails = function (id) {
  const product = allProducts.find(p => p.id === id);
  if (!product) return;

document.getElementById("modalTitle").textContent = `${product.gyarto} ${product.tipus}`;
  document.getElementById("modalImage").src = `/img/${product.id}.png`;
    document.getElementById("modalCpu").textContent = product.processorName || "—";
  document.getElementById("modalOs").textContent = product.operatingSystemName || "—";
  document.getElementById("modalDisplay").textContent = product.kijelzo || "—";
  document.getElementById("modalRam").textContent = product.memoria || "—";
  document.getElementById("modalStorage").textContent = product.merevlemez || "—";
  document.getElementById("modalGpu").textContent = product.videoVezerlo || "—";
  document.getElementById("modalStock").textContent = product.db || "—";
  document.getElementById("modalPrice").textContent = `${(product.ar ?? 0).toLocaleString("hu-HU")} Ft`;

  // Kosárba gomb esemény
  const addBtn = document.getElementById("modalAddToCart");
  addBtn.onclick = () => addToCart(product.id);

  // Bootstrap 3 kompatibilis megnyitás
  $('#productModal').modal('show');
};


  // === 🔄 Inicializálás ===
  document.addEventListener("DOMContentLoaded", () => {
    load();
  });
})();