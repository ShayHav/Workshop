@param String action
@param Double minPrice
@param Double maxPrice
@param String category

<div class="my-4 mx-4 mb-5 container">
    <form method="get" id="filterProducts" class="form border rounded border-1 w-75" action="${action}">
        <div class="row mb-2">
            <div class="col">
                <div class="mt-3 mx-3 mb-3">
                    <label for="minPrice" class="form-label">Min Price</label>
                    <input type="range" step="0.1" min="${minPrice}" max="${maxPrice}" class="form-range" id="minPrice" name="minPrice" value="0"
                           onChange="document.getElementById('rangemin').innerText = document.getElementById('minPrice').value">
                    <span id="rangemin">0<!-- Default value --></span>
                </div>
                <div class="mt-3 mx-3 mb-3">
                    <label for="maxPrice" class="form-label">Max Price</label>
                    <input type="range" step="0.1" min="${minPrice}" max="${maxPrice}" class="form-range" id="maxPrice" name="maxPrice" value="${maxPrice}"
                           onChange="document.getElementById('rangemax').innerText = document.getElementById('maxPrice').value">
                    <span id="rangemax">${maxPrice}<!-- Default value --></span>
                </div>
            </div>
            <div class="col">
                <div class="mt-3 mx-3 mb-3">
                    <label for="category" class="form-label">Category</label>
                    <input type="text" name="category" id="category" class="form-control" placeholder="category to filter" value="${category}">
                </div>
            </div>
        </div>
        <div class="row col-4">
            <div class="mb-2 mx-2 col">
                <button class="btn btn-primary" type="submit">Filter results</button>
            </div>
            <div class="mb-2 col">
                <button type="button" id="resetFilter" class="btn btn-primary">Reset Filter</button>
            </div>
        </div>
        <input class="visually-hidden" name="searchBy" value="">
    </form>
</div>

<script>
    const reset_button = document.getElementById('resetFilter');
    reset_button.addEventListener('click',()=> window.location.replace("${action}"));
</script>
