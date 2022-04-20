package domain.user;

import domain.Logger_singleton;
import domain.shop.Shop;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShoppingBasket {
    private static final Logger logger = Logger.getLogger(ShoppingBasket.class.getName());
    private Shop shop;
    Map<Integer, Integer> productAmountList = new HashMap<>();
    double basketAmount;

    public ShoppingBasket(Shop shop, int productID, int amount) {
        this.shop = shop;
        productAmountList.put(productID, amount);
    }

    /***
     * update amount of productID in the basket
     * pre-condition - product exists in the basket, amount - non-negative integer
     * @param productID
     * @param amount
     * @return false if the product
     */
    public boolean updateAmount(int productID, int amount) {
        if (!productAmountList.containsKey(productID)) {
            logger.log(Level.WARNING, String.format("update amount of product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getID(), productID));
            //TODO: singleton Logger??
            Logger_singleton.getInstance().logMsg(Level.WARNING, String.format("update amount of product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getID(), productID));
            //TODO: singleton Logger??
            return false;
        } else if (amount < 0) {
            logger.log(Level.WARNING, String.format("update amount of product in basket of shop %d failed - tried to update to negative amount.", shop.getID()));
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
     * @param productID
     * @return false if the product
     */
    public boolean removeProduct(int productID) {
        if (!productAmountList.containsKey(productID)) {
            logger.log(Level.WARNING, String.format("remove product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getID(), productID));
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
            logger.log(Level.WARNING, String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getID()));
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
    public void checkout(TransactionInfo billingInfo) {
        shop.checkout(productAmountList, basketAmount, billingInfo);
    }
}
