@import Presentation.Model.PresentationUser
@param  PresentationUser user
@param java.util.List<Presentation.Model.PresentationShop> shops

@template.navBar(user = user, title = "My shops")
<main class="container">
    <h3 class="mt-3">My shops</h3>
    <div class="container row mt-3 mx-auto">
        @for(Presentation.Model.PresentationShop shop: shops)
            <div class="card mt-1" style="width: 18rem">
                <img src="https://cdn.pixabay.com/photo/2017/08/07/19/46/shop-2607121_960_720.jpg"
                     class="card-img-top mt-2" alt="shop front image">
                <div class="card-body">
                    <h2 class="card-title">${shop.name}</h2>
                    <p class="card-text">${shop.description}</p>
                    @if(shop.isOpen())
                        <p class="card-text text-success">The shop is open</p>
                    @else
                        <p class="card-text text-danger">The shop is closed</p>
                    @endif
                    <a href="/shops/${shop.id}" class="btn btn-primary">Visit Shop</a>
                </div>
            </div>
        @endfor
    </div>
</main>
</body>
</html>