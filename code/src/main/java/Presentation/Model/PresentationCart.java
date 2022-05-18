package Presentation.Model;

import domain.user.Cart;
import domain.user.ShoppingBasket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentationCart {
    public Map<Integer, PresentationBasket> baskets;
    public double totalAmount;

    public PresentationCart(Map<Integer, PresentationBasket> baskets, double totalAmount) {
        this.baskets = baskets;
        this.totalAmount = totalAmount;
    }

    public PresentationCart(Cart.ServiceCart cart){
        baskets = new HashMap<>();
        Map<Integer, ShoppingBasket.ServiceBasket> basketMap = cart.getBaskets();
        for(Integer shopId: basketMap.keySet()){
            ShoppingBasket.ServiceBasket b = basketMap.get(shopId);
            baskets.put(shopId,new PresentationBasket(b));
        }
        totalAmount = cart.getTotalAmount();

    }
}
