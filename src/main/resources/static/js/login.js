async function login() {

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const mensagemErro = document.getElementById("mensagemErro");

    mensagemErro.textContent = "";

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                email,
                senha
            })
        });
        if (!response.ok) {
            mensagemErro.textContent = "E-mail ou senha inválidos.";
            return;
        }
        console.log("Passei aqui")
        const data = await response.json();
        localStorage.setItem("token", data.token);
        window.location.href = "index.html";
    } catch (error) {
        mensagemErro.textContent = "Erro ao conectar com o Servidor.";
    }

}