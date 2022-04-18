package domain.shop;


import domain.Tuple;

import java.util.Map;
import java.util.function.Function;

public class Inventory {
    private Map<Product, Tuple<Integer,Integer>> products;

    private Inventory(){}
    public void addProuct(Product p, Tuple<Integer,Integer> quantityPrice) {
        products.computeIfAbsent(p, (Function<? super Product, ? extends Tuple<Integer, Integer>>) quantityPrice);
    }

    public void reomveProduct(Product product) {
        products.remove(product);
    }
}
