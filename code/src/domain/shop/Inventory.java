package domain.shop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final Map<Product, PricesAndQuantity> items;

    private Inventory(){
        items = new HashMap<>();
    }

    public boolean isInStock(Product p){
        return getQuantity(p) > 0;
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

    public void removeProduct(Product product) {
        if(product == null)
            return;
        items.remove(product);
    }

    public List<Product> getItemsInStock() {
    return new LinkedList<>(); //todo
    }

    public Product getProduct(String prodName){return null; //todo
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
