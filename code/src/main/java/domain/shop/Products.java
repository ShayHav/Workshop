package domain.shop;

import java.util.List;

public class Products {
    private List<Product> Products;
    private static Products products;


    public static Products getInstance()
    {
        if (products == null)
            products = new Products();
        return products;
    }

    private Products(){}

    public Product getProduct(int prodID){
        for (Product prod: Products){
            if(prod.getName().equals(prodID))
                return prod;
            //todo: access database if needed, lazy loading
        }
        return null;
    }

    public void addProduct(){}

}
