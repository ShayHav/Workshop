package domain.shop;

import domain.ControllersBridge;
import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;

import java.util.*;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shop {
    private String name;
    private final int shopID;
    private int rank;
    private User ShopFounder;
    private Map<String,User> ShopOwners;
    private Map<String,User> ShopManagers;
    private ShopManagersPermissionsController shopManagersPermissionsController;
    private Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final OrderHistory orders;
    private boolean isOpen;

    public Shop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String founderId, int shopID) throws IncorrectIdentification, BlankDataExc {
        this.discountPolicy = discountPolicy;
        this.purchasePolicy = purchasePolicy;
        inventory = new Inventory();
        orders = new OrderHistory();
        ShopOwners = new HashMap<>();
        ShopManagers = new HashMap<>();
        rank = -1;
        this.name = name;
        isOpen = true;
        ShopFounder = ControllersBridge.getInstance().getUser(founderId);
        this.shopID = shopID;
        shopManagersPermissionsController = new ShopManagersPermissionsController();
    }

    public synchronized boolean addPermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String id) {
        if (shopManagersPermissionsController.canChangeShopManagersPermissions(id)| ShopOwners.containsKey(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
            return false;
        }
    }

    public synchronized boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id) {
        if (shopManagersPermissionsController.canChangeShopManagersPermissions(id)| ShopOwners.containsKey(id)) {
            return shopManagersPermissionsController.removePermissions(shopManagersPermissionsList, tragetUser);
        } else {
            errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
            return false;
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

    public synchronized Product addListing(String productName, String productDesc, String productCategory, double price, int quantity,String userId) throws InvalidAuthorizationException, InvalidProductInfoException {
        if(shopManagersPermissionsController.canAddProductToInventory(userId)| ShopOwners.containsKey(userId))
            return inventory.addProduct(productName, productDesc, productCategory, price, quantity);
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
        double productBasePrice = inventory.getPrice(prodID);
        if(productBasePrice < 0)
            return 0;
        return discountPolicy.calcPricePerProduct(prodID, productBasePrice, amount);
    }

    public boolean isProductIsAvailable(int prodID){
        return inventory.isInStock(prodID);
    }

    public synchronized Product changeProductDetail(int prodID, String name, String description, String category,String userId, int amount, double price) throws InvalidProductInfoException, ProductNotFoundException {
        if(shopManagersPermissionsController.canChangeProductsDetail(userId)| ShopOwners.containsKey(userId)) {
            inventory.setAmount(prodID, amount);
            inventory.setPrice(prodID, price);
            return inventory.setProduct(prodID, name, description, category);
        }
        return null;
    }
    public boolean isProductIsAvailable(int prodID,int amount){
        return inventory.getQuantity(prodID)>=amount;
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
    public ResponseT<Order> checkout(Map<Integer,Integer> products, double totalAmount, TransactionInfo transaction){
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            //check purchase policy regarding the Product
            if (!purchasePolicyLegal(transaction.getUserID(), set.getKey(), inventory.getPrice(set.getKey()), set.getValue()))
                return new ResponseT("violates purchase policy");
        }
        synchronized (inventory) {
            if (!inventory.reserveItems(products)) {
                try{
                    inventory.restoreStock(products);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
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
            synchronized (inventory) {
                try {
                    inventory.restoreStock(products);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return new ResponseT<>();
        }
        if(!market.supply(transaction, products)){
            return new ResponseT<>("problem with supply system, please contact the company representative");
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
        return ShopOwners.containsKey(id);
    }

    public String AppointNewShopOwner(String targetUser, String userId) throws IncorrectIdentification, BlankDataExc {
        if (shopManagersPermissionsController.canAppointNewShopOwner(userId) | ShopOwners.containsKey(userId) | isFounder(userId)) {
            synchronized (this) {
                User newOwner = ControllersBridge.getInstance().getUser(targetUser);
                if (newOwner != null)
                    if (ShopOwners.get(targetUser) == null)
                        ShopOwners.put(targetUser, newOwner);
                eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopOwner User: %s", targetUser));
                return String.format("Appoint New ShopOwner User: %s", targetUser);
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("Appoint New ShopOwner User: %s", targetUser));
        return String.format("attempt to appoint New ShopOwner User: %s filed", targetUser);
    }

    public int getShopID() {
        return shopID;
    }

    public String AppointNewShopManager(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc {
        if (shopManagersPermissionsController.canAppointNewShopManager(userId)| ShopOwners.containsKey(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                if (newManager != null)
                    if (ShopManagers.get(usertarget)==null)
                        ShopManagers.put(usertarget,newManager);
                eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", usertarget));
                return String.format("Appoint New ShopManager User: %s", usertarget);
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
        return String.format("attempt to appoint New ShopManager User: %s filed", usertarget);
    }

    public synchronized void closeShop(String userID){
        if(shopManagersPermissionsController.canCloseShop(userID)) {
            if (isOpen)
                isOpen = false;
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

    public List<ProductInfo> getProductInfoOfShop() {
        List<ProductInfo> info = inventory.getAllProductInfo();
        for (ProductInfo p : info) {
            p.setShopName(name);
            p.setShopRank(rank);
        }
        return info;
    }

    public ProductInfo getInfoOnProduct(int productId) throws ProductNotFoundException {
        ProductInfo p;
        synchronized (inventory){
            p = inventory.getProductInfo(productId);
        }
        p.setShopName(name);
        p.setShopRank(rank);
        return p;

    }

    public List<Order> getOrders() {
        return orders.getOrders();
    }


    public void setInventory(Inventory inventory){
        this.inventory = inventory;
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

    public List<UserSearchInfo> RequestShopOfficialsInfo(SearchOfficialsFilter f,String userId) {
        if(shopManagersPermissionsController.canRequestInformationOnShopsOfficials(userId)| ShopOwners.containsKey(userId)) {
            return f.applyFilter(getUserList(),shopID);
        }
        return null;
    }
    public List<Order> RequestInformationOfShopsSalesHistory(SearchOrderFilter f,String userId) {
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
}
