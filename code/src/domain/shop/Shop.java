package domain.shop;

import domain.Tuple;
import domain.user.User;

import java.util.List;
import domain.user.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String getItemInfo(int prodID){
        try{
            return getProduct(prodID).getDescription();
        }catch (NullPointerException npe){
            return "this product does not exist.";
        }
    }

    private List<Product> getItemsInStock(){
        //might be an empty list, make sure
        return inventory.getItemsInStock();
    }

    private Product getProduct(int prodID){
        Product product = inventory.getProduct(prodID);
        if(product != null)
            return product;
        else
            throw new NullPointerException("product not found");
    }

    public boolean addListing(int prodID, double price, int quantity){
        return inventory.addProduct(prodID, price, quantity);
    }

    public void removeListing(int prodID){
        inventory.removeProduct(prodID);
    }

    public boolean editPrice(int prodID, double newPrice){
        return inventory.setPrice(prodID, newPrice);
    }

    public boolean editQuantity(int prodID, int newQuantity){
        return inventory.setAmount(prodID, newQuantity);
    }


    public int shopDiscountPolicy(int purchase){

    }

    public int productDiscount(int prodID){return 0;}

    public boolean isProductIsAvailable(int prodID){return true;}

    public void addProductToInventory(Product product, Tuple<Integer,Integer> quantityPrice){inventory.addProuct(product,quantityPrice);}

    public void removeProductFromInventory(int prodID){
        //check if the product on demands.
        inventory.removeProduct(prodID);
    }

    public void changeProductDetail(int prodID, String description){
        inventory.setDescription(prodID, description);
    }

    public void changeDiscountPolicy(DiscountPolicy discountPolicy){}

    public void changePurchasePolicy(PurchasePolicy purchasePolicy){}

    public void changeProductDiscount(PurchasePolicy purchasePolicy){}

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
        //TODO: call to payment and shipment and return appropriate answer
        List<Product> boughtProducts = new ArrayList<>();
        for(Integer item: items.keySet()){
            Product p = inventory.findProduct(item);
            double price = inventory.getPrice(p);
            boughtProducts.add(new ProductHistory(p,price ,items.get(item)));
        }
        Order o = new Order(boughtProducts, totalAmount, transaction.getUserID());
        return 0;
    }
}
