@import Presentation.Model.*
@import java.util.List
@param PresentationUser user
@param PresentationShop shop

@template.navBar(user = user, title = shop.name)
<main class="container mt-">
    <div class="container mt-3">
        <h1 class="fs-3">Thanks for visiting in ${shop.name}</h1>
        <h2 class="fs-4">Little bit about us</h2>
        <p class="fs-5">${shop.description}</p>
    </div>
    <div class="container row">
        @if(user.hasInventoryPermission(Integer.parseInt(shop.id)))
            <div class="col-2">
                @if(shop.isOpen)
                    <a href="/shops/${shop.id}/addProduct" class="btn btn-primary">Add Product</a>
                @else
                    <a href="/shops/${shop.id}/addProduct" class="btn btn-primary disabled" aria-disabled="true">Add
                        Product</a>
                @endif
            </div>
        @endif
        @if(user.hasOrdersPermissions(Integer.parseInt(shop.id)))
            <div class="col-2">
                <a href="/shops/${shop.id}/orders" class="btn btn-info">Sales History</a>
            </div>
            <div class="col-2">
                <a class="btn btn-warning" href="/shops/${shop.id}/edit">Edit Shop</a>
            </div>
        @endif
        @if(shop.isFounder(user))
            @if(shop.isOpen())
                <div class="col-2">
                    <form action="/shops/${shop.id}/closeShop" method="post">
                        <button type="submit" class="btn btn-danger">Close Shop</button>
                    </form>
                </div>
            @else
                <div class="col-2">
                    <form action="/shops/${shop.id}/reopenShop" method="post">
                        <button type="submit" class="btn btn-success">Open Shop</button>
                    </form>
                </div>
            @endif
        @endif

    </div>
    <div class="container row mx-auto mb-3">
        <h3 class="fs-5 mt-3">The things we sell</h3>
        @for(Presentation.Model.PresentationProduct product: shop.products)
            <div class="card mt-1 mb-1 me-3" style="width: 18rem">
                    <img src="https://images.unsplash.com/photo-1562186971-736d2e3ae153?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2071&q=80"
                         class="card-img-top mt-2" alt="product front image">
                <div class="card-body">
                    <h2 class="card-title">${product.name}</h2>
                    <p class="card-text">${product.description}</p>
                    @if(product.amount > 0)
                        <a href="/shops/${shop.id}/${product.serialNumber}" class="btn btn-primary">Show Product</a>
                    @else
                        <a href="/shops/${shop.id}/${product.serialNumber}" class="btn btn-primary">Show Product</a>
                        <p class="mt-2 card-text text-danger">Out Of Stock</p>
                    @endif
                </div>
            </div>
        @endfor
    </div>
</main>

</body>
</html>