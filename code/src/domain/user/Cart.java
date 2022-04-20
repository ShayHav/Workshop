package domain.user;

import domain.shop.Shop;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Shop,ShoppingBasket> shopShoppingBasketMap;

    public Cart(){shopShoppingBasketMap = new HashMap<>();}

    public void addProductToCart(Shop shop, int product, int amount) throws Exception {
        if(!shopShoppingBasketMap.containsKey(shop)){
            ShoppingBasket newShoppingBasket = new ShoppingBasket(shop,product,amount);
            shopShoppingBasketMap.put(shop,newShoppingBasket);
            return;
        }
        throw new Exception("Attempt to add a product that already exists in the cart");
    }

    public void editingCart(Shop shop, int product, int amount) throws Exception {
        if(shopShoppingBasketMap.containsKey(shop)) {
            ShoppingBasket shoppingBasket = shopShoppingBasketMap.get(shop);
            if(shoppingBasket.updateAmount(product,amount))
                shopShoppingBasketMap.put(shop, shoppingBasket);
            else throw new Exception("Attempt to edit a product that not exists in the cart");
        }
    }
}
