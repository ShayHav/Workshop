@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationUser
@import Presentation.Model.PresentationCart
@import Presentation.Model.PresentationBasket
@param PresentationUser user
@param PresentationCart cart

@template.navBar(user = user, title = "Shopping Cart")
<main class="container">
    @for(PresentationBasket basket: cart.baskets.values())
        <div class="container me-auto">
            <h3 class="text fs-5">Basket In Shop:${basket.shopName}</h3>
            @for(PresentationProduct product: basket.productWithAmount.keySet())
                <ul class="container card list-group list-group-flush">
                    <li class="container list-group-item">
                        <form class="form" name="updateAmount" id="updateAmount" action="/users/${user.getUsername()}/cart/${product.serialNumber}/update">
                            <p>category: ${product.name}</p>
                            <label class="form-label" for="quantity">quantity:</label>
                            <input class="form-control" name="quantity" id="quantity" type="number" value="${basket.productWithAmount.get(product)}" min="0" step="1">
                            <button class="btn btn-sm btn-warning" type="submit">update amount</button>
                            <input class="visually-hidden" name="shopID" value="${basket.shopId}" readonly>
                        </form>
                        <form class="form" id="removeProduct" name="remove product" action="/users/${user.getUsername()}/cart/${product.serialNumber}/remove">
                            <button class="btn btn-sm btn-danger" type="submit">remove from cart</button>
                        </form>
                    </li>
                </ul>
            @endfor
        </div>
    @endfor
</main>

</body>
</html>

