package domain.shop;

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

    public Product[] searchItems(String s){return null;}

    public int shopDiscountPolicy(int purchase){return 0;}

    public int productDiscount(int id){return 0;}

    public boolean isProductIsAvailable(int id){return true;}

    public void addProductToInventory(Product product,int quantity){inventory.addProuct(product,quantity);}

    

}
