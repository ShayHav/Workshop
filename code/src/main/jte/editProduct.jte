@param Presentation.Model.PresentationProduct product
@param Presentation.Model.PresentationUser user
@param int shopID
@template.navBar(user = user, title = product.name)
<main class="container align-content-center mb-4">
    <div class="container col-5 mt-5  border">
        <form class="form" method="post" action="/shops/${shopID}/${product.serialNumber}/edit">
            <div class="mb-3">
                <label for="name" class="form-label">SerialNumber</label>
                <input type="text" id="name" class="form-control form-text" value="${product.serialNumber}" readonly>
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Product name</label>
                <input type="text" id="name" name="name" required class="form-control form-text" value="${product.name}">
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea id="description" name="description" class="form-control" rows="3">
                    ${product.description}
                </textarea>
            </div>
            <div class="mb-3">
                <label for="category" class="form-label">Product name</label>
                <input type="text" id="category" name="category" required class="form-control form-text" value="${product.category}">
            </div>
            <div class="mb-3">
                <label for="price" class="form-label">Product name</label>
                <input type="number" min="0" step="0.1" id="price" name="price" required class="form-control form-text" value="${product.price}">
            </div>
            <div class="mb-3">
                <label for="amount" class="form-label">Product name</label>
                <input type="number" min="0" step="1" id="amount" name="amount" required class="form-control form-text" value="${product.amount}">
            </div>
            <div class="row mb-4">
                <span class="me-2">
                    <button type="submit" class="btn btn-success">Save</button>
                    <a href="/shops/${shopID}" class="btn btn-danger">Cancel</a>
                </span>
            </div>
        </form>
    </div>
</main>
</body>
</html>