@import Presentation.Model.PresentationUser
@param  PresentationUser user
@param java.util.List<Presentation.Model.PresentationShop> shops
@param String filteredName

@template.navBar(user = user, title = "My shops")
<main class="container">
    <h3 class="mt-3">My shops</h3>
    @template.shopFilter(action = "/users/" + user.getUsername() + "/shops", filteredName = filteredName )
    <div class="container row mt-3 mx-auto">
        @for(Presentation.Model.PresentationShop shop: shops)
            <div class="card mt-1" style="width: 18rem">
                <img src="https://cdn.pixabay.com/photo/2017/08/07/19/46/shop-2607121_960_720.jpg"
                     class="card-img-top mt-2" alt="shop front image">
                <div class="card-body">
                    <h2 class="card-title">${shop.name}</h2>
                    <p class="card-text">${shop.description}</p>
                    @if(shop.isOpen())
                        <p class="card-text text-success fw-bold">The shop is open</p>
                        <a href="/shops/${shop.id}" class="btn btn-primary">Visit Shop</a>
                    @else
                        @if(!shop.isFounder(user))
                            <p class="card-text text-danger fw-bold">The shop is closed</p>
                        @else
                            <p class="card-text text-danger fw-bold">The shop is closed</p>
                            <a href="/shops/${shop.id}" class="btn btn-primary">Visit Shop</a>
                        @endif
                    @endif
                </div>
            </div>
        @endfor
    </div>
</main>
</body>
</html>