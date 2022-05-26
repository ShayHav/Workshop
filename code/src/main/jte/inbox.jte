@import Presentation.Main
@import Presentation.Model.PresentationUser
@import domain.notifications.Message
@import java.util.List
@param PresentationUser user

@template.navBar(user = user, title = "inbox")

<main class="container mt-3">
    <div class="my-3">
        <h1>Messages</h1>
    </div>

    <div class="row">
        <div class="col-4">
            <div id="messagesPlace" class="list-group">
            </div>
        </div>
        <div class="col-8">
            <div id="messageView" class="visually-hidden">
                <h3>From: <span id="sender"></span></h3>
                <p id="messageContent" class="fs-4"></p>
            </div>
        </div>
    </div>
</main>

</body>
<script>
    messages = [];
    const socket = new WebSocket('ws://localhost:${Main.port}/${user.getUsername()}/messages');
    socket.onmessage = function (event){
        const message = JSON.parse(event.data);
        messages.push(message);

        let newBtn = document.createElement('button');
        newBtn.classList.add('list-group-item', 'list-group-item-action');
        if(!message.readed){
            newBtn.classList.add('fw-bold');
        }
        newBtn.innerText = (message.content.substr(0, 15) + '...');
        document.querySelector('#messagesPlace').prepend(newBtn);
        newBtn.addEventListener('onclick', () => {viewMessage(message, newBtn)});
    }

    const viewMessage = (message, btn) => {
        document.querySelector("#sender").textContent = message.sender;
        document.querySelector('#messageContent').value = message.content;
        document.querySelector("#messageView").classList.remove("visually-hidden");
        btn.classList.remove('fw-bold');
        message.readed = true;
        socket.send(JSON.stringify(message));
    }
</script>
</html>
