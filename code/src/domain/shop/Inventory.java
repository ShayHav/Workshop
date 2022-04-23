package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;

import java.util.*;

public class Inventory {
    private final Map<Integer, ProductImp> keyToProduct;
    private final Map<ProductImp, PricesAndQuantity> items;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private Products productList;

    public Inventory(){
        items = new HashMap<>();
        keyToProduct = new HashMap<>();
        productList = Products.getInstance();
    }

    /**
     * check if a product is in stock
     * @param p the product querying
     * @return true if there is at least one item in the inventory
     */
    public boolean isInStock(int product){
        return getQuantity(product) > 0;
    }

    public double getPrice(int prodID){
        if(keyToProduct.containsKey(prodID))
            return items.get(keyToProduct.get(prodID)).price;
        return -1;
    }

    public int getQuantity(int product){
        if(keyToProduct.containsKey(product))
            return -1;
        return items.get(keyToProduct.get(product)).quantity;
    }

    /**
     * adding a product to the store inventrory
     * @param prodID
     * @param price
     * @param quantity
     * @return
     */
    public boolean addProduct(int prodID, double price, int quantity) {
        Product product = productList.getProduct(prodID);
        if(product == null || keyToProduct.containsKey(prodID)){
            return false;
        }
        if(price < 0.0 || quantity < 0)
            return false;
        items.put(new ProductImp(product),new PricesAndQuantity(quantity, price));
        return true;
    }

    public boolean setPrice(int product, double newPrice){
        if(newPrice < 0.0 || !keyToProduct.containsKey(product))
            return false;
        Product p = keyToProduct.get(product);
        items.get(p).price = newPrice;
        return true;

    }

    public boolean setAmount(int product, int newAmount){
        if(newAmount < 0 || !keyToProduct.containsKey(product))
            return false;
        items.get(keyToProduct.get(product)).quantity = newAmount;
        return true;
    }

    public boolean reduceAmount(int product, int amount){
        if(!keyToProduct.containsKey(product))
            return false;
        Product p = keyToProduct.get(product);
        if(items.get(p).quantity < amount)
            return false;
        items.get(p).quantity -= amount;
        return true;
    }

    public void removeProduct(int product) {
        if(keyToProduct.containsKey(product)){
            Product p = keyToProduct.get(product);
            keyToProduct.remove(product);
            items.remove(p);
        }
    }

    public List<Product> getItemsInStock() {
        List<Product> products = new ArrayList<>();
        for(ProductImp p : items.keySet()){
            if(items.get(p).quantity > 0){
                products.add(p);
            }
        }
        return products;
    }

    public Product getProduct(int productID){
        return keyToProduct.get(productID);
    }

    public Product findProduct(int productID){
        return keyToProduct.get(productID);
    }

    public boolean setDescription(int productID, String newDesc){
        if(!keyToProduct.containsKey(productID)){
            return false;
        }
        keyToProduct.get(productID).setDescription(newDesc);
        return true;
    }

    boolean reserveItems(Map<Integer, Integer> items){
        for(Integer item: items.keySet()){
            if(!isInStock(item)){
                return false;
            }
        }
        //all the item is in stock ,and we want to reserve them until payment
        for(Integer item: items.keySet()){
            reduceAmount(item ,items.get(item));
        }
        return true;
    }

    void restoreStock(Map<Integer, Integer> items){
        for(Integer item: items.keySet()){
            setAmount(item , getQuantity(item) + items.get(item));
        }
    }

    private class PricesAndQuantity{
        public int quantity;
        public double price;

        public PricesAndQuantity(int quantity, double price){
            this.price = price;
            this.quantity = quantity;
        }
    }

}
