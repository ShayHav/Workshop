package domain.user;

import domain.shop.Shop;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Integer, ShoppingBasket> baskets;
    private double totalAmount;

    public Cart() {
        baskets = new HashMap<>();
        totalAmount = 0;
    }


    public void addProductToCart(Shop shop, int productID, int amount) {
        int shopID = shop.getID();
        if (!baskets.containsKey(shopID)) {
            ShoppingBasket newBasket = new ShoppingBasket(shop, productID, amount);
            baskets.put(shopID, newBasket);
        } else {
            baskets.get(shopID).addProductToBasket(productID, amount);
        }
    }

    public boolean updateAmountOfProduct(int shopID, int productID, int amount) {
        if (!baskets.containsKey(shopID))
            return false;
        return baskets.get(shopID).updateAmount(productID, amount);
    }

    public boolean removeProductFromCart(int shopID, int productID) {
        if (!baskets.containsKey(shopID))
            return false;
        return baskets.get(shopID).removeProduct(productID);
    }

    public double getTotalAmount() {
        for (ShoppingBasket s : baskets.values()) {
            totalAmount += s.calculateTotalAmount();
        }
        return totalAmount;
    }

    public boolean checkout(TransactionInfo billingInfo) {
        for (ShoppingBasket s : baskets.values()) {
            s.checkout(billingInfo);
        }

    }
}
