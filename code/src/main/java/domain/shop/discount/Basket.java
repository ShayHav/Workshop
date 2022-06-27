package domain.shop.discount;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.ProductImp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket extends HashMap<ProductImp, Integer> {

    double basePrice;

    public Basket(){
        super();
        this.basePrice = 0;
    }

    public Basket(Basket existingBasket){
        this.putAll(existingBasket);
        this.basePrice = existingBasket.getBasePrice();
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
        int amount = 0;
        for (Map.Entry<ProductImp, Integer> product: this.entrySet()){
            if (product.getKey().getId() == prodID)
                amount += product.getValue();
        }
        return  amount;
    }

    public List<String> findAllDistinctCategories(){
        List<String> distinctCategories = new ArrayList<>();
        for(ProductImp product : this.keySet()) {
            distinctCategories.add(product.getCategory());
        }
        return distinctCategories.stream().distinct().collect(Collectors.toList());
    }

    public double getBasePrice(){
        return basePrice;
    }

    public void setBasePrice(double basePrice){
        this.basePrice = basePrice;
    }


}
