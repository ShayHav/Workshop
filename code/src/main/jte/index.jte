@import Presentation.Model.PresentationUser
@param  PresentationUser user
@param java.util.List<Presentation.Model.PresentationShop> shops
@param String filteredName

@template.navBar(user = user, title = "Market")
<main class="container">
    @if(user.isLoggedIn())
        <h1 class="fs-1 mt-2 mb-3">Welcome Back, ${user.getUsername()}!</h1>
    @else
        <h1 class="fs-1">Welcome to the MarketPlace!</h1>
    @endif
    @template.shopFilter(filteredName = filteredName,action = "/")
    <div class="container row mx-auto">
        @for(Presentation.Model.PresentationShop shop: shops)
            <div class="card mt-1 me-3" style="width: 18rem">
                <img src="https://cdn.pixabay.com/photo/2017/08/07/19/46/shop-2607121_960_720.jpg"
                     class="card-img-top" alt="shop front image">
                <div class="card-body">
                    <h2 class="card-title">${shop.name}</h2>
                    <p class="card-text">${shop.description}</p>
                    <a href="/shops/${shop.id}" class="btn btn-primary">Visit Shop</a>
                </div>
            </div>
        @endfor
    </div>
</main>
</body>
</html>