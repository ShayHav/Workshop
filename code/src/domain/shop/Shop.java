package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.user.User;
import org.w3c.dom.events.Event;

import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class Shop {
    private String name;
    private int rank;
    private User ShopFounder;
    private List<User> ShopOwners;
    private List<User> ShopManagers;
    private List<User> Shoppers;
    private Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private OrderHistory orders;


    public Shop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy){
        this.discountPolicy=discountPolicy;
        this.purchasePolicy=purchasePolicy;
        orders = new OrderHistory();
        rank = 0;
        this.name = name;
    }


    public String getItemInfo(int prodID){
        try{
            String info = getProduct(prodID).getDescription();
            eventLogger.logMsg(Level.INFO, String.format("returned description of product: %d ", prodID));
            return info;
        }catch (NullPointerException npe){
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            return "this product does not exist.";
        }
    }

    public List<Product> getItemsInStock(){
        //might be an empty list, make sure
        return inventory.getItemsInStock();
    }

    public Product getProduct(int prodID){
        Product product = inventory.getProduct(prodID);
        if(product != null) {
            eventLogger.logMsg(Level.INFO, String.format("returned product: %d", prodID));
            return product;
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            throw new NullPointerException("product not found");
        }
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

    public List<Discount> productDiscount(int prodID){
        return discountPolicy.getAllDiscountsForProd(prodID);
    }

    public double productPriceAfterDiscounts(int prodID, int amount){
        double productBasePrice = inventory.getPrice(prodID);
        return discountPolicy.calcPricePerProduct(prodID, productBasePrice, amount);
    }

    public boolean isProductIsAvailable(int prodID){
        return inventory.isInStock(prodID);
    }

    public void changeProductDetail(int prodID, String description){
        inventory.setDescription(prodID, description);
    }

    //todo????? needed?
    public void changeDiscountPolicy(DiscountPolicy discountPolicy){}
    //todo????? needed?
    public void changePurchasePolicy(PurchasePolicy purchasePolicy){}
    //todo????? needed?
    public void changeProductDiscount(PurchasePolicy purchasePolicy){}

    public double calculateTotalAmountOfOrder(Map<Integer,Integer> itemsAndQuantity){
        return 0.0; //TODO: impl
    }

    public boolean addPercentageDiscount(int prodID, double percentage){
        return discountPolicy.addPercentageDiscount(prodID, percentage);
    }

    public boolean addBundleDiscount(int prodID, int amountNeededToBuy, int amountGetFree){
        return discountPolicy.addBundleDiscount(prodID, amountNeededToBuy, amountGetFree);
    }


    public boolean purchasePolicyLegal(int userID, int prodID, double price,int amount){
        return purchasePolicy.checkIfProductRulesAreMet(userID, prodID, price, amount);
    }

    public int checkOut(Map<Integer,Integer> items, double totalAmount, TransactionInfo transaction){
        //TODO lock inventory to check stock
        for(Map.Entry<Integer, Integer> set : items.entrySet()){
            if(!inventory.isInStock(set.getKey())){
                return -1;
            }
            //todo:check purchase policy regarding the item
            //if (purchasePolicyLegal(transaction.getUserid(), set.getKey(), inventory.getPrice(set.getKey()), ))
            //todo:check discount policy regarding the item
        }
        for(Integer item: items.keySet()){
            if(!inventory.isInStock(item)){
                return -1 ;
            }
        }
        //all the item is in stock ,and we want to reserve them until payment
        for(Integer item: items.keySet()){
            inventory.reduceAmount(item ,items.get(item));
        }
        //TODO: call to payment and shipment and return appropriate answer
        List<Product> boughtProducts = new ArrayList<>();
        for(Integer item: items.keySet()){
            Product p = inventory.findProduct(item);
            double price = inventory.getPrice(p.getId());
            boughtProducts.add(new ProductHistory(p,price ,items.get(item)));
        }
        Order o = new Order(boughtProducts, totalAmount, transaction.getUserid());
        orders.addOrder(o);
        return 0;
    }
}
