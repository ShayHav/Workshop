@import Presentation.Model.PresentationUser
@param PresentationUser user
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>SignIn</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
@template.navBar(user = user)
    <main class="container align-content-center">
        <div class="container col-5 mt-5 border">
            <div class="container col-4 mt-3">
                <h1 class="fs-2 fw-bold">Sign-In</h1>
            </div>
            <div class="container mb-5">
                <form method="post" action="/users/login" class="form">
                    <div class="mb-3">
                        <label for="userName" class="form-label">Username</label>
                        <input type="text" name="username" class="form-control" id="userName" placeholder="username" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" name="password" class="form-control" id="password" placeholder="password" required>
                    </div>
                    <div class="mb-3">
                        <button type="submit" class="btn btn-success">Login</button>
                        <a href="/" class="btn btn-danger">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
        <div class="container col-4 mt-3 border-top">
            <div class="container align-content-center mt-3 col-6">
                <div class="container">
                    <h2 class="fs-5 mb-3">New to Market?</h2>
                </div>
                <div class="container ps-6">
                    <a href="/users/new" class="btn btn-warning">Register</a>
                </div>
            </div>
        </div>
    </main>
</body>
</html>