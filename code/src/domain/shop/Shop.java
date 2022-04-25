package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Response;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.user.TransactionInfo;
import domain.user.User;

import java.util.*;

import java.util.logging.Level;

public class Shop {
    private String name;
    private int shopID;
    private int rank;
    private User ShopFounder;
    private List<User> ShopOwners;
    private List<User> ShopManagers;
    Map<ShopManagersPermissions, List<Integer>> ShopManagersPermissionsMap;
    private List<User> Shoppers;
    private final Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final OrderHistory orders;
    private boolean isOpen;

    public Shop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy,int founderId, int shopID) {
        this.discountPolicy = discountPolicy;
        this.purchasePolicy = purchasePolicy;
        inventory = new Inventory();
        orders = new OrderHistory();
        ShopOwners = new LinkedList<>();
        ShopManagers = new LinkedList<>();
        Shoppers = new LinkedList<>();
        rank = 0;
        this.name = name;
        isOpen = true;
        ShopFounder = MarketSystem.getInstance().getUser(founderId);
        this.shopID = shopID;
        ShopManagersPermissionsMap = new HashMap<>();
        ShopManagersPermissionsInit();
    }

    private void ShopManagersPermissionsInit(){
        for (ShopManagersPermissions myVar : ShopManagersPermissions.values()){
            ShopManagersPermissionsMap.put(myVar, new LinkedList<>());
        }
    }

    public String getProductInfo(int prodID){
        try{
            String info = getProduct(prodID).getDescription();
            eventLogger.logMsg(Level.INFO, String.format("returned description of product: %d ", prodID));
            return info;
        }catch (NullPointerException npe){
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            return "this product does not exist.";
        }
    }


    public List<Product> getProductsInStock(){
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

    public boolean addListing(int prodID, double price, int quantity,int userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.AddProductToInventory).contains(userId))
            return inventory.addProduct(prodID, price, quantity);
        else return false;
    }

    public void removeListing(int prodID,int userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.RemoveProductToInventory).contains(userId))
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

    public double calculateTotalAmountOfOrder(Map<Integer,Integer> ProductsAndQuantity){
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

    public ResponseT checkOut(Map<Integer,Integer> Products, double totalAmount, TransactionInfo transaction){
        for(Map.Entry<Integer, Integer> set : Products.entrySet()){
            //check purchase policy regarding the Product
            if (!purchasePolicyLegal(transaction.getUserID(), set.getKey(), inventory.getPrice(set.getKey()), set.getValue()))
                return new ResponseT("violates purchase policy");
        }
        synchronized (inventory) {
            if (!inventory.reserveItems(Products)) {
                inventory.restoreStock(Products);
                return new ResponseT("not in stock");
            }
        }


        //calculate price
        Map<Integer, Double> Product_totalPricePer = new HashMap<>();
        double totalPaymentDue = 0;
        double Product_price_single;
        double Product_total;
        for(Map.Entry<Integer, Integer> set : Products.entrySet()){
            //check purchase policy regarding the Product
            Product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            Product_total = Product_price_single * set.getValue();
            Product_totalPricePer.put(set.getKey(), Product_total);
            totalPaymentDue += Product_total;
        }

        MarketSystem market = MarketSystem.getInstance();
        if(!market.pay(transaction)){
            inventory.restoreStock(Products);
            return new ResponseT();
        }
        if(!market.supply(transaction)){
            return new ResponseT("problem with supply system");
        }
        // creating Order object to store in the Order History with unmutable copy of product
        List<Product> boughtProducts = new ArrayList<>();
        for(Integer Product: Products.keySet()){
            Product p = inventory.findProduct(Product);
            double price = inventory.getPrice(p.getId());
            boughtProducts.add(new ProductHistory(p,price ,Products.get(Product)));
        }
        Order o = new Order(boughtProducts, totalAmount, transaction.getUserID());
        orders.addOrder(o);
        return new ResponseT(o);
    }

    public boolean isFounder(int id) {
        return ShopFounder.getId() == id;
    }

    public boolean isOwner(int id) {
        for(User run :ShopOwners){
            if (run.getId()==id)
                return true;
        }
        return false;
    }

    public void setOwner(int id) {
        User newOwner =MarketSystem.getInstance().getUser(id);
        if(newOwner!=null)
            if(!ShopOwners.contains(newOwner))
                ShopOwners.add(newOwner);
    }

    public int getShopID() {
        return shopID;
    }

    public void setManager(int id) {
        User newManager =MarketSystem.getInstance().getUser(id);
        if(newManager!=null)
            if(!ShopManagers.contains(newManager))
                ShopManagers.add(newManager);
    }

    public void closeShop(){
        if(isOpen)
            isOpen = false;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
