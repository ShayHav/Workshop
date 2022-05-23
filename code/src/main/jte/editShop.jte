@import Presentation.Main
@import Presentation.Model.PresentationShop
@import Presentation.Model.PresentationUser
@param PresentationShop shop
@param PresentationUser user

@template.navBar(user = user, title = "Edit shop: " + shop.name)

<main class="container mt-5">
    <div>
        <h1 class="fs-2">Editing shop ${shop.name}</h1>
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
                            <label for="managerID">ManagerID</label>
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
    const addManager = function (event) {
        const message = {
            type: 'addManager',
            requestingUser: '${user.getUsername()}',
            subject: document.querySelector('#managerID').value
        }
        socket.send(JSON.stringify(message));
    }
    document.querySelectorAll('.remove-manager-btn').forEach(btn=> btn.onclick= removeManager);
    function addManagerNode(message) {
        let node = document.createEvent('li');
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
    document.querySelector("#add-manager-btn").onclick = addManager;
    socket.onmessage = async (e) => {
        const message = JSON.parse(e.data);
        if (!message.errorMessage) {
            if (message.type === 'removeManager')
                document.getElementById(message.removedUser).remove();
            else if (message.type === 'addManager') {
                addManagerNode(message);
            }
        } else {
            document.getElementById(message.removedUser).classList.add('border', 'border-red');
        }
    }
</script>
</html>