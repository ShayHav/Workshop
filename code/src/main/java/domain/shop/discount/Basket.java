package domain.shop.discount;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.ProductImp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket extends HashMap<ProductImp, Integer> {
    public Basket(){
        super();
    }

    public Basket(Map<ProductImp, Integer> existingBasket){
        this.putAll(existingBasket);
    }

    public double calculateTotal(){
        double total = 0;
        for(Map.Entry<ProductImp, Integer> productInCart: this.entrySet())
            total += (productInCart.getKey().getBasePrice() * productInCart.getValue());
        return total;
    }

    public ProductImp findProduct(int prodID) throws ProductNotFoundException {
        for (ProductImp product: this.keySet()){
            if (product.getId() == prodID)
                return product;
        }
        throw new ProductNotFoundException();
    }

    public int findAmount(int prodID) {
        for (Map.Entry<ProductImp, Integer> product: this.entrySet()){
            if (product.getKey().getId() == prodID)
                return product.getValue();
        }
        return  0;
    }

    public List<String> findAllDistinctCategories(){
        List<String> distinctCategories = new ArrayList<>();
        for(ProductImp product : this.keySet()) {
            distinctCategories.add(product.getCategory());
        }
        return distinctCategories.stream().distinct().collect(Collectors.toList());
    }


}
