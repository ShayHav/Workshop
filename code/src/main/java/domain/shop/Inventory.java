package domain.shop;

import domain.DAL.ControllerDAL;
import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.InvalidProductInfoException;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.PurchaseFormats.BidFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;
import java.util.logging.Level;
public class Inventory {
    private int shopID;
    private Map<Integer, ProductImp> keyToProduct;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private ControllerDAL controllerDAL = ControllerDAL.getInstance();


    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public Map<Integer, ProductImp> getKeyToProduct() {
        return keyToProduct;
    }

    public void setKeyToProduct(Map<Integer, ProductImp> keyToProduct) {
        this.keyToProduct = keyToProduct;
    }

    public Inventory(){
        keyToProduct = new HashMap<>();
    }

    /**
     * check if a product is in stock
     * @param product the id of the product querying
     * @return true if there is at least one item in the inventory
     */
    public synchronized boolean isInStock(int product){
        int quantity;
        try {
            quantity = getQuantity(product);
        }catch (ProductNotFoundException productNotFoundException){
            return false;
        }
        return quantity > 0;
    }

    public double getPrice(int prodID) throws ProductNotFoundException {
        synchronized (keyToProduct) {
            if (keyToProduct.containsKey(prodID)) {
                eventLogger.logMsg(Level.INFO, String.format("return the price of product with id: %d", prodID));
                return keyToProduct.get(prodID).getBasePrice();
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("product: %d does not exist", prodID));
        throw new ProductNotFoundException(String.format("product: %d does not exist", prodID));
    }

    public String getName(int productId) throws ProductNotFoundException {
        synchronized (keyToProduct) {
            if (!keyToProduct.containsKey(productId)) {
                errorLogger.logMsg(Level.SEVERE, String.format("this product does not exist: %d", productId));
                throw new ProductNotFoundException("this product does not exist");
            }
        }
        return keyToProduct.get(productId).getName();
    }

    public String getCategory(int productId) throws ProductNotFoundException {
        synchronized (keyToProduct) {
            if (!keyToProduct.containsKey(productId)) {
                errorLogger.logMsg(Level.SEVERE, String.format("this product does not exist: %d", productId));
                throw new ProductNotFoundException("this product does not exist");
            }
            return keyToProduct.get(productId).getCategory();
        }
    }

    public int getQuantity(int product)  throws ProductNotFoundException{
        synchronized (keyToProduct) {
            if (!keyToProduct.containsKey(product)) {
                errorLogger.logMsg(Level.WARNING, String.format("No product with the id %d in the store", product));
                throw new ProductNotFoundException();
            }
            return keyToProduct.get(product).getAmount();
        }
    }

    /**
     * adding a product to the store inventory

     */
    public Product addProduct(int serialNumber, String productName, String productDesc, String productCategory, double price, int quantity) throws InvalidProductInfoException {
        if(price < 0.0 || quantity < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("Non positive price or quantity at adding a product with product id %d", serialNumber));
            throw new InvalidProductInfoException();
        }
        ProductImp p;
        synchronized (keyToProduct) {
            if(keyToProduct.containsKey(serialNumber))
            throw new InvalidProductInfoException(String.format("serialNumber is already in use: %d", serialNumber));
            p = new ProductImp(serialNumber, productName, productDesc, productCategory, price, quantity);
            keyToProduct.put(serialNumber, p);
        }
        eventLogger.logMsg(Level.INFO, String.format("product with id %d added to the store inventory", serialNumber));

        controllerDAL.upDateInventory(this);

        return p;
    }

    public void setPrice(int product, double newPrice) throws InvalidProductInfoException, ProductNotFoundException {
        if(newPrice < 0.0){
            errorLogger.logMsg(Level.WARNING, String.format("illegal argument at set price to product %d", product));
            throw new InvalidProductInfoException("illegal argument at set price to product");
        }
        synchronized (keyToProduct) {
            if(!keyToProduct.containsKey(product)){
                errorLogger.logMsg(Level.WARNING, String.format("product: %d does not exist", product));
                throw new ProductNotFoundException("product does not exist");
            }
            ProductImp p = keyToProduct.get(product);
            p.setBasePrice(newPrice);
        }
        controllerDAL.upDateInventory(this);
    }

