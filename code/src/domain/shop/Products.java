package domain.shop;

import java.util.List;

public class Products {
    private List<Product> Products;

    public Products(){}

    public Product getProduct(String prodName){
        for (Product prod: Products){
            if(prod.getName().equals(prodName))
                return prod;
            //todo: access database if needed, lazy loading
        }
        return null;
    }

    public void addProduct(){}

}
