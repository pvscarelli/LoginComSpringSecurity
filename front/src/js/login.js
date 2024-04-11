const sendBtn = document.getElementById("sendBtn").addEventListener("click", (ev) => {
  ev.preventDefault()
  const username = document.getElementById("username").value
  const password = document.getElementById("password").value

  if (username !== "" && password !== "") {
    fetch("http://localhost:8080/auth/login", {
      method: "post",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        login: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        const token = data.token
        localStorage.setItem("jwt-token", token)
        window.location.href = "index.html"
      })
      .catch((error) => {
        showErrorMessage("Revise os campos...")
      })
  }
})

function showErrorMessage(message) {
  let errorMessageDiv = document.getElementById("errorMessage")
  let errorMessageText = document.getElementById("errorMessageText")
  errorMessageText.innerText = message
  errorMessageDiv.style.display = "block"
}
