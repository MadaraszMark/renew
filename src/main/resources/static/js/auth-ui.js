document.addEventListener('DOMContentLoaded', () => {
  const authContainer = document.getElementById('authLinks');
  if (!authContainer) return;

  const basePath = "/" + window.location.pathname.split("/")[1];
  const userData = localStorage.getItem('user');
  authContainer.innerHTML = '';

  if (!userData) {
    // Nem bejelentkezett felhasználó
    authContainer.innerHTML = `
      <li><a href="${basePath}/login"><i class="fa fa-sign-in"></i> Bejelentkezés</a></li>
      <li><a href="${basePath}/register"><i class="fa fa-user-plus"></i> Regisztráció</a></li>
    `;
  } else {
    const user = JSON.parse(userData);
    let html = '';

    // 🔹 Ha USER vagy GUEST → csak Üzenetek
    if (user.role === 'USER' || user.role === 'GUEST') {
      html += `<li><a href="${basePath}/messages"><i class="fa fa-envelope"></i> Üzenetek</a></li>`;
    }

    // 🔹 Ha ADMIN → Admin panel
    if (user.role === 'ADMIN') {
      html += `<li><a href="${basePath}/admin"><i class="fa fa-cog"></i> Admin</a></li>`;
    }

    // 🔹 User e-mail és kijelentkezés
    html += `
      <li><a href="#"><i class="fa fa-user-o"></i> ${user.email}</a></li>
      <li><a href="#" onclick="logoutUser(event)"><i class="fa fa-sign-out"></i> Kijelentkezés</a></li>
    `;

    authContainer.innerHTML = html;
  }
});

async function logoutUser(e) {
  e.preventDefault();
  try {
    await fetch(`${contextPath}auth/logout`, { method: 'POST' });
  } catch (err) {
    console.warn('Logout hiba:', err);
  } finally {
    localStorage.removeItem('user');
    window.location.href = contextPath;
  }
}
