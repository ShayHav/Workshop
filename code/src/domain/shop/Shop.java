package domain.shop;

import domain.Tuple;
import domain.user.User;

import java.util.List;

public class Shop {
    private User ShopFounder;
    private List<User> ShopOwners;
    private List<User> ShopManagers;
    private List<User> Shoppers;
    private Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;

 public Shop(DiscountPolicy discountPolicy,PurchasePolicy purchasePolicy){
        this.discountPolicy=discountPolicy;
        this.purchasePolicy=purchasePolicy;
    }

    /***
     *
     * @param s
     * @return
     */
    public String getItemInfo(String s){return null;}

    private List<Product> getItemsInStock(){
        return inventory.getItemsInStock();
    }

    private Product getProduct(String prodName){
        return inventory.getProduct(prodName);
    }

    private void addListing(){}

    public int shopDiscountPolicy(int purchase){return 0;}

    public int productDiscount(int id){return 0;}

    public boolean isProductIsAvailable(int id){return true;}

    public void addProductToInventory(Product product, Tuple<Integer,Integer> quantityPrice){inventory.addProuct(product,quantityPrice);}

    public void removeProductFromInventory(Product product){
        //check if the product on demands.
        inventory.reomveProduct(product);
    }

    public void changeProductDetail(Product p, String s, String cat){}

    public void changeDiscountPolicy(DiscountPolicy discountPolicy){}

    public void changePurchasePolicy(PurchasePolicy purchasePolicy){}

    public void changeProductDiscount(PurchasePolicy purchasePolicy){}

}
