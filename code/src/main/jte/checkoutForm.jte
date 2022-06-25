@import Presentation.Model.PresentationUser
@param PresentationUser user

@template.navBar(user = user, title = "Checkout")

<main class="container mt-3">
    <h1 class="fs-2">Checkout</h1>
    <div class="mb-3">
        <h2 class="fs-5">Please fill in the following details for checkout</h2>
    </div>
    <form class="form col-6" method="post" name="checkoutForm">
        <div class="border-bottom border-1">
            <div class="mb-3">
                <label for="fullName" class="form-label">Full name</label>
                <input type="text" id="fullName" name="fullName" class="form-control form-text" placeholder="Full name"
                       required>
            </div>
            <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <input type="text" inputmode="text" id="address" name="address" required class="form-control form-text"
                       placeholder="Address">
            </div>
            <div class="mb-3">
                <label for="city" class="form-label">city</label>
                <input type="text" inputmode="text" id="city" name="city" required class="form-control form-text"
                       placeholder="City">
            </div>
            <div class="mb-3">
                <label for="country" class="form-label">country</label>
                <input type="text" inputmode="text" id="country" name="country" required class="form-control form-text"
                       placeholder="Country">
            </div>

            <div class="mb-3">
                <label for="zip" class="form-label">zip</label>
                <input type="text" inputmode="numeric" id="zip" name="zip" required class="form-control form-text"
                       placeholder="Zip Code">
            </div>

            <div class="mb-4">
                <label for="phoneNumber" class="form-label">Phone Number</label>
                <input type="text" inputmode="tel" id="phoneNumber" name="phoneNumber" required
                       class="form-control form-text" placeholder="Phone Number">
            </div>
        </div>
        <div class="mb-3">
            <p class="fw-bold mt-2">Payment Details</p>
            <div class="mb-3">
                <label for="cardNumber" class="form-label">Card Number</label>
                <input type="text" inputmode="numeric" id="cardNumber" name="cardNumber" required
                       class="form-control form-text" placeholder="Card Number">
            </div>
            <div class="mb-3">
                <label for="ccv" class="form-label">CCV</label>
                <input type="text" inputmode="numeric" id="ccv" name="ccv" required
                       class="form-control form-text" placeholder="ccv">
            </div>
            <p class="form-label">Expiration Date</p>
            <div class="row mb-3">
                <div class="col-3">
                    <select name="year" class="form-select" aria-label="year">
                        <option selected>Year</option>
                        @for (int i=2022; i<=2030; i++)
                            <option value="${i}">${i}</option>
                        @endfor
                    </select>
                </div>
                <div class="col-3">
                    <select name="month" class="form-select" aria-label="month">
                        <option selected>Month</option>
                        @for (int i=1; i<=12; i++)
                            <option value="${i}">${i}</option>
                        @endfor
                    </select>
                </div>
            </div>
        </div>
        <div>
            <button name="finishCheckout" id="finishCheckout" type="button" class="btn btn-success mb-2">Finish
                Checkout
            </button>
            <a href="users/${user.getUsername()}/cart" class="mb-2 btn btn-danger">Cancel</a>
        </div>
    </form>
    <div id="responsePlace" class="container mt-3 col-4 visually-hidden">
        <p class="fs-5"></p>
    </div>
</main>
<script>
    const socket = new WebSocket("ws://localhost:${Presentation.Main.port}/users/${user.getUsername()}/cart/checkout")
    socket.onopen = function (event) {
        console.log("connected to checkout form web-socket in port: ",${Presentation.Main.port})
    }
    const username = "${user.getUsername()}"
    const checkout_button = document.querySelector('#finishCheckout');
    const add = function () {
        const form = document.checkoutForm;
        const message = {
            fullName: form.fullName.value,
            address: form.address.value,
            phoneNumber: form.phoneNumber.value,
            cardNumber: form.cardNumber.value,
            month: form.month.value,
            year: form.year.value
        };
        socket.send(JSON.stringify(message));
    }
    checkout_button.addEventListener('click', add);
    socket.onmessage = async function (event) {
        const error = JSON.parse(event.data);
        if (error === "") {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = "The checkout completed successfully";
            toUpdate.classList.add("border", "border-success", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
            await new Promise(r => setTimeout(r, 2000));
            window.location.replace("/users/${user.getUsername()}/cart");
        } else {
            const toUpdate = document.getElementById("responsePlace");
            toUpdate.firstChild.textContent = error;
            toUpdate.classList.add("border", "border-danger", "border-3", "rounded");
            toUpdate.classList.remove("visually-hidden");
        }

    }
</script>
</body>
</html>
