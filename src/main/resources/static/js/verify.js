const email = localStorage.getItem("signupEmail");

document.getElementById("verification-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const verificationCode = document.getElementById("verificationCode").value.trim();

  try {
    const res = await fetch("/auth/verify", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, verificationCode }),
    });

    const data = await res.text();

    if (res.ok) {
      alert("Verification successful!");
      localStorage.removeItem("signupEmail");
      window.location.href = "/login";
    } else {
      alert("Verification failed: " + data);
    }
  } catch (err) {
    alert("ERROR: " + err.message);
  }
});
