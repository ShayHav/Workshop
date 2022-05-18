package domain.shop;

import domain.ControllersBridge;
import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.filter.*;

import java.util.*;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shop {
    private String name;
    private final int shopID;
    private int rank;
    private User ShopFounder;
    private String description;
    private Map<String,User> ShopOwners;
    private Map<String,User> ShopManagers;
    private ShopManagersPermissionsController shopManagersPermissionsController;
    private final Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final OrderHistory orders;
    private boolean isOpen;
    private MarketSystem marketSystem;

    public Shop(String name,String description, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, User shopFounder, int shopID) {
        this.discountPolicy = discountPolicy;
        this.purchasePolicy = purchasePolicy;
        inventory = new Inventory();
        orders = new OrderHistory();
        ShopOwners = new HashMap<>();
        ShopManagers = new HashMap<>();
        rank = -1;
        this.name = name;
        isOpen = true;
        this.description = description;
        this.ShopFounder = shopFounder;
        this.shopID = shopID;
        shopManagersPermissionsController = new ShopManagersPermissionsController();
        shopManagersPermissionsController.addPermissions(getAllPermissionsList(), shopFounder.getUserName());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public synchronized boolean addPermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String id) {
        if (shopManagersPermissionsController.canChangeShopManagersPermissions(id)| ShopOwners.containsKey(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot change permissions", id) );
            return false;
        }
    }

    public synchronized boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id) {
        if (shopManagersPermissionsController.canChangeShopManagersPermissions(id)| ShopOwners.containsKey(id)) {
            return shopManagersPermissionsController.removePermissions(shopManagersPermissionsList, tragetUser);
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot remove permissions", id));
            return false;
        }
    }

    public int getRank() {
        return rank;
    }

    public String getProductInfo(int prodID){
        try{
            String info = getProduct(prodID).getDescription();
            eventLogger.logMsg(Level.INFO, String.format("returned description of product: %d ", prodID));
            return info;
        }catch (ProductNotFoundException npe){
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            return "this product does not exist.";
        }
    }


    public List<Product> getProductsInStock(){
        //might be an empty list, make sure
        return inventory.getItemsInStock();
    }

    public Product getProduct(int prodID) throws ProductNotFoundException {
        Product product = inventory.findProduct(prodID);
        if(product != null) {
            eventLogger.logMsg(Level.INFO, String.format("returned product: %d", prodID));
            return product;
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("could not find product: %d", prodID));
            throw new ProductNotFoundException("product not found");
        }
    }

    public synchronized Product addListing(int serialNumber, String productName, String productDesc, String productCategory, double price, int quantity,String userId) throws InvalidAuthorizationException, InvalidProductInfoException {
        if(shopManagersPermissionsController.canAddProductToInventory(userId)| ShopOwners.containsKey(userId))
            return inventory.addProduct(serialNumber, productName, productDesc, productCategory, price, quantity);
        else
            throw new InvalidAuthorizationException("you do not have permission to list a product to this shop");
    }

    public synchronized void removeListing(int prodID,String userId) throws InvalidAuthorizationException {
        if(shopManagersPermissionsController.canRemoveProductFromInventory(userId)| ShopOwners.containsKey(userId))
            inventory.removeProduct(prodID);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editPrice(int prodID, double newPrice,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if (shopManagersPermissionsController.canChangeProductsDetail(userId) | ShopOwners.containsKey(userId))
            inventory.setPrice(prodID, newPrice);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editQuantity(int prodID, int newQuantity,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if(shopManagersPermissionsController.canChangeProductsDetail(userId)| ShopOwners.containsKey(userId))
            inventory.setAmount(prodID, newQuantity);
        else
            throw new InvalidAuthorizationException("you can edit quantity of a product in this shop");
    }

    public List<Discount> productDiscount(int prodID){
        return discountPolicy.getAllDiscountsForProd(prodID);
    }

    public double productPriceAfterDiscounts(int prodID, int amount){
        double productBasePrice;
        try {
            productBasePrice = inventory.getPrice(prodID);
        }catch (ProductNotFoundException prodNotFound){
            return 0;
        }
        if(productBasePrice < 0)
            return 0;
        if(discountPolicy == null)
            return productBasePrice;

        return discountPolicy.calcPricePerProduct(prodID, productBasePrice, amount);
    }

    public boolean isProductIsAvailable(int prodID, int quantity) throws ProductNotFoundException {
        return inventory.getQuantity(prodID) >= quantity;
    }

    public synchronized Product changeProductDetail(int prodID, String name, String description, String category,String userId, int amount, double price) throws InvalidProductInfoException, ProductNotFoundException {
        if(shopManagersPermissionsController.canChangeProductsDetail(userId)| ShopOwners.containsKey(userId)) {
            inventory.setAmount(prodID, amount);
            inventory.setPrice(prodID, price);
            return inventory.setProduct(prodID, name, description, category);
        }
        return null;
    }
    public boolean isEnoughAmountOfProduct(int prodID, int amount){
        int quantity;

        try {
            quantity = inventory.getQuantity(prodID);
        }catch (ProductNotFoundException productNotFoundException){
            return false;
        }

        return quantity >= amount;

    }

    public synchronized int addPercentageDiscount(int prodID, double percentage){
        return discountPolicy.addPercentageDiscount(prodID, percentage);
    }

    public synchronized int addBundleDiscount(int prodID, int amountNeededToBuy, int amountGetFree){
        return discountPolicy.addBundleDiscount(prodID, amountNeededToBuy, amountGetFree);
    }

    public boolean purchasePolicyLegal(String userID, int prodID, double price,int amount){
        return purchasePolicy.checkIfProductRulesAreMet(userID, prodID, price, amount);
    }

    public double calculateTotalAmountOfOrder(Map<Integer, Integer> productAmountList){
        double totalPaymentDue = 0;
        double Product_price_single;
        double Product_total;
        for(Map.Entry<Integer, Integer> set : productAmountList.entrySet()){
            //check purchase policy regarding the Product
            Product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            Product_total = Product_price_single * set.getValue();
            totalPaymentDue += Product_total;
        }
        return totalPaymentDue;
    }

    /**
     * check out from the store the given items to a client
     * @param products the items that the client want to buy and the amount from each
     * @param transaction the info of the client to be charged and supply
     * @return true if successfully created the order and add to the inventory
     */
    public ResponseT<Order> checkout(Map<Integer,Integer> products, TransactionInfo transaction) throws BlankDataExc {
        double productBasePrice;
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            //check purchase policy regarding the Product
            try {
                productBasePrice = inventory.getPrice(set.getKey());
            }catch (ProductNotFoundException prodNotFound){
                return new ResponseT<>(String.format("this product does not exist in shop %d",shopID));
            }
            if (!purchasePolicyLegal(transaction.getUserID(), set.getKey(), productBasePrice, set.getValue()))
                return new ResponseT<>(String.format("checkout process violates purchase policy in shop %d",shopID));
        }
        synchronized (inventory) {
            if (!inventory.reserveItems(products)) {
                try{
                    inventory.restoreStock(products);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResponseT<>(String.format("one of items selected is not in stock in shop %d",shopID));
            }
        }


        //calculate price
        Map<Integer, Double> product_PricePer = new HashMap<>();
        double product_price_single;
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            product_PricePer.put(set.getKey(), product_price_single);
        }

        if(!marketSystem.pay(transaction)){
            synchronized (inventory) {
                try {
                    inventory.restoreStock(products);
                }catch (Exception e){
                    errorLogger.logMsg(Level.SEVERE, "couldn't restock, Fatal. Explanation:\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
            return new ResponseT<>(String.format("checkout in shop %d failed: problem with pay system, please contact the company representative", shopID));
        }
        if(!marketSystem.supply(transaction, products)){
            return new ResponseT<>(String.format("checkout in shop %d failed: problem with supply system, please contact the company representative", shopID));
        }
        // creating Order object to store in the Order History with unmutable copy of product
        Order o = createOrder(products, transaction, product_PricePer);
        return new ResponseT<>(o);
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
        return ShopFounder.getUserName().equals(id);
    }

    public boolean isOwner(String id) {
        return ShopOwners.containsKey(id);
    }

    public String AppointNewShopOwner(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if (shopManagersPermissionsController.canAppointNewShopOwner(userId)| ShopOwners.containsKey(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointOwner(shopID)){
                        ShopManagers.putIfAbsent(usertarget, newManager);
                        newManager.addRole(shopID,Role.ShopOwner);
                        eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", usertarget));
                        return String.format("Appoint New ShopManager User: %s", usertarget);
                    }
                }
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
        return String.format("attempt to appoint New ShopManager User: %s filed", usertarget);
    }

    public int getShopID() {
        return shopID;
    }

    public String AppointNewShopManager(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if (shopManagersPermissionsController.canAppointNewShopManager(userId)| ShopOwners.containsKey(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointManager(shopID)){
                        ShopManagers.putIfAbsent(usertarget, newManager);
                        newManager.addRole(shopID,Role.ShopManager);
                        eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", usertarget));
                        return String.format("Appoint New ShopManager User: %s", usertarget);
                    }
                }
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
        return String.format("attempt to appoint New ShopManager User: %s filed", usertarget);
    }

    public synchronized void closeShop(String userID) throws InvalidSequenceOperationsExc {
        if(shopManagersPermissionsController.canCloseShop(userID)) {
            if (isOpen)
                isOpen = false;
            else throw new InvalidSequenceOperationsExc(String.format("attempt to Close Closed Shop userID: %s",userID));
        }
    }
    public synchronized void openShop(String userID) throws InvalidSequenceOperationsExc {
        if(shopManagersPermissionsController.canOpenShop(userID)) {
            if (!isOpen)
                isOpen = true;
            else throw new InvalidSequenceOperationsExc(String.format("attempt to Open Opened Shop userID: %s",userID));
        }
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }


    public void RequestInformationOfShopsSalesHistory(){
        throw new UnsupportedOperationException();
    }

    public ShopInfo getShopInfo() {
        if(this.isOpen)
            return new ShopInfo(name, rank);
        return null;
    }

    public List<Product> getProductInfoOfShop() {
        List<Product> info = inventory.getAllProductInfo();
        return info;
    }

    public Product getInfoOnProduct(int productId) throws ProductNotFoundException {
        Product p;
        synchronized (inventory){
            p = inventory.getProductInfo(productId);
        }
        p.setShopRank(rank);
        return p;

    }

    public List<Order> getOrders() {
        return orders.getOrders();
    }


    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy = discountPolicy;
    }

    public void setPurchasePolicy(PurchasePolicy purchasePolicy){
        this.purchasePolicy = purchasePolicy;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<User> RequestShopOfficialsInfo(SearchOfficialsFilter f, String userId) {
        if(shopManagersPermissionsController.canRequestInformationOnShopsOfficials(userId)| ShopOwners.containsKey(userId)) {
            return f.applyFilter(getUserList(),shopID);
        }
        return null;
    }
    public List<Order> RequestInformationOfShopsSalesHistory(SearchOrderFilter f, String userId) {
        if(shopManagersPermissionsController.canRequestInformationOfShopsSalesHistory(userId) | ShopOwners.containsKey(userId))
            return f.applyFilter(orders.getOrders());
        else return null;
    }

    private List<User> getUserList(){
        return Stream.concat(hashMapToList(ShopOwners).stream(),hashMapToList(ShopManagers).stream()).collect(Collectors.toList());
    }

    private List<User> hashMapToList(Map<String,User> hashMap) {
        return new LinkedList(hashMap.values());

    }

    private static List<ShopManagersPermissions> getAllPermissionsList()
    {
        ShopManagersPermissions[] SMP = ShopManagersPermissions.values();
        List<ShopManagersPermissions> list = new LinkedList<ShopManagersPermissions>();
        for (ShopManagersPermissions permission: SMP) {
            list.add(permission);
        }
        return list;
    }

    public List<ShopManagersPermissions> requestInfoOnManagerPermissions(String managerUsername) throws IllegalArgumentException {
        if(!ShopManagers.containsKey(managerUsername) && !ShopOwners.containsKey(managerUsername) && !ShopFounder.getUserName().equals(managerUsername))
            throw new IllegalArgumentException("username "+managerUsername+" is not authorize in the shop " + shopID);
        return shopManagersPermissionsController.getPermissions(managerUsername);
    }

    private void setMarketSystem(MarketSystem ms){
        marketSystem = ms;
    }

    public boolean canBeDismiss(String targetUser) {
        return isOwner(targetUser) | isFounder(targetUser) | ShopManagers.containsKey(targetUser);
    }

    public boolean DismissalOwner(String userName, String targetUser) throws InvalidSequenceOperationsExc {
        if(ShopOwners.containsKey(userName) & ShopOwners.containsKey(targetUser)){
            if (shopManagersPermissionsController.canDismissalOfStoreOwner(userName)) //TODO: need to check if OK.
                return true;
        }
        throw new InvalidSequenceOperationsExc();
    }

    public List<User> getShopOwners() {
        List<User> output = new LinkedList<>();
        ShopOwners.values().forEach((u)->output.add(u));
        output.add(ShopFounder);
        return output;
    }

}