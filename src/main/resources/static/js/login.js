document.getElementById("login-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  try {
    const response = await fetch("/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (response.ok) {
      const token = data.token;
      const role = data.role;

      localStorage.setItem("jwt", token);
      localStorage.setItem("role", role);
      let route;
      switch (role.toUpperCase()) {
        case "STUDENT":
          route = "/StudentHomePage";
          break;
        case "TEACHER":
          route = "/TeacherHomePage";
          break;
        case "ADMIN":
          route = "/AdminHomePage";
          break;
        case "STAFF":
          route = "/StaffHomePage";
          break;
        default:
          alert("Unknown role: " + role);
          return;
      }
     const accessResponse = await fetch(route, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });

      if (accessResponse.ok) {
        window.location.href = route;
      } else {
        alert("Access denied. Token may be invalid or you don't have permission.");
      }

    } else {
      alert("Login failed: " + (data.message || "Check your credentials"));
    }
  } catch (error) {
    alert("An error occurred: " + error.message);
  }
});
