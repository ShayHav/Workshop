@import Presentation.Main
@import Presentation.Model.PresentationUser
@import gg.jte.Content
@import java.net.InetAddress
@param PresentationUser r_user

@template.navBar(user = r_user, title = "Register")
<main class="container align-content-center">
    <div class="container col-5 mt-5 border">
        <div class="container col-4 mt-3">
            <h1 class="fs-2 fw-bold">Register</h1>
        </div>
        <div class="container mb-5">
            <form method="post" name="registerForm" class="form">
                <div class="mb-3">
                    <label for="userName" class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" id="userName" placeholder="username">
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="username" class="form-control" id="password" placeholder="password">
                </div>
                <div class="mb-3">
                    <button type="button" id="submitButton" class="btn btn-success">Register</button>
                    <a href="/" class="btn btn-danger">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    <div id="responsePlace" class="container mt-5 col-4 visually-hidden">
        <p class="fs-5"></p>
    </div>

</main>
</body>
<script type="javascript">
    const socket = new WebSocket("wss://${Main.ip.getHostAddress()}:8080/users/new")
    const button = document.querySelector('#submitButton');
    button.addEventListener('click', function () {
        const form = document.registerForm;
        const message = {username: form.username.value, password: form.password.value};
        socket.send(JSON.stringify(message));
    });
    socket.onmessage = async function (event) {
        const response = JSON.parse(event.data);
        if (!response.errorMessage) {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = "The user was created";
            toUpdate.classList.add("border", "border-success", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
            await new Promise(r => setTimeout(r, 3000));
            window.location.replace("/");
        }
        const username = document.getElementById("userName");
        username.classList.add("border", "border-danger");
        const toUpdate = document.getElementById("responsePlace");
        toUpdate.firstChild.textContent = response.error;
        toUpdate.classList.add("border", "border-danger", "border-3", "rounded");
        toUpdate.classList.remove("visually-hidden");
    }
</script>
</html>