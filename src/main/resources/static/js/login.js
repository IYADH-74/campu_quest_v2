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
      localStorage.setItem("jwt", data.token);
      localStorage.setItem("role", data.role);
      alert("Login successful!");

      switch (data.role) {
        case "STUDENT":
          window.location.href = "/StudentHomePage";
          break;
        case "TEACHER":
          window.location.href = "/TeacherHomePage";
          break;
        case "ADMIN":
          window.location.href = "/AdminHomePage";
          break;
        case "STAFF":
          window.location.href = "/StaffHomePage";
          break;
        default:
          alert("Unknown role: " + data.role);
      }

    } else {
      alert("Login failed: " + (data.message || "Check your credentials"));
    }
  } catch (error) {
    alert("An error occurred: " + error.message);
  }
});
