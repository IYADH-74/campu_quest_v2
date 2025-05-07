document.addEventListener("DOMContentLoaded", async () => {
  const role = localStorage.getItem("role");
  if (!role) {
    return window.location.href = "/login";
  }

  try {
    const resp = await fetch("/users/me", {
      credentials: "include"     // ← send the jwt cookie
    });
    if (!resp.ok) throw new Error("unauthorized");

    const me = await resp.json();
    if (me.role.toLowerCase() !== role.toLowerCase()) {
      return window.location.href = "/unauthorized";
    }
    // all good → let the page render
  } catch (err) {
    console.warn("Auth guard:", err);
    window.location.href = "/login";
  }
});
