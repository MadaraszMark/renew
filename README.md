# 💻 ReNew – Felújított Laptop Webáruház

## 🧾 Leírás
A **ReNew** egy **Spring Boot + Thymeleaf** alapú webalkalmazás, amely egy  
felújított laptopokat értékesítő online áruház működését valósítja meg.  
A cél egy biztonságos, modern és könnyen kezelhető rendszer létrehozása,  
ahol a felhasználók böngészhetik a termékeket, kapcsolatba léphetnek a céggel,  
regisztrálhatnak, míg az **adminisztrátorok** kezelhetik az adatokat.

---

## 🚀 Fő funkciók
- 👤 **Felhasználói regisztráció és bejelentkezés** (JWT autentikáció)
- 🔒 **Biztonságos jogosultságkezelés** – admin és user szerepkörök
- 💬 **Kapcsolat űrlap + Üzenetkezelés**
- 💾 **Laptop CRUD funkciók** (admin panelről kezelhető)
- 📊 **Diagram (Chart.js)** – laptopok gyártónkénti statisztikája
- 🛒 **Kosárkezelés** (session alapú)
- 🧰 **REST API tesztelése Postman és cURL segítségével**
- 📁 **Teljes adatbázis kapcsolat** (JPA + Hibernate)

---

## 🗃️ Technológiák
- **Java 21 / Spring Boot 3**
- **Spring Security + JWT Token**
- **Spring Data JPA / Hibernate**
- **MySQL adatbázis**
- **Thymeleaf sablonmotor**
- **Chart.js**
- **Bootstrap / jQuery / Font Awesome**
- **Postman / cURL (API teszteléshez)**

---

## 🧩 Backend modulok
- **`LaptopController`, `LaptopService`, `LaptopRepository`** – REST API a laptopok kezelésére  
- **`AuthController`, `AuthService`** – regisztráció, bejelentkezés, token generálás  
- **`ChartController`, `ChartService`** – gyártónkénti statisztikai adatok szolgáltatása  
- **`CartController`, `CartService`** – session-alapú kosárkezelés  
- **`SecurityConfig`, `JwtAuthenticationFilter`, `JwtTokenUtil`** – biztonsági réteg konfigurálása  

---

## 🧱 Frontend oldalak
- 🏠 **index.html** – főoldal, terméklista  
- 💻 **store.html** – laptopok böngészése és keresése  
- ℹ️ **about.html** – cégbemutató  
- 📬 **contact.html** – kapcsolatfelvétel űrlap  
- 📊 **chart.html** – gyártónkénti laptop-statisztika  
- 📨 **messages.html** – felhasználói üzenetek megtekintése  
- 🔧 **admin.html** – admin felület (üzenetek + laptop CRUD)

---

## 🔐 Biztonsági konfiguráció
A projekt **JWT alapú stateless autentikációt** alkalmaz:
- minden bejelentkezett felhasználó egy aláírt **token**-t kap,  
- az admin végpontokhoz csak **`ROLE_ADMIN`** jogosultsággal lehet hozzáférni,  
- a jelszavak **BCrypt** algoritmussal titkosítva tárolódnak.

> 📁 A `SecurityConfig` osztály határozza meg az engedélyezett útvonalakat és a filterláncot.  
> A `JwtAuthenticationFilter` ellenőrzi a token érvényességét minden kérésnél.
