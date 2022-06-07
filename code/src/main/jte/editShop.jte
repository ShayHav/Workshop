@import Presentation.Main
@import Presentation.Model.PresentationShop
@import Presentation.Model.PresentationUser
@import domain.shop.PurchasePolicys.PurchaseRule
@import domain.shop.discount.Discount
@param PresentationShop shop
@param PresentationUser user

@template.navBar(user = user, title = "Edit shop: " + shop.name)

<main class="container mt-5">
    <div class="mb-5">
        <h1 class="fs-2">Editing shop: ${shop.name}</h1>
    </div>
    <div class="row mb-3">
        <div class="col-6">
            <h2 class="fs-4">Shop Owners</h2>
            <ul id="ownerList" class="list-group">
                @for(PresentationUser owner: shop.managers)
                    <li id="${owner.getUsername()}" class="list-group-item">
                        <h3 class="fs-5">${owner.getUsername()}</h3>
                        @if(user.isMyAppointed(shop.getID(), owner) || shop.isFounder(user))
                            <form class="remove-owner">
                                <input class="visually-hidden" name="ownerID" type="text" value="${owner.getUsername()}"
                                       readonly>
                                <button class="btn btn-danger remove-owner-btn">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                         class="bi bi-trash3-fill" viewBox="0 0 16 16">
                                        <path d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z"/>
                                    </svg>
                                </button>
                            </form>
                        @endif
                    </li>
                @endfor
                <li class="list-group-item">
                    <form>
                        <div class="row align-items-end">
                            <div class="col-8">
                                <label for="ownerID">Owner's Username</label>
                                <input placeholder="username to appoint" id="ownerID" name="ownerID"
                                       class="form-text form-control" type="text" required>
                            </div>
                            <div class="col-4 mb-0">
                                <button type="button" id="add-owner-btn" class="btn btn-success">Add Owner</button>
                            </div>
                        </div>
                    </form>
                </li>
            </ul>
        </div>

        <div class="col-6">
            <h2 class="fs-4">Shop Managers</h2>
            <ul id="managersList" class="list-group">
                @for(PresentationUser manager: shop.managers)
                    <li id="${manager.getUsername()}" class="list-group-item">
                        <h3 class="fs-5">${manager.getUsername()}</h3>
                        @if(user.isMyAppointed(shop.getID(), manager) || shop.isFounder(user))
                            <form class="remove-manager">
                                <input class="visually-hidden" name="managerID" type="text"
                                       value="${manager.getUsername()}"
                                       readonly>
                                <button class="btn btn-danger remove-manager-btn">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                         class="bi bi-trash3-fill" viewBox="0 0 16 16">
                                        <path d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z"/>
                                    </svg>
                                </button>
                            </form>
                        @endif
                    </li>
                @endfor
                <li class="list-group-item">
                    <form>
                        <div class="row align-items-end">
                            <div class="col-8">
                                <label for="managerID">Manager's Username</label>
                                <input id="managerID" name="managerID" class="form-text form-control" type="text"
                                       required>
                            </div>
                            <div class="col-4 mb-0">
                                <button type="button" id="add-manager-btn" class="btn btn-success">Add Manager</button>
                            </div>
                        </div>
                    </form>
                </li>
            </ul>
        </div>
    </div>
    <div class="mb-3 mt-5">
        <h3 class="mb-2">Purchase Rules management</h3>
        <ul class="list-group mb-2">
            <form class="form mb-2" method="post" action="/shops/${shop.id}/combineRules" id="rulesForm">
            @for(PurchaseRule rule : shop.getAllRules())
                <li class="list-group-item">
                    <div class="form-check">
                        <input class="form-check-input" name="rule" type="checkbox" value="${rule.getID()}" id="rule${rule.getID()}">
                        <label class="form-check-label" for="rule${rule.getID()}">
                            ${rule.toString()}
                        </label>
                    </div>
                </li>
            @endfor
                @if(shop.getAllRules().size() > 1)
                <li class="list-group-item">
                    <input id="combineRuleType" type="hidden" name="combineRuleType">
                    <div class="row">
                        <div class="col-3">
                            <button id="combineRuleOr" type="submit" class="btn btn-warning">Combine Rules: Or</button>
                        </div>
                        <div class="col-3">
                            <button id="combineRuleAnd" type="submit" class="btn btn-info">Combine Rules: And</button>
                        </div>
                    </div>
                </li>
                @endif
            </form>
            <li class="list-group-item">

                <form class="form" id="addRuleForm" action="/shops/${shop.id}/addRule" method="post">
                    <div class="row">
                    <span class="col-4">
                    <select id="ruleType" class="form-select" name="ruleType" aria-label="Select Rule Type">
                        <option selected>Select Rule Type</option>
                        <option value="ProhibitPurchaseHour">Prohibit purchase hour</option>
                        <option value="minimumQuantity">Minimum Quantity</option>
                        <option value="maximusQuantity">Maximum Quantity</option>
                    </select>
                    </span>
                        <span class="col-4">
                    <select id="ruleBase" class="form-select" name="ruleBase" aria-label="Select Rule Type">
                        <option selected>Select Rule Base</option>
                        <option value="allProduct">All products in Shop</option>
                        <option value="category">All products in Category</option>
                        <option value="product">Single product</option>
                    </select>
                    </span>
                    </div>
                    <div class="row my-3">
                        <div id="rulesParams" class="visually-hidden col-6"></div>
                        <div class="visually-hidden col-6">
                            <label id='productOrCategoryLabel' for="productOrCategory" class="form-label"></label>
                            <input type="text" class="form-control" name="productOrCategory" id="productOrCategory">
                        </div>
                    </div>
                    <button class="btn btn-success" type="submit">Add Rule</button>
                </form>
            </li>
        </ul>
    </div>
    <div class="mt-5 mb-4">
        <h3>Discount Policies management</h3>
        <ul class="list-group mb-2 nt-2">
            <form class="form mb-2" method="post" action="/shops/${shop.id}/composeDiscounts" id="rulesForm">
                @for(Discount d: shop.discountPolicy.getAllDistinctDiscounts())
                    <li class="list-group-item">
                        <div class="form-check">
                            <input class="form-check-input" name="rule" type="checkbox" value="${d.getID()}"
                                   id="rule${d.getID()}">
                            <label class="form-check-label" for="rule${d.getID()}">
                                ${d.toString()}
                            </label>
                        </div>
                    </li>
                @endfor
                <li class="list-group-item col-4">
                    <select id="discountCompose" class="form-select" name="discountCompose" aria-label="Select Discount composition operator">
                        <option selected>Select Discount composition operator</option>
                        <option value="and">And</option>
                        <option value="or">Or</option>
                        <option value="xor">Xor</option>
                    </select>
                </li>
                <li class="list-group-item">
                    <button type="submit" class="btn btn-warning">Compose Discounts</button>
                </li>
            </form>
            <li class="list-group-item">
                <form class="form" id="addDiscountForm" action="/shops/${shop.id}/addDiscount" method="post">
                    <div class="row">
                    <span class="col-4">
                    <select id="discountType" class="form-select" name="discountType" aria-label="Select Discount Type">
                        <option selected>Select Discount Type</option>
                        <option value="regularDiscount">Regular Discount</option>
                        <option value="bundleDiscount">Bundle Discount</option>
                        <option value="conditionalDiscount">Conditional Discount</option>
                    </select>
                    </span>
                        <span class="col-4">
                    <select id="discountBase" class="form-select" name="discountBase" aria-label="Select Discount Type">
                        <option selected>Select Discount Base</option>
                        <option value="allProduct">All products in Shop</option>
                        <option value="category">All products in Category</option>
                        <option value="product">Single product</option>
                    </select>
                    </span>
                    </div>
                    <div class="row my-3">
                        <div id="discountParams" class="col-6">
                        </div>
                        <div class="visually-hidden col-6">
                            <label id='productOrCategoryDiscountLabel' for="productOrCategoryDiscount" class="form-label"></label>
                            <input type="text" class="form-control" name="productOrCategoryDiscount" id="productOrCategoryDiscount">
                        </div>
                    </div>
                    <button class="btn btn-success" type="submit">Add Discount</button>
                </form>
            </li>
        </ul>
    </div>
