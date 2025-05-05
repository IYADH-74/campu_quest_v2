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
        // Save JWT in localStorage or cookie
        localStorage.setItem("jwt", data.token);
        alert("Login successful!");
        window.location.href = "/HomePage"; 
      } else {
        alert("Login failed: " + (data.message || "Check your credentials"));
      }
    } catch (error) {
      alert("An error occurred: " + error.message);
    }
  });
  