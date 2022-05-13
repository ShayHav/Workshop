@param Presentation.Model.PresentationProduct product
@param Presentation.Model.PresentationUser user
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
@template.NavBar(user = user)
<main class="container align-content-center">
    <div class="container col-5 mt-5 border">
        <div class="container col-4 mt-3">
            <h1 class="fs-2 fw-bold">${product.name}</h1>
        </div>
        <div class="container mt-5">
            <p class="fs-5 text-primary">${product.category}</p>
        </div>

        <div class="container">
            <h2 class="fs-4">Description</h2>
            <p class="fs-5">${product.description}</p>
        </div>
        @if(product.amount <= 10)
            <div>
                <p class="fs-5 text-danger">Less then ${product.amount} is left!</p>
            </div>
        @endif
        <div class="container">
            <p>${product.price}<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-currency-dollar" viewBox="0 0 16 16">
                    <path d="M4 10.781c.148 1.667 1.513 2.85 3.591 3.003V15h1.043v-1.216c2.27-.179 3.678-1.438 3.678-3.3 0-1.59-.947-2.51-2.956-3.028l-.722-.187V3.467c1.122.11 1.879.714 2.07 1.616h1.47c-.166-1.6-1.54-2.748-3.54-2.875V1H7.591v1.233c-1.939.23-3.27 1.472-3.27 3.156 0 1.454.966 2.483 2.661 2.917l.61.162v4.031c-1.149-.17-1.94-.8-2.131-1.718H4zm3.391-3.836c-1.043-.263-1.6-.825-1.6-1.616 0-.944.704-1.641 1.8-1.828v3.495l-.2-.05zm1.591 1.872c1.287.323 1.852.859 1.852 1.769 0 1.097-.826 1.828-2.2 1.939V8.73l.348.086z"/>
                </svg></p>
            <a href="/users/${user.getUsername()}/addToCart/?item=${product.serialNumber}" class="btn btn-success">Add To Cart</a>
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