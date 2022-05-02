package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.Tuple;
import domain.market.MarketSystem;
import domain.shop.Order;
import domain.shop.Shop;
import domain.user.ShoppingBasket.BasketInfo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Cart {
    private Map<Integer, ShoppingBasket> baskets;
    private double totalAmount;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();

    public Cart() {
        baskets = new HashMap<>();
        totalAmount = 0;
    }

    public boolean addProductToCart(int shopID, int productID, int amount) {
        boolean result;
        if (!baskets.containsKey(shopID)) {
            Shop shop = MarketSystem.getInstance().getShop(shopID);
            ShoppingBasket newBasket = new ShoppingBasket(shop);
            result = newBasket.addProductToBasket(productID, amount);
            baskets.put(shopID, newBasket);

        } else {
            result = baskets.get(shopID).addProductToBasket(productID, amount);

        }
        if(result)
            eventLogger.logMsg(Level.INFO, String.format("add product %d in shop %d to cart succeeded", productID, shopID));
        else
            errorLogger.logMsg(Level.WARNING, String.format("add product %d in shop %d to cart failed", productID, shopID));
        return result;
    }

    public boolean updateAmountOfProduct(int shopID, int productID, int amount) {
        if (!baskets.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING, String.format("cannot update amount of product %d because cart doesn't contain basket with shop %d", productID, shopID));
            return false;
        }
        return baskets.get(shopID).updateAmount(productID, amount);
    }

    public boolean removeProductFromCart(int shopID, int productID) {
        if (!baskets.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING, String.format("cannot remove product %d because cart doesn't contain basket with shop %d", productID, shopID));
            return false;
        }
        return baskets.get(shopID).removeProduct(productID);
    }

    public double getTotalAmount() {
        for (ShoppingBasket s : baskets.values()) {
            totalAmount += s.calculateTotalAmount();
        }
        return totalAmount;
    }


    public CartInfo showCart() {
        List<BasketInfo> basketInfoList = new ArrayList<>();
        for (Integer shopID : baskets.keySet()) {
            BasketInfo basket = baskets.get(shopID).showBasket();
           basketInfoList.add(basket);
        }
        totalAmount = getTotalAmount();
        return new CartInfo(totalAmount,basketInfoList);
    }

    public List<ResponseT<Order>> checkout(String userId, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        LocalDate transaction_date = LocalDate.now();
        totalAmount = getTotalAmount();
        TransactionInfo billingInfo = new TransactionInfo(userId, fullName, address, phoneNumber, cardNumber, expirationDate, transaction_date, totalAmount);
        List<ResponseT<Order>> orders = new ArrayList<>();
        for (Integer shopId : baskets.keySet()) {
            ShoppingBasket s = baskets.get(shopId);
            ResponseT<Order> result = s.checkout(billingInfo);
            orders.add(result);
            if (!result.isErrorOccurred()) {
                baskets.remove(shopId);
                eventLogger.logMsg(Level.INFO, String.format("basket of shop %d checkout successfully.", shopId));
            }
            else{
                errorLogger.logMsg(Level.WARNING, String.format("basket of shop %d failed in checkout.", shopId));
            }
        }
        return orders;
    }

    public class CartInfo{

        private double totalAmount;
        private List<BasketInfo> baskets;

        public CartInfo(double totalAmount,List<BasketInfo> baskets){
            this.totalAmount = totalAmount;
            this.baskets = baskets;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public List<BasketInfo> getBaskets() {
            return baskets;
        }
    }
}
