// login.js

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("login-form");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = form.email.value.trim();
    const password = form.password.value;

    try {
      const res = await fetch("/auth/login", {
        method: "POST",
        credentials: "include",               // ← send & receive the jwt cookie
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });

      if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        return alert("Login failed: " + (err.message || res.status));
      }

      const { role } = await res.json();
      if (!role) {
        return alert("Login succeeded but no role returned!");
      }

      localStorage.setItem("role", role);

      const route = {
        Student: "/student/home",
        Teacher: "/teacher/home",
        Admin:   "/admin/home",
        Staff:   "/staff/home"
      }[role];

      if (!route) {
        return alert("Unknown role: " + role);
      }

      window.location.href = route;

    } catch (err) {
      console.error("Network/error:", err);
      alert("An unexpected error occurred: " + err.message);
    }
  });
});
