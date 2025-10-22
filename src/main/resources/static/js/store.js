document.addEventListener("DOMContentLoaded", () => {
  const productList = document.getElementById("product-list");
  const cartBadge = document.querySelector(".dropdown .qty");
  const cartList = document.querySelector(".cart-list");
  const cartSummary = document.querySelector(".cart-summary");
  const API_BASE = "/api/cart";

  // === 🔑 Session ID (egyedi kosár minden userhez)
  let sessionId = localStorage.getItem("sessionId");
  if (!sessionId) {
    sessionId = "sess_" + Math.random().toString(36).substring(2, 10);
    localStorage.setItem("sessionId", sessionId);
  }

  let allProducts = [];

  // === 📦 Termékek betöltése ===
  async function loadProducts() {
    try {
      const res = await fetch("/api/laptops?page=0&size=12");
      if (!res.ok) throw new Error("Nem sikerült betölteni a laptopokat");
      const data = await res.json();
      allProducts = data.content || [];

      if (!Array.isArray(allProducts) || allProducts.length === 0) {
        productList.innerHTML = `<div class="col-md-12 text-center py-5">
          <h4>Nincs megjeleníthető termék</h4></div>`;
        return;
      }

      productList.innerHTML = allProducts.map(card).join("");
    } catch (err) {
      console.error("❌ Termék betöltési hiba:", err);
      productList.innerHTML = `<div class="col-md-12 text-center py-5">
        <h4>Hiba a termékek betöltésekor</h4>
        <p>${err.message}</p></div>`;
    }
  }

  // === 💳 Termékkártya ===
  function card(l) {
    const ar = (l.ar ?? 0).toLocaleString("hu-HU");
    const title = `${l.gyarto || ""} ${l.tipus || ""}`.trim();
    return `
      <div class="col-md-4 col-xs-6">
        <div class="product">
          <div class="product-img" style="cursor:pointer;" onclick="showProductDetails(${l.id})">
            <img src="/img/${l.id}.png" alt="${title}" onerror="this.src='/img/logo.png'">
          </div>
          <div class="product-body">
            <p class="product-category">${l.operatingSystemName || "—"}</p>
            <h3 class="product-name">${title}</h3>
            <h4 class="product-price">${ar} Ft</h4>
          </div>
          <div class="add-to-cart">
  <button type="button" class="add-to-cart-btn"
    onclick="event.stopPropagation(); addToCart(${l.id}, event)">
    <i class="fa fa-shopping-cart"></i> Kosárba
  </button>
</div>
        </div>
      </div>`;
  }

  // === 🧺 Kosár frissítése (REST alapú) ===
async function updateCart() {
  try {
    const res = await fetch(`${API_BASE}/session/${sessionId}`);
    if (!res.ok) throw new Error(`Kosár betöltése sikertelen (${res.status})`);
    const cart = await res.json();

    if (!cartList || !cartSummary) return;

    if (cart.length === 0) {
      if (cartBadge) cartBadge.textContent = "0";
      cartList.innerHTML = `<p class="text-center text-muted">Nincs termék a kosárban</p>`;
      cartSummary.innerHTML = `<small>Összesen: 0 Ft</small>`;
      return;
    }

    let total = 0, totalQty = 0;
    cartList.innerHTML = cart.map(item => {
      total += item.price * item.quantity;
      totalQty += item.quantity;
      return `
        <div class="product-widget">
          <div class="product-img">
            <img src="/img/${item.productId}.png" alt="${item.productName}" onerror="this.src='/img/logo.png'">
          </div>
          <div class="product-body">
            <h3 class="product-name">${item.productName}</h3>
            <h4 class="product-price">
              <span class="qty">${item.quantity}x</span>
              ${(item.price * item.quantity).toLocaleString("hu-HU")} Ft
            </h4>
          </div>
          <button class="delete" onclick="removeFromCart(${item.id})"><i class="fa fa-close"></i></button>
        </div>`;
    }).join("");

    // 🔹 Összesítés + kosár ikon frissítése
    if (cartBadge) cartBadge.textContent = totalQty;
    cartSummary.innerHTML = `
      <small>${cart.length} termék</small>
      <h5>Összesen: ${total.toLocaleString("hu-HU")} Ft</h5>
      <button onclick="clearCart()" class="btn btn-sm btn-outline-danger w-100 mt-2">
        <i class="fa fa-trash"></i> Kosár ürítése
      </button>`;
  } catch (err) {
    console.error("❌ Kosár frissítési hiba:", err.message);
  }
}


  // === 🧩 Globális kosárfüggvények (window-on elérhető!)
  window.addToCart = async function (id, e) {
  if (e) e.stopPropagation();
  const product = allProducts.find(p => p.id === id);
  if (!product) return;

  const item = {
    sessionId,
    productId: id,
    productName: `${product.gyarto} ${product.tipus}`,
    quantity: 1,
    price: product.ar
  };

  try {
    const res = await fetch("/api/cart/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(item)
    });

    if (!res.ok) throw new Error("Nem sikerült a kosárba helyezni.");
    await updateCart(); // 🔥 FONTOS: azonnal frissítjük a kosarat

    // 🔹 Visszajelzés a gombon
    const btn = e?.target?.closest("button");
    if (btn) {
      const oldText = btn.innerHTML;
      btn.innerHTML = "✅ Hozzáadva!";
      btn.disabled = true;
      setTimeout(() => {
        btn.innerHTML = oldText;
        btn.disabled = false;
      }, 1500);
    }
  } catch (err) {
    console.error("❌ Kosár hiba:", err);
  }
};


  window.removeFromCart = async function (id) {
    try {
      const res = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error("Törlés sikertelen");
      await updateCart();
    } catch (err) {
      console.error("❌ Törlés hiba:", err);
    }
  };

  window.clearCart = async function () {
    try {
      const res = await fetch(`${API_BASE}/session/${sessionId}`, { method: "DELETE" });
      if (!res.ok) throw new Error("Kosár ürítése sikertelen");
      await updateCart();
    } catch (err) {
      console.error("❌ Kosár törlés hiba:", err);
    }
  };

  // === 💡 Termék részletek (modal)
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

    const addBtn = document.getElementById("modalAddToCart");
    addBtn.onclick = (ev) => addToCart(product.id, ev);

    $("#productModal").modal("show");
  };

  // === 🚀 Indítás ===
  loadProducts().then(updateCart);
});
