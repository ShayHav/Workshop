package domain.user;

import domain.ErrorLoggerSingleton;
import domain.ResponseT;
import domain.shop.Order;
import domain.shop.ProductInfo;
import domain.shop.Shop;
import java.util.HashMap;
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
    private final Shop shop;
    private final Map<Integer, Integer> productAmountList;
    private double basketAmount;

    public ShoppingBasket(Shop shop) {
        this.shop = shop;
        productAmountList = new HashMap<>();
        basketAmount = 0;
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
     * @param productID product id
     * @param amountToAdd amount to add
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

    public BasketInfo showBasket() {
        Map<ProductInfo,Integer> productWithAmount = new HashMap<>();
        for(Integer product: productAmountList.keySet()){
            ProductInfo p = shop.getInfoOnProduct(product);
            int amount = productAmountList.get(product);
            productWithAmount.put(p,amount);
        }
        basketAmount = calculateTotalAmount();
        return new BasketInfo(shop.getShopID(),shop.getName(),productWithAmount,basketAmount);
    }

    public Map<Integer, Integer> getProductAmountList() {
        return productAmountList;
    }

    public record BasketInfo(int shopId, String shopName,
                             Map<ProductInfo, Integer> productWithAmount,
                             double totalAmount) {}
}
