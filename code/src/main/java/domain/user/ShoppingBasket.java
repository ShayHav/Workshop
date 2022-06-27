package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.BidNotFoundException;
import domain.Exceptions.BlankDataExc;
import domain.Responses.ResponseT;
import domain.shop.Order;
import domain.shop.Product;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.ProductImp;
import domain.shop.Shop;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Entity
public class ShoppingBasket {

    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final Shop shop;
    private Map<Integer, Integer> productAmountList;
    private Map<Integer, Boolean> bidIDs_status;
    private double basketAmount;

    public ShoppingBasket(Shop shop) {
        this.shop = shop;
        productAmountList = new HashMap<>();
        bidIDs_status = new HashMap<>();
        basketAmount = 0;
    }

    /***
     * update amount of productID in the basket
     * pre-condition - product exists in the basket, amount - non-negative integer
     * @param productID id of product
     * @param amount desired amount
     * @return false if the product
     */
    public void updateAmount(int productID, int amount) throws ProductNotFoundException,IllegalArgumentException {
        if (shop.isProductIsAvailable(productID, amount)) {
            if (!productAmountList.containsKey(productID)) {
                errorLogger.logMsg(Level.WARNING, String.format("update amount of product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getShopID(), productID));
                throw new ProductNotFoundException(String.format("requested product %d wasn't in the basket.", productID));
            } else if (amount < 0) {
                errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - tried to update to negative amount.", productID, shop.getShopID()));
                throw new IllegalArgumentException(String.format("tried to update product %d to negative amount. ", productID));
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
            }
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
            throw new ProductNotFoundException(String.format("product %d is not available.", productID));
        }
    }

    /***
     * remove productID in the basket
     * pre-condition - product exists in the basket
     * @param productID id of product
     * @return false if the product
     */
    public void removeProduct(int productID) throws ProductNotFoundException {
        if (!productAmountList.containsKey(productID)) {
            errorLogger.logMsg(Level.WARNING, String.format("remove product in basket of shop %d failed - requested product %d wasn't in the basket.", shop.getShopID(), productID));
            throw new ProductNotFoundException(String.format("requested product %d wasn't in the basket.", productID));
        } else {
            synchronized (productAmountList) {
                productAmountList.remove(productID);
            }
            this.calculateTotalAmount();
            eventLogger.logMsg(Level.INFO, String.format("product %d removed successfully from basket", productID));
        }
    }

    /***
     * add product with product id to basket, in amount of amountToAdd
     * pre-condition - amountToAdd is positive Integer
     * @param productID product id
     * @param amountToAdd amount to add
     */
    public void addProductToBasket(int productID, int amountToAdd) throws IllegalArgumentException, ProductNotFoundException{
        if (shop.isProductIsAvailable(productID, amountToAdd)) {
            if (amountToAdd <= 0) {
                errorLogger.logMsg(Level.WARNING, String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
                throw new IllegalArgumentException(String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
            } else if (!productAmountList.containsKey(productID)) {
                synchronized (productAmountList) {
                    productAmountList.put(productID, amountToAdd);
                }
                this.calculateTotalAmount();
                eventLogger.logMsg(Level.INFO, String.format("product %d with amount %d added to basket successfully", productID, amountToAdd));
            } else {
                int currAmount = productAmountList.get(productID);
                synchronized (productAmountList) {
                    productAmountList.replace(productID, (currAmount + amountToAdd));
                }
                this.calculateTotalAmount();
                eventLogger.logMsg(Level.INFO, String.format("product %d with amount %d added to basket successfully", productID, currAmount));
            }
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
           throw new ProductNotFoundException(String.format("product %d is unavailable in shop %d", productID,shop.getShopID()));
        }
    }


    public int addBidToBasket(int productID, int amountToAdd, double offerPrice, User basketOwner) throws IllegalArgumentException, ProductNotFoundException{
        if (shop.isProductIsAvailable(productID, amountToAdd)) {
            if (amountToAdd <= 0) {
                errorLogger.logMsg(Level.WARNING, String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
                throw new IllegalArgumentException(String.format("add product of product %d in basket of shop %d failed - tried to add with non-positive amount.", productID, shop.getShopID()));
            }

            if (offerPrice <= 0) {
                errorLogger.logMsg(Level.WARNING, String.format("offering price given: %d must give a positive number price.", offerPrice));
                throw new IllegalArgumentException(String.format("offering price given: %d must give a positive number price.", offerPrice));
            }
            ProductImp product;
            try {
                 product = shop.getProduct(productID);
            }catch (ProductNotFoundException productNotFoundException){
                errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
                throw new ProductNotFoundException(String.format("product %d is unavailable in shop %d", productID,shop.getShopID()));
            }
            ProductImp newBidProduct = new ProductImp(product.getId(), product.getName(), product.getDescription(), product.getCategory(), offerPrice, amountToAdd);
            int bidID = shop.addNewBid(newBidProduct, basketOwner);
            bidIDs_status.put(bidID, false);
            return bidID;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("update amount of product %d in basket of shop %d failed - product is not available.", productID, shop.getShopID()));
            throw new ProductNotFoundException(String.format("product %d is unavailable in shop %d", productID,shop.getShopID()));
        }
    }

    public void acceptBid(int bidID) throws BidNotFoundException {
        if(bidIDs_status.get(bidID) == null)
            throw new BidNotFoundException("this bid does not exist, cannot resolve it");
        bidIDs_status.put(bidID, true);
        this.calculateTotalAmount();
    }

    public void removeBid(int bidID) {
        bidIDs_status.remove(bidID);
        this.calculateTotalAmount();
    }

    /***
     * calculate total amount of the products in the basket
     * @return total amount.
     */
    public double calculateTotalAmount() {
        synchronized (this) {
            List<Integer> acceptedBids = new ArrayList<>();
            for(Map.Entry<Integer, Boolean> bid_status: bidIDs_status.entrySet()){
                if(bid_status.getValue() == true)
                    acceptedBids.add(bid_status.getKey());
            }
            basketAmount = shop.calculateTotalAmountOfOrder(productAmountList, acceptedBids);
        }

        return basketAmount;
    }

    /***
     * complete the purchase by performing checkout via the relevant shop of the basket.
     * @param billingInfo all the relevant information to complete the transaction.
     */
    public ResponseT<Order> checkout(TransactionInfo billingInfo) throws BlankDataExc {
        List<Integer> acceptedBids = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> bid_status: bidIDs_status.entrySet()){
            if(bid_status.getValue() == true)
                acceptedBids.add(bid_status.getKey());
        }
        ResponseT<Order> order = shop.checkout(productAmountList, acceptedBids, billingInfo);
        if(!order.isErrorOccurred()) {
            productAmountList = new HashMap<>();
            for (Map.Entry<Integer, Boolean> bid : bidIDs_status.entrySet()) {
                if (bid.getValue().equals(true))
                    bidIDs_status.remove(bid.getKey());
            }
        }
        return order;
    }

    public ServiceBasket showBasket() {
        Map<Product,Integer> productWithAmount = new HashMap<>();
        for(Integer product: productAmountList.keySet()){
            Product p;
            try {
                p = shop.getInfoOnProduct(product);
            }catch (ProductNotFoundException pnfe){
                productAmountList.remove(product);
                continue;
            }
            int amount = productAmountList.get(product);
            productWithAmount.put(p,amount);
        }

        for(Integer bidID: bidIDs_status.keySet()){
            Product p;
            try {
                p = shop.getInfoOnBid(bidID);
            }catch (BidNotFoundException bidNotFoundException){
                removeBid(bidID);
                continue;
            }
            int amount = p.getAmount();
            productWithAmount.put(p,amount);
        }
        basketAmount = calculateTotalAmount();
        return new ServiceBasket(shop.getShopID(),shop.getName(),productWithAmount,basketAmount);
    }

    public Map<Integer, Integer> getProductAmountList() {
        return productAmountList;
    }

    public boolean shouldBeRemovedFromCart(){
        return productAmountList.size() == 0 && bidIDs_status.size() == 0;
    }

    public class ServiceBasket {

        private int shopId;
        private String shopName;
        private Map<Product,Integer> productWithAmount;
        private double totalAmount;

        public ServiceBasket(int shopId, String shopName, Map<Product, Integer> productWithAmount, double totalAmount){
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

        public Map<Product, Integer> getProductWithAmount() {
            return productWithAmount;
        }

        public String getShopName() {
            return shopName;
        }
    }
}
