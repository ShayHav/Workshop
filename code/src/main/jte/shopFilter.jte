@param String action
@param String filteredName

<div class="my-4 mx-4 mb-5 small container">
    <form method="get" id="filterShops" class="form border rounded border-1 w-75" action="${action}">
        <div class="row mb-2">
            <div class="col-4">
                <div class="mt-3 mx-3 mb-3">
                    <label for="shopName" class="form-label">Shop name</label>
                    <input type="text" name="shopName" id="shopName" class="form-control" placeholder="shop name to filter" value="${filteredName}">
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
