package domain.shop;

import domain.Tuple;

public class Shop {

    private Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;


    public Shop(DiscountPolicy discountPolicy,PurchasePolicy purchasePolicy){
        this.discountPolicy=discountPolicy;
        this.purchasePolicy=purchasePolicy;
    }


    public String gettingInformation(String s){return null;}

    public String gettingInformationProduct(String s){return null;}

    public Food[] searchItems(String s){return null;}

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
