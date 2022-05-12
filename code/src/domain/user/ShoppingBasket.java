package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.shop.Order;
import domain.shop.ProductInfo;
import domain.shop.ProductNotFoundException;
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
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
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
        if(shop.isProductIsAvailable(productID,amount)) {
            if (!productAmountList.containsKey(productID)) {
                errorLogger.logMsg(Level.WARNING, String.format("update amount of product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getShopID(), productID));
                return false;
            } else if (amount < 0) {
                errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - tried to update to negative amount.", productID, shop.getShopID()));
                return false;
            } else {
                if (amount == 0) {
                    synchronized (productAmountList) {
                        productAmountList.remove(productID);
                    }
                    eventLogger.logMsg(Level.INFO, String.format("product %d removed from basket %d", productID, shop.getShopID()));
                } else {
                    synchronized (productAmountList) {
                        productAmountList.replace(productID, amount);
                    }
                    eventLogger.logMsg(Level.INFO, String.format("product %d amount in basket %d was updated to amount %d", productID, shop.getShopID(), amount));
                }

                return true;
            }
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
            return false;}
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
            synchronized (productAmountList) {
                productAmountList.remove(productID);
            }
            eventLogger.logMsg(Level.INFO, String.format("product %d removed successfully from basket", productID));
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
        if (shop.isProductIsAvailable(productID, amountToAdd)) {
            if (amountToAdd < 0) {
                errorLogger.logMsg(Level.WARNING, String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
                return false;
            } else if (!productAmountList.containsKey(productID)) {
                synchronized (productAmountList) {
                    productAmountList.put(productID, amountToAdd);
                }
                eventLogger.logMsg(Level.INFO, String.format("product %d with amount %d added to basket successfully", productID, amountToAdd));
            } else {
                int currAmount = productAmountList.get(productID);
                synchronized (productAmountList) {
                    productAmountList.replace(productID, (currAmount + amountToAdd));
                }
                eventLogger.logMsg(Level.INFO, String.format("product %d with amount %d added to basket successfully", productID, currAmount));
            }
            return true;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
            return false;
        }
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
    public ResponseT<Order> checkout(TransactionInfo billingInfo) throws BlankDataExc {
        return shop.checkout(productAmountList, basketAmount, billingInfo);
    }

    public BasketInfo showBasket() {
        Map<ProductInfo,Integer> productWithAmount = new HashMap<>();
        for(Integer product: productAmountList.keySet()){
            ProductInfo p;
            try {
                p = shop.getInfoOnProduct(product);
            }catch (ProductNotFoundException pnfe){
                removeProduct(product);
                continue;
            }
            int amount = productAmountList.get(product);
            productWithAmount.put(p,amount);
        }
        basketAmount = calculateTotalAmount();
        return new BasketInfo(shop.getShopID(),shop.getName(),productWithAmount,basketAmount);
    }

    public Map<Integer, Integer> getProductAmountList() {
        return productAmountList;
    }

    public class BasketInfo{

        private int shopId;
        private String shopName;
        private Map<ProductInfo,Integer> productWithAmount;
        private double totalAmount;

        public BasketInfo(int shopId, String shopName, Map<ProductInfo, Integer> productWithAmount, double totalAmount){
            this.shopId = shopId;
            this.shopName = shopName;
            this.productWithAmount = productWithAmount;
            this.totalAmount = totalAmount;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public int getShopId() {
            return shopId;
        }

        public Map<ProductInfo, Integer> getProductWithAmount() {
            return productWithAmount;
        }

        public String getShopName() {
            return shopName;
        }
    }
}
