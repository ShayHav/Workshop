@param String action
@param Double minPrice
@param Double maxPrice
@param String minDate
@param String maxDate

<div class="my-4 mx-4 mb-5 small container-sm">
    <form method="get" id="filterOrders" class="form border rounded border-1 w-75" action="${action}">
        <div class="row mb-2">
            <div class="col-3">
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
            <div class="col-4">
                <div class="mt-3 mx-3 mb-3">
                    <label for="minDate" class="form-label">Min Date</label>
                    <input type="date" name="minDate" id="minDate" class="form-control" value="${minDate}">
                </div>
                <div class="mt-3 mx-3 mb-3">
                    <label for="maxDate" class="form-label">Max Date</label>
                    <input type="date" name="maxDate" id="maxDate" class="form-control" value="${maxDate}">
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
    </form>
</div>

<script>
    const reset_button = document.getElementById('resetFilter');
    reset_button.addEventListener('click',()=> window.location.replace("${action}"));
</script>
