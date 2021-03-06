package domain.user;

import domain.*;
import domain.Exceptions.*;
import domain.Responses.Response;
import domain.Responses.ResponseT;
import domain.shop.Order;
import domain.shop.Shop;
import domain.user.ShoppingBasket.ServiceBasket;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
@Entity
public class Cart {
    private Map<Integer, ShoppingBasket> baskets;
    private double totalAmount;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();

    public Cart() {
        baskets = new HashMap<>();
        totalAmount = 0;
    }

    public Response addProductToCart(int shopID, int productID, int amount) throws ShopNotFoundException {
        if (!baskets.containsKey(shopID)) {
            try {
                Shop shop = ControllersBridge.getInstance().getShop(shopID);
                ShoppingBasket newBasket = new ShoppingBasket(shop);
                newBasket.addProductToBasket(productID, amount);
                baskets.put(shopID, newBasket);
                totalAmount = getTotalAmount();
                eventLogger.logMsg(Level.INFO, String.format("add product %d in shop %d to cart succeeded", productID, shopID));
                return new Response();
            } catch (IllegalArgumentException | ProductNotFoundException e) {
                errorLogger.logMsg(Level.WARNING, String.format("add product %d in shop %d to cart failed", productID, shopID));
                return new Response(e.getMessage());
            }
        } else {
            try {
                baskets.get(shopID).addProductToBasket(productID, amount);
                totalAmount = getTotalAmount();
                return new Response();
            } catch (IllegalArgumentException | ProductNotFoundException e) {
                errorLogger.logMsg(Level.WARNING, String.format("add product %d in shop %d to cart failed", productID, shopID));
                return new Response(e.getMessage());
            }
        }
    }


    public ResponseT<Integer> addNewBidToCart(int shopID, int productID, int amount, User basketOwner, double price) throws ShopNotFoundException {
        if (!baskets.containsKey(shopID)) {
            try {
                Shop shop = ControllersBridge.getInstance().getShop(shopID);
                ShoppingBasket newBasket = new ShoppingBasket(shop);
                int bidID = newBasket.addBidToBasket(productID, amount, price, basketOwner);
                baskets.put(shopID, newBasket);
                totalAmount = getTotalAmount();
                eventLogger.logMsg(Level.INFO, String.format("add product %d in shop %d to cart succeeded", productID, shopID));
                return new ResponseT<>(bidID);
            } catch (IllegalArgumentException | ProductNotFoundException e) {
                errorLogger.logMsg(Level.WARNING, String.format("add product %d in shop %d to cart failed", productID, shopID));
                return new ResponseT<>(e.getMessage());
            }
        } else {
            try {
                int bidID = baskets.get(shopID).addBidToBasket(productID, amount, price, basketOwner);
                totalAmount = getTotalAmount();
                return new ResponseT<>(bidID);
            } catch (IllegalArgumentException | ProductNotFoundException e) {
                errorLogger.logMsg(Level.WARNING, String.format("add product %d in shop %d to cart failed", productID, shopID));
                return new ResponseT<>(e.getMessage());
            }
        }
    }

    public void bidApproved(int shopID, int bidID) throws CriticalInvariantException, BidNotFoundException {
        synchronized (baskets) {
            if (!baskets.containsKey(shopID))
                throw new CriticalInvariantException();
            baskets.get(shopID).acceptBid(bidID);
        }
    }

    public void removeBid(int shopID, int bidID) throws CriticalInvariantException, BidNotFoundException {
        synchronized (baskets) {
            if (!baskets.containsKey(shopID))
                throw new CriticalInvariantException("tried to remove bid from a shop that isn't in user's cart");
            ShoppingBasket basket = baskets.get(shopID);
            basket.removeBid(bidID);
            if(basket.shouldBeRemovedFromCart())
                baskets.remove(shopID);
        }
    }

    public Response updateAmountOfProduct(int shopID, int productID, int amount) {
        if (!baskets.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING, String.format("cannot update amount of product %d because cart doesn't contain basket with shop %d", productID, shopID));
            return new Response(String.format("cannot update amount of product %d because cart doesn't contain basket with shop %d", productID, shopID));
        }
        try {
            baskets.get(shopID).updateAmount(productID, amount);
            getTotalAmount();
            return new Response();

        } catch (ProductNotFoundException | IllegalArgumentException e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeProductFromCart(int shopID, int productID) {
        if (!baskets.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING, String.format("cannot remove product %d because cart doesn't contain basket with shop %d", productID, shopID));
            return new Response(String.format("cannot update amount of product %d because cart doesn't contain basket with shop %d", productID, shopID));
        }
        try {
            baskets.get(shopID).removeProduct(productID);
            if(baskets.get(shopID).shouldBeRemovedFromCart()){
                baskets.remove(shopID);
            }
            getTotalAmount();
            return new Response();
        } catch (ProductNotFoundException e) {
            return new Response(e.getMessage());
        }
    }

    public double getTotalAmount() {
        totalAmount = 0;
        for (ShoppingBasket s : baskets.values()) {
            totalAmount += s.calculateTotalAmount();
        }
        return totalAmount;
    }


    public ServiceCart showCart() {
        Map<Integer,ServiceBasket> basketMap = new HashMap<>();
        for (Integer shopID : baskets.keySet()) {
            ServiceBasket basket = baskets.get(shopID).showBasket();
            basketMap.put(shopID,basket);
        }
        totalAmount = getTotalAmount();
        return new ServiceCart(totalAmount, basketMap);
    }

    public List<ResponseT<Order>> checkout(String userId, String fullName, String address, String city, String country, String zip,
                                           String phoneNumber, String cardNumber, String ccv, String expirationDate) throws BlankDataExc {
        LocalDate transaction_date = LocalDate.now();
        totalAmount = getTotalAmount();
        TransactionInfo billingInfo = new TransactionInfo(userId, fullName, address, city, country, zip, phoneNumber, cardNumber,ccv, expirationDate, transaction_date, totalAmount);

        List<ResponseT<Order>> orders = new ArrayList<>();
        for (Integer shopId : baskets.keySet()) {
            ShoppingBasket s = baskets.get(shopId);
            ResponseT<Order> result = s.checkout(billingInfo);
            orders.add(result);
            if (!result.isErrorOccurred()) {
                eventLogger.logMsg(Level.INFO, String.format("basket of shop %d checkout successfully.", shopId));
                if (s.shouldBeRemovedFromCart())
                    baskets.remove(shopId);
            } else {
                errorLogger.logMsg(Level.WARNING, String.format("basket of shop %d failed in checkout.", shopId));
            }
        }
        return orders;
    }

    public class ServiceCart {

        private final double totalAmount;
        private final Map<Integer,ServiceBasket> baskets;

        public ServiceCart(double totalAmount, Map<Integer,ServiceBasket> baskets) {
            this.totalAmount = totalAmount;
            this.baskets = baskets;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public Map<Integer,ServiceBasket>getBaskets() {
            return baskets;
        }
    }
}
