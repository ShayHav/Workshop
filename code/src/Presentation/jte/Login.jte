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
    <main class="container">
        <div class="align-content-center">
            <h1 class="fs-2 fw-bold">Sign-In</h1>
            <div class="container">
                <form method="post" action="/users" class="form">
                    <div class="mb-3">
                        <label for="userName" class="form-label">Username</label>
                        <input type="text" name="username" class="form-control" id="userName" placeholder="username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" name="username" class="form-control" id="password" placeholder="password">
                    </div>
                    <div class="mb-3">
                        <button type="submit" class="btn btn-success">Login</button>
                        <a href="/" class="btn btn-danger">Cancel</a>
                    </div>
                </form>
            </div>
            <div class="container">
                <h2 class="fs-3">New to Market?</h2>
                <h3 class="fs-4">Register Here</h3>
                <a href="/users/new" class="btn btn-warning">Register</a>
            </div>
        </div>
    </main>
</body>
</html>