document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("signup-form");

  fetch("/api/classes")
  .then(response => response.json())
  .then(classes => {
    classes.forEach(classe => {
      const option = document.createElement("option");
      option.value = classe.id;
      option.textContent = classe.className;
      classDropdown.appendChild(option);
    });
  })
  .catch(err => {
    console.error("FAILED TO LOAD CLASSES:", err);
    alert("Could not load class list. Talk to IT or scream into the void.");
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
      firstName: form.firstname.value,
      lastName: form.lastname.value,
      username: form.username.value,
      email: form.email.value,
      password: form.password.value,
      role:"Student",
      classeId: classDropdown.value,
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
