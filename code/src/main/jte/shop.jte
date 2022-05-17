@import Presentation.Model.*
@import java.util.List
@param PresentationUser user
@param PresentationShop shop

@template.navBar(user = user, title = shop.name)
<main class="container mt-3">
    <div class="container mt-3">
        <h1 class="fs-3">Thanks for visiting in ${shop.name}</h1>
        <h2 class="fs-4">Little bit about us</h2>
        <p class="fs-5">${shop.description}</p>
    </div>
    @if(user.hasInventoryPermission(Integer.parseInt(shop.id)))
        <div class="container">
        <a href="/shops/${shop.id}/addProduct" class="btn btn-primary">Add Product</a>
        </div>
    @endif
    <div class="container row">
        <h3 class="fs-5 mt-3">The things we sell</h3>
        @for(Presentation.Model.PresentationProduct product: shop.products)
            <div class="card m-1" style="width: 18rem">
                <img src="https://images.unsplash.com/photo-1562186971-736d2e3ae153?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2071&q=80"
                     class="card-img-top" alt="product front image">
                <div class="card-body">
                    <h2 class="card-title">${product.name}</h2>
                    <p class="card-text">${product.description}</p>
                    <a href="/shops/${shop.id}/${product.serialNumber}" class="btn btn-primary">Show Product</a>
                </div>
            </div>
        @endfor
    </div>
</main>

</body>
</html>