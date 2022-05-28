@import Presentation.Model.PresentationOrder
@import Presentation.Model.PresentationProduct
@import Presentation.Model.PresentationShop
@import Presentation.Model.PresentationUser
@import java.util.List
@import java.util.Map
@param PresentationUser user
@param Map<PresentationUser,List<PresentationOrder>> userOrders
@param Map<PresentationShop,List<PresentationOrder>> shopOrders

@template.navBar(user = user, title = "Sales")
<main class="mx-4 my-3">
    <div class="mb-5 mt-3">
        <div class="mb-3 fs-3" style="text-align: center">Shops History</div>
        @for(PresentationShop shop: shopOrders.keySet())
            <div class="mt-3 mb-2 row">
                <div class="col-2">
                    <div class="mb-3 fs-5">Shop Name</div>
                </div>
            </div>
            <div class=" containercard">
                <div class="card-header" id="heading${shop.name}">
                    <div class="row">
                        <div class="col-2">
                            <p>${shop.name}</p>
                        </div>
                        <div class="col-2">
                            <button class="btn btn-link" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#collapse${shop.name}"
                                    aria-expanded="false" aria-controls="collapse${shop.name}"
                                    id="collapseButton${shop.name}">Show Orders
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="collapse${shop.name}" class="collapse">
                <div class="mt-3 mb-2 row">
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
                <div>
                    @for(PresentationOrder order : shopOrders.get(shop))
                        <div class="card">
                            <div class="card-header" id="heading${order.id}">
                                <div class="row">
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
                                        <button class="btn btn-link" type="button" data-bs-toggle="collapse"
                                                data-bs-target="#collapse${order.id}"
                                                aria-expanded="false" aria-controls="collapse${order.id}"
                                                id="collapseButton${order.id}">Show Items in order
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div id="collapse${order.id}" class="collapse">
                                <div class="card-body">
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
            </div>
        @endfor
    </div>
    <div class="mt-3">
        <div class="mb-3 fs-3" style="text-align: center">Users History</div>
        @for(PresentationUser u: userOrders.keySet())
            <div class="mt-3 mb-2 row">
                <div class="col-2">
                    <div class="mb-3 fs-5">Username</div>
                </div>
            </div>
            <div class="card">
                <div class="card-header" id="heading${u.getUsername()}">
                    <div class="row">
                        <div class="col-2">
                            <p>${u.getUsername()}</p>
                        </div>
                        <div class="col-2">
                            <button class="btn btn-link" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#collapse${u.getUsername()}"
                                    aria-expanded="false" aria-controls="collapse${u.getUsername()}"
                                    id="collapseButton${u.getUsername()}">Show Orders
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="collapse${u.getUsername()}" class="collapse">
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
                <div>
                    @for(PresentationOrder order : userOrders.get(u))
                        <div class="card">
                            <div class="card-header" id="heading${order.id}">
                                <div class="row">
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
                                        <button class="btn btn-link" type="button" data-bs-toggle="collapse"
                                                data-bs-target="#collapse${order.id}"
                                                aria-expanded="false" aria-controls="collapse${order.id}"
                                                id="collapseButton${order.id}">Show Items in order
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div id="collapse${order.id}" class="collapse">
                                <div class="card-body">
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
            </div>
        @endfor
    </div>
</main>
</body>

</html>
        
