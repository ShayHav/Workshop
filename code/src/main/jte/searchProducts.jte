@import java.util.List
@import Presentation.Main
@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationUser

@param List<Presentation.Model.PresentationProduct> products
@param PresentationUser user

@template.navBar(user = user, title = "Search Result")

<main class="container mt-3">
    <h1 class="fs-2">Search Results</h1>
    @for(PresentationProduct product: products)
        <div class="card mb-3">
            <div class="row g-0">
                <div class="col-md-4">
                    <img src="https://images.unsplash.com/photo-1562186971-736d2e3ae153?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2071&q=80" class="img-fluid rounded-start" alt="product's image">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <a class="card-title link" href="/shops/${product.shop}/${product.serialNumber}">${product.name}</a>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small class="text-muted">${product.price}</small></p>
                    </div>
                </div>
            </div>

        </div>
    @endfor
</main>
</body>
</html>