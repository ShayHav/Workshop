package domain.shop.user;

import domain.*;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.Order;
import domain.shop.Shop;
import domain.Exceptions.ShopNotFoundException;
import domain.Exceptions.BlankDataExc;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
@Entity
public class Cart {
    @Transient
    private Map<Integer, ShoppingBasket> baskets;
    @OneToMany(mappedBy = "c")
    private List<ShoppingBasket> basketLs;
    private double totalAmount;
    @Transient
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    @Transient
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    @Id
    private String username;

    public Cart() {
        baskets = new HashMap<>();
        basketLs = new ArrayList<>();
        totalAmount = 0;

    }

    public List<ShoppingBasket> getBasketLs() {
        return basketLs;
    }

    public void setBasketLs(List<ShoppingBasket> basketLs) {
        this.basketLs = basketLs;
    }

    public Cart merge(Cart c)
    {
        setTotalAmount(c.getTotalAmount());
        setBaskets(c.getBaskets());
        setBasketLs(c.getBasketLs());
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Integer, ShoppingBasket> getBaskets() {
        return baskets;
    }

    public void setBaskets(Map<Integer, ShoppingBasket> baskets) {
        this.baskets = baskets;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Response addProductToCart(int shopID, int productID, int amount) throws ShopNotFoundException {
        if (!baskets.containsKey(shopID)) {
            try {
                Shop shop = ControllersBridge.getInstance().getShop(shopID);
                ShoppingBasket newBasket = new ShoppingBasket(shop);
                newBasket.setCart(this);//Ariel added
                newBasket.addProductToBasket(productID, amount);
                baskets.put(shopID, newBasket);
                basketLs.add(newBasket);//Ariel added
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
            if(baskets.get(shopID).getProductAmountList().size() == 0){
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
        Map<Integer, ShoppingBasket.ServiceBasket> basketMap = new HashMap<>();
        for (Integer shopID : baskets.keySet()) {
            ShoppingBasket.ServiceBasket basket = baskets.get(shopID).showBasket();
            basketMap.put(shopID,basket);
        }
        totalAmount = getTotalAmount();
        return new ServiceCart(totalAmount, basketMap);
    }

    public List<ResponseT<Order>> checkout(String userId, String fullName, String address, String
            phoneNumber, String cardNumber, String expirationDate) throws BlankDataExc {
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
            } else {
                errorLogger.logMsg(Level.WARNING, String.format("basket of shop %d failed in checkout.", shopId));
            }
        }
        return orders;
    }

    public class ServiceCart {

        private double totalAmount;
        private Map<Integer, ShoppingBasket.ServiceBasket> baskets;

        public ServiceCart(double totalAmount, Map<Integer, ShoppingBasket.ServiceBasket> baskets) {
            this.totalAmount = totalAmount;
            this.baskets = baskets;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public Map<Integer, ShoppingBasket.ServiceBasket>getBaskets() {
            return baskets;
        }
    }
}
