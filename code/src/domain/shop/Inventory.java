package domain.shop;

import java.util.*;

public class Inventory {
    private final Map<Integer, ProductImp> keyToProduct;
    private final Map<ProductImp, PricesAndQuantity> items;
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
        return getQuantity(keyToProduct.get(product)) > 0;
    }

    public double getPrice(Product p){
        if(p == null || !items.containsKey(p))
            return -1;
        return items.get(p).price;
    }

    public int getQuantity(int product){
        if(keyToProduct.containsKey(product))
            return -1;
        return items.get(keyToProduct.get(product)).quantity;
    }

    public boolean addProduct(int prodID, double price, int quantity) {
        Product product = productList.getProduct(prodID);
        if(product == null || keyToProduct.containsKey(prodID)){
            return false;
        }
        if(price < 0.0 || quantity < 0)
            return false;
        items.put(product/*todo: config to impl*/ ,new PricesAndQuantity(quantity, price));
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

    private class PricesAndQuantity{
        public int quantity;
        public double price;

        public PricesAndQuantity(int quantity, double price){
            this.price = price;
            this.quantity = quantity;
        }
    }

}
