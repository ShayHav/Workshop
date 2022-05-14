@import Presentation.Model.PresentationUser
@param PresentationUser user
@param int shopId

@template.navBar(user = user, title = "Add Product")

    <main class="container mt-3">
        <form class="form">
            <form class="form" method="post" action="/shops/${shopId}/addProduct">
                <div class="mb-3">
                    <label for="name" class="form-label">SerialNumber</label>
                    <input type="text" inputmode="numeric" id="name" class="form-control form-text" placeholder="Serial Number" required>
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Product name</label>
                    <input type="text" id="name" name="name" required class="form-control form-text" placeholder="Product Name">
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label">Description</label>
                    <textarea id="description" name="description" class="form-control" rows="3" required placeholder="Product Description">
                </textarea>
                </div>
                <div class="mb-3">
                    <label for="category" class="form-label">Product name</label>
                    <input type="text" id="category" name="category" required class="form-control form-text" placeholder="Product category">
                </div>
                <div class="mb-3">
                    <label for="price" class="form-label">Product name</label>
                    <input type="number" min="0" step="0.1" id="price" name="price" required class="form-control form-text" >
                </div>
                <div class="mb-3">
                    <label for="amount" class="form-label">Product name</label>
                    <input type="number" min="0" step="1" id="amount" name="amount" required class="form-control form-text">
                </div>
                <div class="row">
                <span class="me-2">
                    <button class="btn btn-success">Save</button>
                </span>
                    <a href="/shops/${shopId}" class="btn btn-danger">Cancel</a>
                </div>
            </form>
        </form>
    </main>
</body>
</html>
