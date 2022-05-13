@import Presentation.Model.PresentationUser
@import gg.jte.Content
@param PresentationUser r_user


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>SignIn</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
@template.NavBar(user = r_user)
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
    const socket = new WebSocket("wss://localhost:8080/users/new")
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