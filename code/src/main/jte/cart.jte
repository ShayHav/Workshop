@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationUser
@import Presentation.Model.PresentationCart
@import Presentation.Model.PresentationBasket
@param PresentationUser user
@param PresentationCart cart

@template.navBar(user = user, title = "Shopping Cart")
<main class="container">
    <h2>Welcome to cart</h2>
    @for(PresentationBasket basket: cart.baskets.values())
        <div class="container me-auto">
            <h3 class="text fs-5">Basket In Shop:${basket.shopName}</h3>
            @for(PresentationProduct product: basket.productWithAmount.keySet())
                <ul class="container card list-group list-group-flush row mx-auto">
                    <li class="container list-group-item">
                        <form class="form" name="updateAmount" id="updateAmount"
                              action="/users/${user.getUsername()}/cart/${product.serialNumber}/update">
                            <p>category: ${product.name}</p>
                            <div class="row">
                                <label class="form-label" for="quantity">quantity:</label>
                                <div class="col-2">

                                    <input class="form-control mb-2" name="quantity" id="quantity" type="number"
                                           value="${basket.productWithAmount.get(product)}" min="0" step="1">
                                </div>
                                <div class="col">
                                    <button class="btn btn-warning mb-2" type="submit">update amount</button>
                                    <input class="visually-hidden" name="shopID" value="${basket.shopId}" readonly>
                                </div>
                            </div>
                        </form>
                        <form class="form" id="removeProduct" name="remove product"
                              action="/users/${user.getUsername()}/cart/${product.serialNumber}/remove">
                            <button class="btn btn-sm btn-danger" type="submit"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                                    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                                </svg></button>
                        </form>
                    </li>
                </ul>
            @endfor
        </div>
    @endfor
    @if(cart.baskets.size()>0)
        <div class="mb-2">
            <p class="container mt-3">
                Total Amount: ${cart.totalAmount}
            </p>
        </div>

    @endif
</main>

</body>
</html>

