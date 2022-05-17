@param Presentation.Model.PresentationProduct product
@param Presentation.Model.PresentationUser user
@param int shopId
@template.navBar(user = user, title = product.name)
<main class="container align-content-center mb-4">
    <div class="container card col-5 mt-5 border">
        <div class="container col-4 mt-3 mb-3">
            <h1 class="card-title fs-2 fw-bold">${product.name}</h1>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item">
                <div class="container card-text ">
                    <h2 class="fs-4 fw-bold">Category</h2>
                    <p class="fs-5 text">${product.category}</p>
                </div>
            </li>
            <li class="list-group-item">
                <div class="container card-text">
                    <h2 class="fs-4 fw-bold">Description</h2>
                    <p class="fs-5">${product.description}</p>
                </div>
            </li>
            @if(product.amount <= 10)
                <li class="list-group-item">
                    <div>
                        <p class="fs-5 text-danger">Less then ${product.amount} is left!</p>
                    </div>
                </li>
            @endif
            <li class="list-group-item">
                <div class="container card-text">
                    <h2 class="fs-4 fw-bold">Price</h2>
                    <p class="fs-5 text-lg-start">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-currency-dollar" viewBox="0 0 16 16">
                            <path d="M4 10.781c.148 1.667 1.513 2.85 3.591 3.003V15h1.043v-1.216c2.27-.179 3.678-1.438 3.678-3.3 0-1.59-.947-2.51-2.956-3.028l-.722-.187V3.467c1.122.11 1.879.714 2.07 1.616h1.47c-.166-1.6-1.54-2.748-3.54-2.875V1H7.591v1.233c-1.939.23-3.27 1.472-3.27 3.156 0 1.454.966 2.483 2.661 2.917l.61.162v4.031c-1.149-.17-1.94-.8-2.131-1.718H4zm3.391-3.836c-1.043-.263-1.6-.825-1.6-1.616 0-.944.704-1.641 1.8-1.828v3.495l-.2-.05zm1.591 1.872c1.287.323 1.852.859 1.852 1.769 0 1.097-.826 1.828-2.2 1.939V8.73l.348.086z"/>
                        </svg>${product.price}</p>
                </div>
            </li>

        </ul>
        <div class="container card-body">
            <form class="form mb-2" method="post" name="amountForm">
                <div class="mb-2">
                    <label for="amount" class="form-label fw-bold">Quantity:</label>
                    <input type="number" min="0" step="1" id="amount" name="amount" required>
                </div>
                <div>
                    <button type="button" id="addProduct" class="btn btn-success">AddToCart</button>
                </div>
            </form>
            @if(user.hasInventoryPermission(shopId))
                <a href="/shops/${shopId}/${product.serialNumber}" class="btn btn-warning">Edit Product</a>
            @endif
        </div>
    </div>
    <div id="responsePlace" class="container mt-5 col-4 visually-hidden">
        <p class="fs-5"></p>
    </div>
</main>
</body>


<script>
    const socket = new WebSocket("ws://localhost:${Presentation.Main.port}/users/${user.getUsername()}/addToCart")
    socket.onopen = function (event){
        console.log("connected to add product web-socket in port: ",${Presentation.Main.port})
    }
    const username = "${user.getUsername()}"
    const shopID = "${shopId}"
    const serial = "${product.serialNumber}"
    const add_button = document.querySelector('#addProduct');
    const f = function () {
        const form = document.amountForm;
        const message = {username: username, shopID: shopID, serialNumber: serial, quantity: form.amount.value};
        socket.send(JSON.stringify(message));
    }
    add_button.addEventListener('click', f);
    socket.onmessage = async function (event) {
        const response = JSON.parse(event.data);
        if (!response.errorMessage) {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = "The product wad added to cart successfully";
            toUpdate.classList.add("border", "border-success", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
            await new Promise(r => setTimeout(r, 1500));
        }
        else {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = response.errorMessage;
            toUpdate.classList.add("border", "border-danger", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
        }
    }
</script>
</html>