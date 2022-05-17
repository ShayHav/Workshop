@import Presentation.Model.PresentationUser
@param PresentationUser user
@param int shopId

@template.navBar(user = user, title = "Add Product")

<main class="container mt-3">
    <h1 class="fs-2">Add Product</h1>
    <div class="my-3">
    <h2 class="fs-5">Please fill in the details of the product you wish to add to this store</h2>
    </div>
    <form class="form" method="post" action="/shops/${shopId}/addProduct">
        <div class="mb-3">
            <label for="serialNumber" class="form-label">SerialNumber</label>
            <input type="text" inputmode="numeric" id="serialNumber" name="serialNumber" class="form-control form-text" placeholder="Serial Number"
                   required>
        </div>
        <div class="mb-3">
            <label for="name" class="form-label">Product name</label>
            <input type="text" id="name" name="name" required class="form-control form-text" placeholder="Product Name">
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea id="description" name="description" class="form-control" rows="3" required
                      placeholder="Product Description">
                </textarea>
        </div>
        <div class="mb-3">
            <label for="category" class="form-label">Category</label>
            <input type="text" id="category" name="category" required class="form-control form-text"
                   placeholder="Product category">
        </div>
        <div class="mb-3">
            <label for="price" class="form-label">Price</label>
            <input type="number" min="0" step="0.1" id="price" name="price" required class="form-control form-text">
        </div>
        <div class="mb-3">
            <label for="amount" class="form-label">Quantity</label>
            <input type="number" min="0" step="1" id="amount" name="amount" required class="form-control form-text">
        </div>
        <div class="row">
            <div class="col-6">
                <button type="submit" class="btn btn-lg btn-success">Save</button>
                <a href="/shops/${shopId}" class="btn btn-lg btn-danger">Cancel</a>
            </div>
        </div>
    </form>
</main>
</body>
</html>
