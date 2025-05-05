document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("signup-form");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
      firstName: form.firstname.value,
      lastName: form.lastname.value,
      username: form.username.value,
      email: form.email.value,
      password: form.password.value,
      role:"Student"
    };
    try {
      const response = await fetch("/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        localStorage.setItem("signupEmail", data.email);
        window.location.href = "/verify";
      } else {
        const error = await response.text();
        alert("Signup failed: " + error);
      }
    } catch (err) {

      alert("An error occurred: " + err.message);
    }

  });
});
