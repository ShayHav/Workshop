package domain.shop;

import domain.Tuple;
import domain.user.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shop {

    /*
        TODO: Decide if to create class which will handle roles of member os just a Map attribute in shop
     */
//
    private Inventory inventory;
//    private DiscountPolicy discountPolicy;
//    private PurchasePolicy purchasePolicy;
//
//
//    public Shop(DiscountPolicy discountPolicy,PurchasePolicy purchasePolicy){
//        this.discountPolicy=discountPolicy;
//        this.purchasePolicy=purchasePolicy;
//    }
//
//    /***
//     *
//     * @param s
//     * @return
//     */
//    public String gettingInformation(String s){return null;}
//
//    public String gettingInformationProduct(String s){return null;}
//
//    public Food[] searchItems(String s){return null;}
//
//    public int shopDiscountPolicy(int purchase){return 0;}
//
//    public int productDiscount(int id){return 0;}
//
//    public boolean isProductIsAvailable(int id){return true;}
//
//    public void addProductToInventory(Product product, Tuple<Integer,Integer> quantityPrice){inventory.addProuct(product,quantityPrice);}
//
//    public void removeProductFromInventory(Product product){
//        //check if the product on demands.
//        inventory.reomveProduct(product);
//    }
//
//    public void changeProductDetail(Product p, String s, String cat){}
//
//    public void changeDiscountPolicy(DiscountPolicy discountPolicy){}
//
//    public void changePurchasePolicy(PurchasePolicy purchasePolicy){}
//
//    public void changeProductDiscount(PurchasePolicy purchasePolicy){}

    public double calculateTotalAmountOfOrder(Map<Integer,Integer> itemsAndQuantity){
        return 0.0; //TODO: impl
    }

    public int checkOut(Map<Integer,Integer> items, double totalAmount, TransactionInfo transaction){
        //TODO lock inventory to check stock
        for(Integer item: items.keySet()){
            if(!inventory.isInStock(item)){
                return -1 ;// whereve status to the user
            }
        }
        //all the item is in stock and we want to reserve them untill payment
        for(Integer item: items.keySet()){
            inventory.reduceAmount(items.get(item));
        }
        //TODO: call to payment and shipment and return appropraite answer
        List<Product> boughtProducts = new ArrayList<>();
        for(Integer item: items.keySet()){
            Product p = inventory.findProduct(item);
            double price = inventory.getPrice(p);
            boughtProducts.add(new ProductHistory(p,price ,items.get(item)));
        }
        Order o = new Order(boughtProducts, totalAmount);
        return 0;
    }
}
