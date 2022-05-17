package Presentation.Model;

import domain.shop.Product;
import domain.user.ShoppingBasket;

import java.util.HashMap;
import java.util.Map;

public class PresentationBasket {
    public int shopId;
    public String shopName;
    public Map<PresentationProduct, Integer> productWithAmount;
    public double totalAmount;

    public PresentationBasket(int shopId, String shopName, Map<PresentationProduct,Integer> productWithAmount, double totalAmount){
        this.shopId = shopId;
        this.shopName = shopName;
        this.productWithAmount = productWithAmount;
        this.totalAmount = totalAmount;
    }

    public PresentationBasket(ShoppingBasket.ServiceBasket basket) {
        shopId = basket.getShopId();
        shopName = basket.getShopName();
        totalAmount = basket.getTotalAmount();

        productWithAmount = new HashMap<>();
        Map<Product,Integer> products = basket.getProductWithAmount();
        for(Product p: products.keySet()){
            int amount = products.get(p);
            productWithAmount.put(new PresentationProduct(p),amount);
        }
    }
}
