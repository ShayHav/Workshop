@param Presentation.Model.PresentationProduct product
@param Presentation.Model.PresentationUser user
@param int shopId
@template.navBar(user = user, title = product.name)
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
        @if(user.hasInventoryPermission(shopId))
            <div class="container">
                <a href="/shops/${shopId}/${product.serialNumber}" class="btn btn-info">Edit</a>
            </div>
        @endif
    </div>
</main>
</body>
</html>