package domain.user;

import domain.Tuple;
import domain.shop.Order;
import domain.shop.Shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public Map<Integer, List<Tuple<Integer,Integer>>> showCart(){
        Map<Integer, List<Tuple<Integer,Integer>>> cart = new HashMap<>();
        for(Integer shopID: baskets.keySet()){
            List<Tuple<Integer,Integer>> basket = baskets.get(shopID).showBasket();
            cart.put(shopID, basket);
        }
        return cart;
    }

    //TODO: figure out how to notify error per basket
    public List<Tuple<status,Order>> checkout(int id, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        LocalDate transaction_date = LocalDate.now();
        TransactionInfo billingInfo = new TransactionInfo(id ,fullName, address, phoneNumber, cardNumber, expirationDate, transaction_date, totalAmount);
        List<Tuple<status,Order>> orders = new ArrayList<>();
        for (ShoppingBasket s : baskets.values()) {
            Tuple<status,Order> result = s.checkout(billingInfo);
            if (result.y != null)
                baskets.remove(s);
        }

    }
}
