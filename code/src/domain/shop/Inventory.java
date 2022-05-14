package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.InvalidProductInfoException;
import domain.Exceptions.ProductNotFoundException;

import java.util.*;
import java.util.logging.Level;

public class Inventory {
    private final Map<Integer, ProductImp> keyToProduct;
    private final Map<ProductImp, Integer> productToQuantity;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();

    private int productIdGen;

    public Inventory(){
        productToQuantity = new HashMap<>();
        keyToProduct = new HashMap<>();
        productIdGen = 1;
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
        if(keyToProduct.containsKey(prodID)) {
            eventLogger.logMsg(Level.INFO, String.format("return the price of product with id: %d", prodID));
            return keyToProduct.get(prodID).getBasePrice();
        }
        errorLogger.logMsg(Level.WARNING, String.format("product: %d does not exist", prodID));
        throw new ProductNotFoundException(String.format("product: %d does not exist", prodID));
    }

    public int getQuantity(int product) throws ProductNotFoundException {
        if(!keyToProduct.containsKey(product)) {
            errorLogger.logMsg(Level.WARNING, String.format("No product with the id %d in the store", product));
            throw new ProductNotFoundException();
        }
        return productToQuantity.get(keyToProduct.get(product));
    }

    /**
     * adding a product to the store inventory

     */
    public Product addProduct(String productName, String productDesc, String productCategory, double price, int quantity) throws InvalidProductInfoException {
        int prodID;
        synchronized (this) {
            prodID = productIdGen++;
        }
        if(price < 0.0 || quantity < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("Non positive price or quantity at adding a product with product id %d", prodID));
            throw new InvalidProductInfoException();
        }
        ProductImp p = new ProductImp(prodID, productName, productDesc, productCategory, price);
        productToQuantity.put(p, quantity);
        keyToProduct.put(prodID,p);
        eventLogger.logMsg(Level.INFO, String.format("product with id %d added to the store inventory", prodID));
        return p;
    }

    public void setPrice(int product, double newPrice) throws InvalidProductInfoException, ProductNotFoundException {
        if(newPrice < 0.0){
            errorLogger.logMsg(Level.WARNING, String.format("illegal argument at set price to product %d", product));
            throw new InvalidProductInfoException("illegal argument at set price to product");
        }
        if(!keyToProduct.containsKey(product)){
            errorLogger.logMsg(Level.WARNING, String.format("product: %d does not exist", product));
            throw new ProductNotFoundException("product does not exist");
        }
        synchronized (productToQuantity) {
            ProductImp p = keyToProduct.get(product);
            p.setBasePrice(newPrice);
        }
    }

    public void setAmount(int product, int newAmount) throws InvalidProductInfoException, ProductNotFoundException {
        if(newAmount < 0) {
            errorLogger.logMsg(Level.WARNING, String.format("illegal argument at set price to product %d", product));
            throw new InvalidProductInfoException("invalid new amount");
        }
        if(!keyToProduct.containsKey(product)){
            errorLogger.logMsg(Level.WARNING, String.format("product:%d does not exist", product));
            throw new ProductNotFoundException("this product does not exist in the inventory");
        }
        synchronized (productToQuantity) {
            productToQuantity.replace(keyToProduct.get(product), newAmount);
        }
    }

    /**
     * reduce the quantity of a product in the inventory
     * @param product product id of a product in the inventory
     * @param amount the number of the items to reduce from the inventory
     * @return true if successfully reduce the quantity in the inventory false otherwise
     */
    public boolean reduceAmount(int product, int amount){
        if(!keyToProduct.containsKey(product))
            return false;
        ProductImp p = keyToProduct.get(product);
        if(productToQuantity.get(p) < amount)
            return false;
        synchronized (productToQuantity) {
            int currentAmount = productToQuantity.get(p);
            currentAmount -= amount;
            productToQuantity.replace(p, currentAmount);
        }
        return true;
    }

    public void removeProduct(int product) {
        if(keyToProduct.containsKey(product)){
            Product p = keyToProduct.get(product);
            keyToProduct.remove(product);
            synchronized (productToQuantity) {
                productToQuantity.remove(p);
            }
        }
    }

    public List<Product> getItemsInStock() {
        List<Product> products = new ArrayList<>();
        for(ProductImp p : productToQuantity.keySet()){
            if(productToQuantity.get(p) > 0){
                products.add(p);
            }
        }
        eventLogger.logMsg(Level.INFO, "return all the items in stock");
        return products;
    }

    public Product findProduct(int productID){
        return keyToProduct.get(productID);
    }

    public boolean setDescription(int productID, String newDesc){
        if(!keyToProduct.containsKey(productID)){
            errorLogger.logMsg(Level.WARNING, String.format("product with id  %d is not in the store inventory", productID));
            return false;
        }
        keyToProduct.get(productID).setDescription(newDesc);
        return true;
    }


    /**
     * a method to reserved all the item to a client, so he could buy them
     * @param items map of product id to the quantity
     * @return true iff the items were in stock and were reserved them
     */
    protected boolean reserveItems(Map<Integer, Integer> items){
        for(Integer item: items.keySet()){
            if(!isInStock(item)){
                eventLogger.logMsg(Level.INFO, String.format("failed to reserve the items due to lack of stock of product with id: %d", item));
                return false;
            }
        }
        //all the item is in stock ,and we want to reserve them until payment
        for(Integer item: items.keySet()){
            reduceAmount(item ,items.get(item));
        }
        eventLogger.logMsg(Level.INFO, "items were reserved");
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
        if(!keyToProduct.containsKey(product)){
            throw new ProductNotFoundException("this product does not exist");
        }
        ProductImp p  = keyToProduct.get(product);
        p.setName(name);
        p.setDescription(description);
        p.setCategory(category);
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

    public ProductInfo getProductInfo(int productId) throws ProductNotFoundException {
        if(!keyToProduct.containsKey(productId)){
            throw new ProductNotFoundException("this product does not exist");
        }
        ProductImp p = keyToProduct.get(productId);
        ProductInfo product =  p.getProductInfo();
        product.setPrice(p.getBasePrice());
        return product;
    }

    public synchronized List<ProductInfo> getAllProductInfo(){
        List<ProductInfo> products = new ArrayList<>();
        for(ProductImp p: keyToProduct.values()){
            ProductInfo productInfo = p.getProductInfo();
            double price = p.getBasePrice();
            productInfo.setPrice(price);
            products.add(productInfo);
        }
        return products;
    }

}
