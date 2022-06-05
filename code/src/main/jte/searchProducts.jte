@import java.util.List
@import Presentation.Main
@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationUser

@param List<Presentation.Model.PresentationProduct> products
@param PresentationUser user
@param Double minPrice
@param Double maxPrice
@param String category
@param String searchBy
@param String query

@template.navBar(user = user, title = "Search Result");

<main class="container mt-3">
    <div>
        <h1 class="fs-2">Search Results</h1>
        <div class="my-4 mx-4 mb-5 container">
            <form method="get" id="filterProducts" class="form border rounded border-1 w-75" action="/search">
                <div class="row mb-2">
                    <div class="col">
                        <div class="mt-3 mx-3 mb-3">
                            <label for="minPrice" class="form-label">Min Price</label>
                            <input type="range" step="0.1" min="${minPrice}" max="${maxPrice}" class="form-range" id="minPrice" name="minPrice" value="0"
                                   onChange="document.getElementById('rangemin').innerText = document.getElementById('minPrice').value"
                                   oninput="document.getElementById('rangemin').innerText = document.getElementById('minPrice').value">
                            <span id="rangemin">0<!-- Default value --></span>
                        </div>
                        <div class="mt-3 mx-3 mb-3">
                            <label for="maxPrice" class="form-label">Max Price</label>
                            <input type="range" step="0.1" min="${minPrice}" max="${maxPrice}" class="form-range" id="maxPrice" name="maxPrice" value="${maxPrice}"
                                   onChange="document.getElementById('rangemax').innerText = document.getElementById('maxPrice').value"
                                   oninput="document.getElementById('rangemax').innerText = document.getElementById('maxPrice').value">
                            <span id="rangemax">${maxPrice}<!-- Default value --></span>
                        </div>
                    </div>
                    <div class="col">
                        <div class="mt-3 mx-3 mb-3">
                            <label for="category" class="form-label">Category</label>
                            <input type="text" name="category" id="category" class="form-control" placeholder="category to filter" value="${category}">
                        </div>
                    </div>
                </div>
                <div class="row col-4">
                    <div class="mb-2 mx-2 col">
                        <button class="btn btn-primary" type="submit">Filter results</button>
                    </div>
                    <div class="mb-2 col">
                        <button type="button" id="resetFilter" class="btn btn-primary">Reset Filter</button>
                    </div>
                </div>
                <input class="visually-hidden" id="searchBy" name="searchBy" value="${searchBy}">
                <input class="visually-hidden" id="query" name="query" value="${query}">
            </form>
        </div>

    </div>
    <div class="mt-3">
        @for(PresentationProduct product: products)
            <div class="card mb-3">
                <div class="row g-0">
                    <div class="col-md-4">
                        <img src="https://images.unsplash.com/photo-1562186971-736d2e3ae153?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2071&q=80"
                             class="img-fluid rounded-start" alt="product's image">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <a class="card-title fs-3 l"
                               href="/shops/${product.shop}/${product.serialNumber}">${product.name}</a>
                            <p class="card-text fs-5">${product.description}</p>
                            <p class="card-text fs-5"><small class="text-muted"><b>price:</b> ${product.price}$</small></p>
                        </div>
                    </div>
                </div>
            </div>
        @endfor
    </div>
</main>
</body>
</html>


<script>
    const reset_button = document.getElementById('resetFilter');
    reset_button.addEventListener('click',()=> {
        document.getElementById("minPrice").max = "1000";
        document.getElementById('minPrice').value = "0";
        document.getElementById("maxPrice").max = "1000";
        document.getElementById('maxPrice').value = "1000";
        document.getElementById('category').value = "";

        const form_products = document.getElementById("filterProducts");
        form_products.submit();
    });


</script>