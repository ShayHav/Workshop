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
    <div>
        <h3 class="mb-2">Purchase Rules management</h3>
        <ul class="list-group">
            @for()
                <li class="list-group-item">

                </li>
            @endfor
            <li>
                <form>

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
            message.type === 'removeManager' ? document.getElementById(message.removedUser).remove() :
                message.type === 'removeOwner' ? document.getElementById(message.removedUser).remove() :
                    message.type === 'addManager' ? addManagerNode(message) :
                        message.type === 'addOwner' ? addOwnerNode(message) : undefined
        else
            document.getElementById(message.removedUser).classList.add('border', 'border-red');

    }
</script>
</html>