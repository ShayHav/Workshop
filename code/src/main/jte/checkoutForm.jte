@import Presentation.Model.PresentationUser
@param PresentationUser user

@template.navBar(user = user, title = "Checkout")

<main class="container mt-3">
    <h1 class="fs-2">Checkout</h1>
    <div class="my-3">
        <h2 class="fs-5">Please fill in the following details for checkout</h2>
    </div>
    <form class="form" method="post" name="checkoutForm">
        <div class="mb-3">
            <label for="fullName" class="form-label">Full name</label>
            <input type="text" id="fullName" name="fullName" class="form-control form-text" placeholder="Full name"
                   required>
        </div>
        <div class="mb-3">
            <label for="address" class="form-label">Address</label>
            <input type="text" inputmode="tel" id="address" name="address" required class="form-control form-text" placeholder="Address">
        </div>

        <div class="mb-3">
            <label for="phoneNumber" class="form-label">Phone Number</label>
            <input type="text" inputmode="tel" id="phoneNumber" name="phoneNumber" required class="form-control form-text" placeholder="Phone Number">
        </div>
        <div class="mb-3">
            <label for="cardNumber" class="form-label">Card Number</label>
            <input type="text" inputmode="numeric" id="cardNumber" name="cardNumber" required class="form-control form-text" placeholder="Card Number">
        </div>
        <div class="mb-3">
            <label class="form-label">Expiration Date</label>
            <label for="month" class="form-label">Month</label>
            <input type="text" inputmode="numeric" min="1" max="12" id="month" name="month" required class="form-control form-text" placeholder="month">
            <label for="year" class="form-label">Year</label>
            <input type="text" inputmode="numeric" min="2022" id="year" name="year" required class="form-control form-text" placeholder="year">
        </div>
        <div class="row">
            <div class="col-6">
                <button name="finishCheckout" id="finishCheckout" type="button" class="btn btn-lg btn-success">Finish Checkout</button>
                <a href="users/${user.getUsername()}/cart" class="btn btn-lg btn-danger">Cancel</a>
            </div>
        </div>
    </form>
    <div id="responsePlace" class="container mt-5 col-4 visually-hidden">
        <p class="fs-5"></p>
    </div>
</main>
<script>
    const socket = new WebSocket("ws://localhost:${Presentation.Main.port}users/${user.getUsername()}/cart/checkout")
    socket.onopen = function (event){
        console.log("connected to checkout form web-socket in port: ",${Presentation.Main.port})
    }
    const username = "${user.getUsername()}"
    const checkout_button = document.querySelector('#finishCheckout');
    const add = function () {
        const form = document.checkoutForm;
        const message = {username:username,fullName:form.fullName.value, address:form.address.value, phoneNumber:form.phoneNumber.value, cardNumber: form.cardNumber.value, month:form.month.value, year:form.year.value};
        socket.send(JSON.stringify(message));
    }
    checkout_button.addEventListener('click', add);
    socket.onmessage = async function (event) {
        const response = JSON.parse(event.data);
        if(response.errorMessage){
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = response.errorMessage;
            toUpdate.classList.add("border", "border-danger", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
        }
        else if(response.value.size()>0){
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = response.value;
            toUpdate.classList.add("border", "border-danger", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
        }
        else {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = "The checkout completed successfully";
            toUpdate.classList.add("border", "border-success", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
            await new Promise(r => setTimeout(r, 1500));
        }
    }
</script>
</body>
</html>
