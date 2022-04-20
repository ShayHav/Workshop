package domain.shop;

import java.util.*;

public class Inventory {
    private final Map<Integer, Product> keyToProduct;
    private final Map<Product, PricesAndQuantity> items;

    public Inventory(){
        items = new HashMap<>();
        keyToProduct = new HashMap<>();
    }

    /**
     * check if a product is in stock
     * @param p the product querying
     * @return true if there is at least one item in the inventory
     */
    public boolean isInStock(int product){
        return getQuantity(keyToProduct.get(product)) > 0;
    }

    public double getPrice(Product p){
        if(p == null || !items.containsKey(p))
            return -1;
        return items.get(p).price;
    }

    public int getQuantity(Product p){
        if(p == null || !items.containsKey(p))
            return -1;
        return items.get(p).quantity;
    }

    public boolean addProduct(String p, double price, int quantity) {
        if(p == null || items.containsKey(p)){
            return false;
        }
        if(price < 0.0 || quantity < 0)
            return false;
        items.put(p,new PricesAndQuantity(quantity, price));
        return true;
    }

    public boolean setPrice(Product p, double newPrice){
        if(p == null || newPrice < 0.0)
            return false;
        if(items.containsKey(p)){
            items.get(p).price = newPrice;
            return true;
        }
        return false;
    }

    public boolean setAmount(Product p, int newAmount){
        if(p == null || newAmount < 0 || !items.containsKey(p))
            return false;
        items.get(p).quantity = newAmount;
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

    public void removeProduct(Product product) {
        if(product == null)
            return;
        items.remove(product);
    }

    public List<Product> getItemsInStock() {
        List<Product> products = new ArrayList<>();
        for(Product p : items.keySet()){
            if(items.get(p).quantity > 0){
                products.add(p);
            }
        }
        return products;
    }

    public Product getProduct(int productID){
        return keyToProduct.get(productID);
    }

    private class PricesAndQuantity{
        public int quantity;
        public double price;

        public PricesAndQuantity(int quantity, double price){
            this.price = price;
            this.quantity = quantity;
        }
    }
    public Product findProduct(int productID){
        return keyToProduct.get(productID);
    }
}