</main>

</body>

<script>
    const socket = new WebSocket('ws://localhost:${Main.port}/shops/${shop.id}');
    socket.onopen = () => console.log("trying to connect to sever using websocket")
    const removeManager = function (event) {
        const message = {
            type: 'removeManager',
            requestingUser: '${user.getUsername()}',
            subject: this.managerID
        }
        socket.send(JSON.stringify(message));
    }
    const removeOwner = function (event) {
        const message = {
            type: 'removeOwner',
            requestingUser: '${user.getUsername()}',
            subject: this.managerID
        }
        socket.send(JSON.stringify(message));
    }
    const addOwner = function (event) {
        const message = {
            type: 'addOwner',
            requestingUser: '${user.getUsername()}',
            subject: document.querySelector('#ownerID').value
        }
        console.log(message);
        socket.send(JSON.stringify(message));
    }
    const addManager = function (event) {
        const message = {
            type: 'addManager',
            requestingUser: '${user.getUsername()}',
            subject: document.querySelector('#managerID').value
        }
        socket.send(JSON.stringify(message));
    }
    document.querySelectorAll('.remove-manager-btn').forEach(btn => btn.onclick = removeManager);
    document.querySelectorAll('.remove-owner-btn').forEach(btn => btn.onclick = removeOwner);

    function addManagerNode(message) {
        let node = document.createElement('li');
        node.id = message.addedManager;
        node.classList.add('list-group-item');
        let headline = document.createElement('h3');
        headline.classList.add('fs-5');
        headline.textContent = message.addedManager;
        let removeform = document.createElement('form');
        removeform.classList.add('remove-manager');
        let input = document.createElement('input');
        input.classList.add('visually-hidden');
        input.value = message.addedManager;
        input.type = 'text';
        input.readOnly = true;
        input.name = 'managerID';
        let button = document.createElement('button');
        button.classList.add('btn', 'btn-danger');
        button.textContent = 'Remove';
        button.onclick = removeManager;
        node.append(headline, removeform);
        removeform.append(input, button);
        document.getElementById('managersList').prepend(node);
    }

    function addOwnerNode(message) {
        console.log(message)
        let node = document.createElement('li');
        node.id = message.addedOwner;
        node.classList.add('list-group-item');
        let headline = document.createElement('h3');
        headline.classList.add('fs-5');
        headline.textContent = message.addedOwner;
        let removeform = document.createElement('form');
        removeform.classList.add('remove-owner');
        let input = document.createElement('input');
        input.classList.add('visually-hidden');
        input.value = message.addedOwner;
        input.type = 'text';
        input.readOnly = true;
        input.name = 'ownerID';
        let button = document.createElement('button');
        button.classList.add('btn', 'btn-danger');
        button.textContent = 'Remove';
        button.onclick = removeOwner;
        node.append(headline, removeform);
        removeform.append(input, button);
        document.getElementById('ownerList').prepend(node);
    }


    document.querySelector("#add-manager-btn").onclick = addManager;
    document.querySelector('#add-owner-btn').onclick = addOwner;
    socket.onmessage = async (e) => {
        const message = JSON.parse(e.data);
        if (!message.errorMessage)
            message.type === 'removeManager' ? document.getElementById(message.removedUser).remove() :
                message.type === 'removeOwner' ? document.getElementById(message.removedUser).remove() :
                    message.type === 'addManager' ? addManagerNode(message) :
                        message.type === 'addOwner' ? addOwnerNode(message) : undefined
        else
            document.getElementById(message.removedUser).classList.add('border', 'border-red');
    }

    // function checkBoxLimit() {
    //     var checkBoxGroup = document.forms['rulesForm']['rule'];
    //     var limit = 2;
    //     for (var i = 0; i < checkBoxGroup.length; i++) {
    //         checkBoxGroup[i].onclick = function() {
    //             var checkedcount = 0;
    //             for (var i = 0; i < checkBoxGroup.length; i++) {
    //                 checkedcount += (checkBoxGroup[i].checked) ? 1 : 0;
    //             }
    //             if (checkedcount > limit) {
    //                 console.log("You can select maximum of " + limit + " checkboxes.");
    //                 alert("You can select maximum of " + limit + " checkboxes.");
    //                 this.checked = false;
    //             }
    //         }
    //     }
    // }
    //
    // checkBoxLimit()

    function addProhibitedRule() {
        const div = document.createElement('div');
        div.classList.add('row', 'mt-3')
        const fromTimeSelector = document.createElement('select');
        const fromTimeSpan = document.createElement('span');
        fromTimeSpan.classList.add('col-4');
        fromTimeSelector.name = 'fromHour'
        fromTimeSelector.classList.add('form-select')
        fromTimeSpan.append(fromTimeSelector)
        let defaultOption = document.createElement('option');
        defaultOption.textContent = 'Starting Hour';
        fromTimeSelector.append(defaultOption);
        for (let i = 0; i < 24; i++) {
            const option = document.createElement('option');
            option.value = i.toString();
            option.textContent = i.toString();
            fromTimeSelector.append(option);
        }

        const toTimeSelector = document.createElement('select');
        toTimeSelector.name = 'toHour'
        toTimeSelector.classList.add('form-select')
        const toTimeSpan = document.createElement('span');
        toTimeSpan.classList.add('col-4');
        toTimeSpan.append(toTimeSelector)

        defaultOption = document.createElement('option');
        defaultOption.textContent = 'Ending Hour';
        toTimeSelector.append(defaultOption);
        for (let i = 0; i < 24; i++) {
            const option = document.createElement('option');
            option.value = i.toString();
            option.textContent = i.toString();
            toTimeSelector.append(option);
        }

        div.append(fromTimeSpan);
        div.append(toTimeSpan);
        let upperDiv = document.querySelector('#rulesParams');
        upperDiv.append(div)
        upperDiv.classList.remove('visually-hidden');
    }

    function addMinimumRule() {
        let minimum_div = document.querySelector('#rulesParams');

        const upperDiv = document.createElement("div");
        upperDiv.classList.add("row");
        minimum_div.append(upperDiv);

        let div = document.createElement("div");
        div.classList.add("col")

        let minimum_input = document.createElement('input');
        minimum_input.type = "number";
        minimum_input.min = '0';
        minimum_input.max = '5000';
        minimum_input.step = '1';
        minimum_input.id = 'minimum';
        minimum_input.name = "minimum";
        minimum_input.required = true;
        minimum_input.classList.add('form-control');

        let minimum_label = document.createElement('label');
        minimum_label.for = 'minimum';
        minimum_label.classList.add('form-label');
        minimum_label.innerText = "Minimum Quantity";

        div.prepend(minimum_input);
        div.prepend(minimum_label);
        upperDiv.append(div);

        div = document.createElement("div");
        div.classList.add("col")
        const targetProduct = document.createElement("input");
        targetProduct.name = "targetProduct";
        targetProduct.type = 'text';
        targetProduct.id = "targetProduct";
        targetProduct.inputMode = "numeric";
        targetProduct.classList.add("form-control");

        const targetLabel = document.createElement("label");
        targetLabel.for = 'targetProduct';
        targetLabel.classList.add("form-label");
        targetLabel.textContent = 'Target Product';
        div.append(targetLabel);
        div.append(targetProduct);

        upperDiv.append(div);
        minimum_div.classList.remove("visually-hidden")
    }

    function addMaximumRule() {
        let maximum_div = document.querySelector('#rulesParams');
        const upperDiv = document.createElement("div");
        upperDiv.classList.add("row");
        maximum_div.append(upperDiv);

        let div = document.createElement("div");
        div.classList.add("col")
        let maximum_input = document.createElement('input');
        maximum_input.type = "number";
        maximum_input.min = '0';
        maximum_input.max = '5000';
        maximum_input.step = '1';
        maximum_input.id = 'maximum';
        maximum_input.name = "maximum";
        maximum_input.required = true;
        maximum_input.classList.add('form-control');

        let maximum_label = document.createElement('label');
        maximum_label.for = 'maximum';
        maximum_label.classList.add('form-label');
        maximum_label.innerText = "Maximum Quantity";

        div.prepend(maximum_input);
        div.prepend(maximum_label);
        upperDiv.append(div);

        div = document.createElement("div");
        div.classList.add("col")
        const targetProduct = document.createElement("input");
        targetProduct.name = "targetProduct";
        targetProduct.type = 'text';
        targetProduct.id = "targetProduct";
        targetProduct.inputMode = "numeric";
        targetProduct.classList.add("form-control");

        const targetLabel = document.createElement("label");
        targetLabel.for = 'targetProduct';
        targetLabel.classList.add("form-label");
        targetLabel.textContent = 'Target Product';
        div.append(targetLabel);
        div.append(targetProduct);

        upperDiv.append(div);
        maximum_div.classList.remove("visually-hidden")
    }

    function clearRuleParam() {
        const div = document.querySelector('#rulesParams');
        div.classList.add('visually-hidden');
        while (div.lastChild) {
            div.removeChild(div.lastChild);
        }
    }

    function clearDiscountParam() {
        const div = document.querySelector('#discountParams');
        div.classList.add('visually-hidden');
        while (div.lastChild) {
            div.removeChild(div.lastChild);
        }
    }

    function clearProductOrCategory() {
        let input = document.querySelector('#productOrCategory');
        input.value = '';
        input.parentElement.classList.add('visually-hidden');
    }

    function unclearProduct() {
        let input = document.querySelector('#productOrCategory');
        let label = document.querySelector('#productOrCategoryLabel');
        input.type = 'text'
        input.inputMode = 'numeric'
        label.textContent = "Product's serial number";
        input.value = '';
        input.parentElement.classList.remove('visually-hidden');
    }

    function unclearCategory() {
        let input = document.querySelector('#productOrCategory');
        let label = document.querySelector('#productOrCategoryLabel');
        label.textContent = "Category";
        input.type = 'text'
        input.value = '';
        input.parentElement.classList.remove('visually-hidden');
    }

    document.querySelector('#ruleType').addEventListener('change', (event) => {
        const value = event.target.value;
        clearRuleParam()
        value === 'ProhibitPurchaseHour' ? addProhibitedRule() :
            value === 'minimumQuantity' ? addMinimumRule() :
                value === 'maximumQuantity' ? addMaximumRule() : clearRuleParam()
    })

    document.querySelector('#ruleBase').onchange = function (event) {
        const value = event.target.value;

        value === 'allProduct' ? clearProductOrCategory() :
            value === 'category' ? unclearCategory() :
                value === 'product' ? unclearProduct() :
                    clearProductOrCategory();
    }

    document.querySelector('#combineRuleOr').onclick = (event)=> {
        console.log("or btn")
        document.querySelector('#combineRuleType').value = 'or';
        document.forms['rulesForm'].submit();
    };

    document.querySelector('#combineRuleAnd').onclick = (event)=> {
        console.log("and btn")
        document.querySelector('#combineRuleType').value = 'and';
        document.forms['rulesForm'].submit();
    };

    function addRegularDiscount(){
        let div = document.querySelector('#discountParams')

        let regular_input = document.createElement('input');
        regular_input.type = "number";
        regular_input.min = '0';
        regular_input.max = '100';
        regular_input.step = '0.5';
        regular_input.id = 'percentage';
        regular_input.name = "percentage";
        regular_input.required = true;
        regular_input.classList.add('form-control');

        let regular_label = document.createElement('label');
        regular_label.for = 'percentage';
        regular_label.classList.add('form-label');
        regular_label.innerText = "Percentage";

        div.append(regular_label);
        div.append(regular_input);
    }

    function addBundleDiscount(){
        let div = document.querySelector('#discountParams')

        let toBuy = document.createElement('input');
        toBuy.type = "number";
        toBuy.min = '0';
        toBuy.max = '5000';
        toBuy.step = '1';
        toBuy.id = 'amountToBuy';
        toBuy.name = "amountToBuy";
        toBuy.required = true;
        toBuy.classList.add('form-control');

        let toBuy_label = document.createElement('label');
        toBuy_label.for = 'amountToBuy';
        toBuy_label.classList.add('form-label');
        toBuy_label.innerText = "Amount to buy";

        let toGetFree = document.createElement('input');
        toGetFree.type = "number";
        toGetFree.min = '0';
        toGetFree.max = '1000';
        toGetFree.step = '1';
        toGetFree.id = 'amountToGetFree';
        toGetFree.name = "amountToGetFree";
        toGetFree.required = true;
        toGetFree.classList.add('form-control');

        let toGetFree_label = document.createElement('label');
        toGetFree_label.for = 'amountToGetFree';
        toGetFree_label.classList.add('form-label');
        toGetFree_label.innerText = "Amount to get free";

        div.append(toBuy_label);
        div.append(toBuy);
        div.append(toGetFree_label);
        div.append(toGetFree);
    }

    function addConditionalDiscount(){}

    function clearProductOrCategoryDiscount() {
        let input = document.querySelector('#productOrCategoryDiscount');
        input.value = '';
        input.parentElement.classList.add('visually-hidden');
    }

    function unclearDiscountProduct() {
        let input = document.querySelector('#productOrCategoryDiscount');
        let label = document.querySelector('#productOrCategoryDiscountLabel');
        input.type = 'text'
        input.inputMode = 'numeric'
        label.textContent = "Product's serial number";
        input.value = '';
        input.parentElement.classList.remove('visually-hidden');
    }

    function unclearDiscountCategory() {
        let input = document.querySelector('#productOrCategoryDiscount');
        let label = document.querySelector('#productOrCategoryDiscountLabel');
        label.textContent = "Category";
        input.type = 'text'
        input.value = '';
        input.parentElement.classList.remove('visually-hidden');
    }


    document.querySelector('#discountBase').onchange = function (event) {
        const value = event.target.value;

        value === 'allProduct' ? clearProductOrCategoryDiscount() :
            value === 'category' ? unclearDiscountCategory() :
                value === 'product' ? unclearDiscountProduct() :
                    clearProductOrCategoryDiscount();
    }

    document.querySelector('#discountType').addEventListener('change', (event) => {
        const value = event.target.value;
        clearRuleParam()
        value === 'regularDiscount' ? addRegularDiscount() :
            value === 'bundleDiscount' ? addBundleDiscount() :
                value === 'conditionalDiscount' ? addConditionalDiscount() : clearDiscountParam()
    })

</script>
</html>