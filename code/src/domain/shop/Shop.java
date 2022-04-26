package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
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
    private final int shopID;
    private int rank;
    private User ShopFounder;
    private List<User> ShopOwners;
    private List<User> ShopManagers;
    private Map<ShopManagersPermissions, List<String>> ShopManagersPermissionsMap;
    private List<User> Shoppers;
    private final Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final OrderHistory orders;
    private boolean isOpen;

    public Shop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy,String founderId, int shopID) {
        this.discountPolicy = discountPolicy;
        this.purchasePolicy = purchasePolicy;
        inventory = new Inventory();
        orders = new OrderHistory();
        ShopOwners = new LinkedList<>();
        ShopManagers = new LinkedList<>();
        Shoppers = new LinkedList<>();
        rank = -1;
        this.name = name;
        isOpen = true;
        ShopFounder = MarketSystem.getInstance().getUser(founderId);
        this.shopID = shopID;
        ShopManagersPermissionsMap = new HashMap<>();
        ShopManagersPermissionsInit();
    }

    public boolean addPermissions(List<ShopManagersPermissions> shopManagersPermissionsList, User tragetUser , String id) {
        if (ShopManagersPermissionsMap.get(ShopManagersPermissions.ChangeShopManagersPermissions).contains(id)) {
            for (ShopManagersPermissions run : shopManagersPermissionsList)
                if (!PermissionExist(ShopManagersPermissionsMap.get(run), tragetUser.getId())) {
                    ShopManagersPermissionsMap.get(run).add(tragetUser.getId());
                    eventLogger.logMsg(Level.INFO,String.format("add Permissions to user: %s",tragetUser.getId()));
                    eventLogger.logMsg(Level.INFO, String.format("add Permissions to user: %s", tragetUser.getId()));
                }
            return true;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
            return false;
        }
    }

    public boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, User tragetUser , String id) {
        if (ShopManagersPermissionsMap.get(ShopManagersPermissions.ChangeShopManagersPermissions).contains(id)) {
            for (ShopManagersPermissions run : shopManagersPermissionsList)
                if (PermissionExist(ShopManagersPermissionsMap.get(run), tragetUser.getId())) {
                    ShopManagersPermissionsMap.get(run).remove(tragetUser.getId());
                    eventLogger.logMsg(Level.INFO,String.format("remove Permissions to user: %s",tragetUser.getId()));
                    eventLogger.logMsg(Level.INFO, String.format("remove Permissions to user: %s", tragetUser.getId()));
                }
            return true;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
        }
        return false;
    }

    private boolean PermissionExist(List<String> permissionList,String userId){
        for (String run : permissionList){
            if(run.equals(userId))
                return true;
        }
        return false;
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
        Product product = inventory.findProduct(prodID);
        if(product != null) {
            eventLogger.logMsg(Level.INFO, String.format("returned product: %d", prodID));
            return product;
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            throw new NullPointerException("product not found");
        }
    }

    public Product addListing(String productName, String productDesc, String productCategory, double price, int quantity,String userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.AddProductToInventory).contains(userId))
            return inventory.addProduct(productName, productDesc, productCategory, price, quantity);
        return null;
    }

    public void removeListing(int prodID,String userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.RemoveProductFromInventory).contains(userId))
        inventory.removeProduct(prodID);
    }

    public boolean editPrice(int prodID, double newPrice,String userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.ChangeProductsDetail).contains(userId))
            return inventory.setPrice(prodID, newPrice);
        else return false;
    }

    public boolean editQuantity(int prodID, int newQuantity,String userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.ChangeProductsDetail).contains(userId))
            return inventory.setAmount(prodID, newQuantity);
        else return false;
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

    public void changeProductDetail(int prodID, String description,String userId){
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.ChangeProductsDetail).contains(userId))
            inventory.setDescription(prodID, description);
    }

    //todo????? needed?
    public void changeDiscountPolicy(DiscountPolicy discountPolicy){throw new UnsupportedOperationException();}
    //todo????? needed?
    public void changePurchasePolicy(PurchasePolicy purchasePolicy){throw new UnsupportedOperationException();}
    //todo????? needed?
    public void changeProductDiscount(PurchasePolicy purchasePolicy){throw new UnsupportedOperationException();}


    public boolean addPercentageDiscount(int prodID, double percentage){
        return discountPolicy.addPercentageDiscount(prodID, percentage);
    }

    public boolean addBundleDiscount(int prodID, int amountNeededToBuy, int amountGetFree){
        return discountPolicy.addBundleDiscount(prodID, amountNeededToBuy, amountGetFree);
    }

    public boolean purchasePolicyLegal(String userID, int prodID, double price,int amount){
        return purchasePolicy.checkIfProductRulesAreMet(userID, prodID, price, amount);
    }

    public double calculateTotalAmountOfOrder(Map<Integer, Integer> productAmountList){
        Map<Integer, Double> Product_totalPricePer = new HashMap<>();
        double totalPaymentDue = 0;
        double Product_price_single;
        double Product_total;
        for(Map.Entry<Integer, Integer> set : productAmountList.entrySet()){
            //check purchase policy regarding the Product
            Product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            Product_total = Product_price_single * set.getValue();
            Product_totalPricePer.put(set.getKey(), Product_total);
            totalPaymentDue += Product_total;
        }
        return totalPaymentDue;
    }

    /**
     * check out from the store the given items to a client
     * @param products the items that the client want to buy and the amount from each
     * @param totalAmount the total amount to pay for the tranaction. TODO to be removed
     * @param transaction the info of the client to be charged and supply
     * @return true if successfully created the order and add to the inventory
     */
    public ResponseT<Order> checkOut(Map<Integer,Integer> products, double totalAmount, TransactionInfo transaction){
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            //check purchase policy regarding the Product
            if (!purchasePolicyLegal(transaction.getUserID(), set.getKey(), inventory.getPrice(set.getKey()), set.getValue()))
                return new ResponseT("violates purchase policy");
        }
        synchronized (inventory) {
            if (!inventory.reserveItems(products)) {
                inventory.restoreStock(products);
                return new ResponseT<>("not in stock");
            }
        }


        //calculate price

        Map<Integer, Double> product_PricePer = new HashMap<>();
        double product_price_single;
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            product_PricePer.put(set.getKey(), product_price_single);
        }


        MarketSystem market = MarketSystem.getInstance();
        if(!market.pay(transaction)){
            inventory.restoreStock(products);
            return new ResponseT<>();
        }
        if(!market.supply(transaction, products)){
            return new ResponseT<>("problem with supply system");
        }
        // creating Order object to store in the Order History with unmutable copy of product
        Order o = createOrder(products, transaction, product_PricePer);
        return new ResponseT(o);
    }

    private Order createOrder(Map<Integer, Integer> products, TransactionInfo transaction, Map<Integer, Double> product_PricePer) {
        List<Product> boughtProducts = new ArrayList<>();
        for(Integer Product: products.keySet()){
            Product p = inventory.findProduct(Product);
            double price = product_PricePer.get(Product);
            boughtProducts.add(new ProductHistory(p,price , products.get(Product)));
        }
        Order o = new Order(boughtProducts, transaction.getTotalAmount(), transaction.getUserID());
        orders.addOrder(o);
        return o;
    }

    public boolean isFounder(String id) {
        return ShopFounder.getId().equals(id);
    }

    public boolean isOwner(String id) {
        for(User run :ShopOwners){
            if (run.getId().equals(id))
                return true;
        }
        return false;
    }

    public String AppointNewShopOwner(String tragetUser, String userId) {
        if(ShopManagersPermissionsMap.get(ShopManagersPermissions.AppointNewShopOwner).contains(userId)) {
            synchronized (this) {
                User newOwner = MarketSystem.getInstance().getUser(tragetUser);
                if (newOwner != null)
                    if (!ShopOwners.contains(newOwner))
                        ShopOwners.add(newOwner);
                eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopOwner User: %s", tragetUser));
                return String.format("Appoint New ShopOwner User: %s", tragetUser);
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("Appoint New ShopOwner User: %s", tragetUser));
        return String.format("attempt to appoint New ShopOwner User: %s filed", tragetUser);
    }

    public int getShopID() {
        return shopID;
    }

    public String AppointNewShopManager(String usertarget, String userId) {
        if (ShopManagersPermissionsMap.get(ShopManagersPermissions.AppointNewShopOwner).contains(userId)) {
            synchronized (this) {
                User newManager = MarketSystem.getInstance().getUser(usertarget);
                if (newManager != null)
                    if (!ShopManagers.contains(newManager))
                        ShopManagers.add(newManager);
                eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", usertarget));
                return String.format("Appoint New ShopManager User: %s", usertarget);
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
        return String.format("attempt to appoint New ShopManager User: %s filed", usertarget);
    }

    public void closeShop(String userID){
        if((ShopManagersPermissionsMap.get(ShopManagersPermissions.CloseShop).contains(userID)))
            if(isOpen)
                isOpen = false;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void RequestInformationOnShopsOfficials(){
        throw new UnsupportedOperationException();
    }

    public void RequestInformationOfShopsSalesHistory(){
        throw new UnsupportedOperationException();
    }

    public ShopInfo getShopInfo() {
        return new ShopInfo(name, rank);
    }

    public List<ProductInfo> getProductInfoOfShop() {
        List<ProductInfo> info = inventory.getAllProductInfo();
        for (ProductInfo p : info) {
            p.setShopName(name);
            p.setShopRank(rank);
        }
        return info;
    }

    public ProductInfo getInfoOnProduct(int productId){
        ProductInfo p;
        synchronized (inventory){
            p = inventory.getProductInfo(productId);
        }
        if(p == null){
            //log
            return null;
        }

        p.setShopName(name);
        p.setShopRank(rank);
        return p;

    }

}
