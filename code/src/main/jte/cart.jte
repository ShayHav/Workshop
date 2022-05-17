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
                <ul class="container">
                @for(PresentationProduct product: basket.productWithAmount.keySet())
                    <li>

                    </li>
                @endfor
                </ul>

        </div>
    @endfor
</main>


</body>
</html>

