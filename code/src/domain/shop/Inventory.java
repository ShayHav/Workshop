package domain.shop;

import domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<Product, Integer> products;

    private Inventory(){}
    public void addProuct(Product p,int quantity) {
        Integer q = new Integer(quantity);
        products.computeIfAbsent(p,q);
    }
}