    public void setAmount(int product, int newAmount) throws InvalidProductInfoException, ProductNotFoundException {
        if(newAmount < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("illegal argument at set price to product %d", product));
            throw new InvalidProductInfoException("invalid new amount");
        }
        synchronized (keyToProduct) {

            if(!keyToProduct.containsKey(product)){
                errorLogger.logMsg(Level.WARNING, String.format("product:%d does not exist", product));
                throw new ProductNotFoundException("this product does not exist in the inventory");
            }

            keyToProduct.get(product).setQuantity(newAmount);
        }
        controllerDAL.upDateInventory(this);
    }

    /**
     * reduce the quantity of a product in the inventory
     * @param product product id of a product in the inventory
     * @param amount the number of the items to reduce from the inventory
     * @return true if successfully reduce the quantity in the inventory false otherwise
     */
    public void reduceAmount(int product, int amount) throws ProductNotFoundException {
        synchronized (keyToProduct) {
            if (!keyToProduct.containsKey(product))
                throw new ProductNotFoundException("product not found");
            ProductImp p = keyToProduct.get(product);
            int currentAmount = keyToProduct.get(product).getAmount();
            currentAmount -= amount;
            keyToProduct.get(product).setQuantity(currentAmount);
            controllerDAL.upDateInventory(this);
        }
    }

    public void removeProduct(int product) throws InvalidProductInfoException {
        synchronized (keyToProduct) {

            if (keyToProduct.containsKey(product)) {
                Product p = keyToProduct.get(product);
                keyToProduct.remove(product);
            } else throw new InvalidProductInfoException(String.format("product is not exist id: %d", product));
        }
        controllerDAL.upDateInventory(this);
    }

    public List<Product> getItemsInStock() {
        List<Product> products = new ArrayList<>();
        synchronized (keyToProduct) {
            for (ProductImp p : keyToProduct.values()) {
                if (p.getAmount() > 0) {
                    products.add(p);
                }
            }
        }
        eventLogger.logMsg(Level.INFO, "return all the items in stock");
        return products;
    }

    public ProductImp findProduct(int productID) throws ProductNotFoundException {
        ProductImp product;
        synchronized (keyToProduct) {
            product = keyToProduct.get(productID);
        }
        if(product == null)
            throw new ProductNotFoundException(String.format("product:%d was not found in inventory", productID));
        return product;
    }

    public boolean setDescription(int productID, String newDesc){
        synchronized (keyToProduct) {

            ProductImp productImp;
            if (!keyToProduct.containsKey(productID)) {
                productImp = controllerDAL.getProduct(productID, shopID);
                if (productImp == null) {
                    errorLogger.logMsg(Level.WARNING, String.format("product with id  %d is not in the store inventory", productID));
                    return false;
                }
                keyToProduct.put(productID, productImp);
            }
            keyToProduct.get(productID).setDescription(newDesc);
        }
        return true;
    }


    /**
     * a method to reserved all the item to a client, so he could buy them
     * @param items map of product id to the quantity
     * @return true iff the items were in stock and were reserved them
     */
    protected boolean reserveItems(Map<Integer, Integer> items) {
        for (Integer item : items.keySet()) {
            if (!isInStock(item)) {
                eventLogger.logMsg(Level.INFO, String.format("failed to reserve the items due to lack of stock of product with id: %d", item));
                return false;
            }
        }
        //all the item is in stock ,and we want to reserve them until payment
        synchronized (keyToProduct) {
            for (Integer item : items.keySet()) {
                int currentAmount = keyToProduct.get(item).getAmount();
                if (currentAmount < items.get(item))
                    return false;
            }
            try {
                for (Integer item : items.keySet())
                    reduceAmount(item, items.get(item));
            }catch (ProductNotFoundException productNotFoundException){
                return false;
            }
        }
        eventLogger.logMsg(Level.INFO, "items were reserved");
        controllerDAL.upDateInventory(this);
        return true;
    }

    /**
     * restoring the quantity of products after a reduced amount
     * @param items map of a product id to the quantity that were reserved
     */
    protected void restoreStock(Map<Integer, Integer> items) throws InvalidProductInfoException, ProductNotFoundException{
        for(Integer item: items.keySet()) {
            setAmount(item, getQuantity(item) + items.get(item));
        }
        eventLogger.logMsg(Level.INFO, "restored the stock of items");
    }

    public Product setProduct(int product, String name, String description, String category) throws ProductNotFoundException {
        ProductImp p;
        synchronized (keyToProduct) {
            if(!keyToProduct.containsKey(product)){
                p = checkInDBForPI(product);
                if(p==null) {
                    errorLogger.logMsg(Level.SEVERE, String.format("this product does not exist: %d", product));
                    throw new ProductNotFoundException("this product does not exist");
                }else {
                    keyToProduct.put(product, p);
                }
            }
        }
        p = keyToProduct.get(product);
        p.setName(name);
        p.setDescription(description);
        p.setCategory(category);
        controllerDAL.upDateInventory(this);
        return p;
    }

  /*  private class PricesAndQuantity{
        public int quantity;
        public double price;

        public PricesAndQuantity(int quantity, double price){
            this.price = price;
            this.quantity = quantity;
        }
    }*/

    public ServiceProduct getProductInfo(int productId) throws ProductNotFoundException {
        synchronized (keyToProduct) {
            if(!keyToProduct.containsKey(productId)){
                ProductImp p = checkInDBForPI(productId);
                if(p==null) {
                    errorLogger.logMsg(Level.SEVERE, String.format("this product does not exist: %d", productId));
                    throw new ProductNotFoundException("this product does not exist");
                }
                keyToProduct.put(productId,p);
            }
            return new ServiceProduct(keyToProduct.get(productId));
        }
    }

    public List<Product> getAllProductInfo(){
        synchronized (keyToProduct) {
            return new ArrayList<>(keyToProduct.values());
        }
    }

    private ProductImp checkInDBForPI(int p){
        return controllerDAL.getProduct(p,shopID);
    }

    public void initLs()
    {

    }

    public void cleanLs()
    {

    }

}
