package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.Tuple;
import domain.shop.Order;
import domain.shop.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShoppingBasket {
    enum status {
        active,
        completed,
        failed_payment,
        failed_supply,
        failed_due_to_error
    }
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private Shop shop;
    private Map<Integer, Integer> productAmountList = new HashMap<>();
    private double basketAmount;

    public ShoppingBasket(Shop shop, int productID, int amount) {
        this.shop = shop;
        productAmountList.put(productID, amount);
    }

    /***
     * update amount of productID in the basket
     * pre-condition - product exists in the basket, amount - non-negative integer
     * @param productID id of product
     * @param amount desired amount
     * @return false if the product
     */
    public boolean updateAmount(int productID, int amount) {
        if (!productAmountList.containsKey(productID)) {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getShopID(), productID));
            return false;
        } else if (amount < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product in basket of shop %d failed - tried to update to negative amount.", shop.getShopID()));
            return false;
        } else {
            if (amount == 0) {
                productAmountList.remove(productID);
            } else {
                productAmountList.replace(productID, amount);
            }
            return true;
        }
    }

    /***
     * remove productID in the basket
     * pre-condition - product exists in the basket
     * @param productID id of product
     * @return false if the product
     */
    public boolean removeProduct(int productID) {
        if (!productAmountList.containsKey(productID)) {
            errorLogger.logMsg(Level.WARNING, String.format("remove product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getShopID(), productID));
            return false;
        } else {
            productAmountList.remove(productID);
            return true;
        }
    }

    /***
     * add product with product id to basket, in amount of amountToAdd
     * pre-condition - amountToAdd is positive Integer
     * @param productID
     * @param amountToAdd
     */
    public boolean addProductToBasket(int productID, int amountToAdd) {
        if (amountToAdd < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
            return false;
        } else if (!productAmountList.containsKey(productID)) {
            productAmountList.put(productID, amountToAdd);
        } else {
            int currAmount = productAmountList.get(productID);
            productAmountList.replace(productID, (currAmount + amountToAdd));
        }
        return true;
    }

    /***
     * calculate total amount of the products in the basket
     * @return total amount.
     */
    public double calculateTotalAmount() {
        basketAmount = shop.calculateTotalAmountOfOrder(productAmountList);
        return basketAmount;
    }

    /***
     * complete the purchase by performing checkout via the relevant shop of the basket.
     * @param billingInfo all the relevant information to complete the transaction.
     */
    public ResponseT<Order> checkout(TransactionInfo billingInfo) {
        return shop.checkOut(productAmountList, basketAmount, billingInfo);
    }

    public List<Tuple<Integer, Integer>> showBasket() {
        List<Tuple<Integer, Integer>> productList = new ArrayList<>();
        for (Integer productID : productAmountList.keySet()) {
            int amount = productAmountList.get(productID);
            productList.add(new Tuple<>(productID,amount));
        }
        return productList;
    }
}
