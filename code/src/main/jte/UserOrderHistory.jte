@import Presentation.Model.PresentationOrder
@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationUser
@import java.util.List
@param PresentationUser user
@param List<PresentationOrder> orders

@template.navBar(user = user, title = "My orders")
<div class="mt-3 mb-2 row">
    <div class="col-2">
        <h5>Shop name</h5>
    </div>
    <div class="col-2">
        <h5>Order Number</h5>
    </div>
    <div class="col-2">
        <h5>Purchase Date</h5>
    </div>
    <div class="col-2">
        <h5>Bought User</h5>
    </div>
    <div class="col-2">
        <h5>Total Amount</h5>
    </div>
</div>
<div id="accordion">
    @for(PresentationOrder order : orders)
        <div class="card rounded">
            <div class="card-header row" id="heading${order.id}">
                <div class="col-2">
                    <p>${order.shopName}</p>
                </div>
                <div class="col-2">
                    <p>${order.id}</p>
                </div>
                <div class="col-2">
                    <p>${order.getPurchaseDate()}</p>
                </div>
                <div class="col-2">
                    <p>${order.userBought}</p>
                </div>
                <div class="col-2">
                    <p>${order.totalAmount}</p>
                </div>
                <div class="col-2">
                    <button class="btn btn-link" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${order.id}"
                            aria-expanded="false" aria-controls="collapse${order.id}"
                            id="collapseButton${order.id}" >Show Items in order
                    </button>
                </div>
            </div>
            <div id="collapse${order.id}" class="collapse">
                <div class="card-body rounded-1">
                    @for(PresentationProduct product: order.productsBoughtWithPrices.keySet())
                        <ul class="container rounded-1 card row mx-auto list-group-flush list-group">
                            <li class="container list-group-item">
                                <div class="col">
                                    <p>Product Name: ${product.name}</p>
                                </div>
                                <div class="col">
                                    <p>Amount Bought: ${product.amount}</p>
                                </div>
                                <div class="col">
                                    <p>Total Price: ${order.productsBoughtWithPrices.get(product)}</p>
                                </div>
                            </li>
                        </ul>
                    @endfor
                </div>
            </div>
        </div>
    @endfor
</div>

</body>

</html>

