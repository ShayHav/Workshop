@import Presentation.Main
@import Presentation.Model.PresentationShop
@import Presentation.Model.PresentationUser
@param PresentationShop shop
@param PresentationUser user

@template.navBar(user = user, title = "Edit shop" + shop.name)

<main class="container mt-5">
    <div>
        <h1 class="fs-2">Editing shop ${shop.name}</h1>
    </div>

    <div class="container">
        <h2 class="fs-4">Shop Managers</h2>
        <ul class="list-group">
        @for(PresentationUser manager: shop.managers)
            <li class="list-group-item">
                <h3 class="fs-5">${manager.getUsername()}</h3>
                @if(user.has)
                <button class="btn btn-danger">remove</button>
                @endif
            </li>
        @endfor
        </ul>
    </div>
</main>

</body>
<script>
    const socket = new WebSocket('ws://localhost:${Main.port}/shop');

</script>
</html>