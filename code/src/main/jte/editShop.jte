@import Presentation.Main
@import Presentation.Model.PresentationShop
@import Presentation.Model.PresentationUser
@param PresentationShop shop
@param PresentationUser user

@template.navBar(user = user, title = "Edit shop: " + shop.name)

<main class="container mt-5">
    <div class="mb-5">
        <h1 class="fs-2">Editing shop: ${shop.name}</h1>
    </div>

    <div class="col-6 mb-3">
        <h2 class="fs-4">Shop Owners</h2>
        <ul id="ownerList" class="list-group">
            @for(PresentationUser owner: shop.managers)
                <li id="${owner.getUsername()}" class="list-group-item">
                    <h3 class="fs-5">${owner.getUsername()}</h3>
                    @if(user.isMyApointed(shop.getID(), owner))
                        <form class="remove-owner">
                            <input class="visually-hidden" name="ownerID" type="text" value="${owner.getUsername()}"
                                   readonly>
                            <button class="btn btn-danger remove-owner-btn">Remove</button>
                        </form>
                    @endif
                </li>
            @endfor
            <li class="list-group-item">
                <form>
                    <div class="row align-items-end">
                        <div class="col-8">
                            <label for="ownerID">Owner's Username</label>
                            <input id="ownerID" name="ownerID" class="form-text form-control" type="text" required>
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
                    @if(user.isMyApointed(shop.getID(), manager))
                        <form class="remove-manager">
                            <input class="visually-hidden" name="managerID" type="text" value="${manager.getUsername()}"
                                   readonly>
                            <button class="btn btn-danger remove-manager-btn">Remove</button>
                        </form>
                    @endif
                </li>
            @endfor
            <li class="list-group-item">
                <form>
                    <div class="row align-items-end">
                        <div class="col-8">
                            <label for="managerID">Manager's Username</label>
                            <input id="managerID" name="managerID" class="form-text form-control" type="text" required>
                        </div>
                        <div class="col-4 mb-0">
                            <button type="button" id="add-manager-btn" class="btn btn-success">Add Manager</button>
                        </div>
                    </div>
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
    document.querySelectorAll('.remove-manager-btn').forEach(btn=> btn.onclick= removeManager);
    document.querySelectorAll('.remove-owner-btn').forEach(btn => btn.onclick=removeOwner);
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
        document.getElementById('managersList').appendChild(node);
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
        document.getElementById('ownerList').appendChild(node);
    }


    document.querySelector("#add-manager-btn").onclick = addManager;
    document.querySelector('#add-owner-btn').onclick = addOwner;
    socket.onmessage = async (e) => {
        const message = JSON.parse(e.data);
        if (!message.errorMessage)
            message.type === 'removeManager' ?  document.getElementById(message.removedUser).remove() :
            message.type === 'removeOwner' ? document.getElementById(message.removedUser).remove() :
            message.type === 'addManager' ? addManagerNode(message) :
            message.type === 'addOwner' ? addOwnerNode(message) : undefined
        else
            document.getElementById(message.removedUser).classList.add('border', 'border-red');

    }
</script>
</html>