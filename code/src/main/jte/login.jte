@import Presentation.Model.PresentationUser
@param PresentationUser user

@template.navBar(user = user, title = "Sign in")
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