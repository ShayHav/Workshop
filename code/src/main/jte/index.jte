@import Presentation.Model.PresentationUser
@param  PresentationUser user
@param java.util.List<Presentation.Model.PresentationShop> shops
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>WorkShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
@template.navBar(user = user)
<main class="container">
    <h1 class="fs-1">Welcome to the MarketPlace!</h1>
    <div class="container row mx-auto">
        @for(Presentation.Model.PresentationShop shop: shops)
            <div class="card m-1" style="width: 18rem">
                <img src="https://cdn.pixabay.com/photo/2017/08/07/19/46/shop-2607121_960_720.jpg"
                     class="card-img-top" alt="shop front image">
                <div class="card-body">
                    <h2 class="card-title">${shop.name}</h2>
                    <p class="card-text">${shop.description}</p>
                    <a href="/shops/${shop.id}/products" class="btn btn-primary">View Products</a>
                </div>
            </div>
        @endfor
    </div>
</main>
</body>
</html>