package domain.shop;

import domain.ControllersBridge;
import domain.DAL.ControllerDAL;
import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.Responses.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchaseFormats.BidFormat;
import domain.shop.PurchaseFormats.BidHandler;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Basket;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.shop.predicate.*;
import domain.user.*;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;

import javax.persistence.*;
import java.util.*;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Shop {
    private String name;
    @Id
    private int shopID;
    private int rank;
    @Transient
    private final User ShopFounder;
    private String username;
    private String description;
    //private Map<String,User> ShopOwners;

    //private Map<String,User> ShopManagers;
    @ManyToMany
    private List<User> ShopOwners;
    @ManyToMany
    private List<User> ShopManagers;
    @Transient
    private ShopManagersPermissionsController shopManagersPermissionsController;
    @OneToOne
    private Inventory inventory;
    @Transient
    private DiscountPolicy discountPolicy;
    @Transient
    private PurchasePolicy purchasePolicy;
    @Transient
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    @Transient
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    @Transient
    private OrderHistory orders;
    private boolean isOpen;
    @Transient
    private MarketSystem marketSystem = MarketSystem.getInstance();
    @Transient
    private ControllerDAL controllerDAL = ControllerDAL.getInstance();
    @Transient
    private BidHandler bidHandler;
    @Transient
    private AppointHandler appointHandler;

    private int numOfVisits;

    public Shop() {

        ShopFounder = null;
        shopID = -1;
    }

    public int getNumOfVisits() {
        return numOfVisits;
    }

    public void setNumOfVisits(int numOfVisits) {
        this.numOfVisits = numOfVisits;
    }

    public OrderHistory getOH()
    {
        return this.orders;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setShopOwners(List<User> shopOwners) {
        ShopOwners = shopOwners;
    }

    public List<User> getShopManagers() {
        return ShopManagers;
    }

    public void setShopManagers(List<User> shopManagers) {
        ShopManagers = shopManagers;
    }

    public ShopManagersPermissionsController getShopManagersPermissionsController() {
        return shopManagersPermissionsController;
    }

    public void setShopManagersPermissionsController(ShopManagersPermissionsController shopManagersPermissionsController) {
        this.shopManagersPermissionsController = shopManagersPermissionsController;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setOrders(OrderHistory orders) {
        this.orders = orders;
    }

    public Shop merge(Shop shop)
    {
        setName(shop.getName());
        setRank(shop.getRank());
        setDescription(shop.getDescription());
        setShopManagers(shop.getShopManagers());
        setShopOwners(shop.getShopOwners());
        setShopManagersPermissionsController(shop.getShopManagersPermissionsController());
        setInventory(shop.getInventory());
        setDiscountPolicy(shop.getDiscountPolicy());
        setPurchasePolicy(shop.getPurchasePolicy());
        setOrders(shop.getOH());
        setOpen(shop.isOpen());
        return this;

    }
    private boolean isShopOwner(String username){
        for(User user: ShopOwners) {
            if(user.getUserName().equals(username))
                return true;
        }
        return false;
    }

    public boolean isShopManager(String username){
        for(User user: ShopManagers) {
            if(user.getUserName().equals(username))
                return true;
        }
        return false;
    }


    public Shop(String name,String description, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, User shopFounder, int shopID) {
        this.discountPolicy = discountPolicy;
        this.discountPolicy.setShopID(shopID);
        numOfVisits =0;
        controllerDAL.saveDiscountPolicy(discountPolicy);
        this.purchasePolicy = purchasePolicy;
        this.purchasePolicy.setShopID(shopID);
        controllerDAL.savePurchasePolicy(purchasePolicy);
        inventory = new Inventory();
        controllerDAL.saveInventory(inventory);
        orders = new OrderHistory();
        controllerDAL.saveOrderHistory(orders);
        ShopOwners = new LinkedList<>();
        ShopManagers = new LinkedList<>();
        rank = -1;
        this.name = name;
        isOpen = true;
        this.description = description;
        this.ShopFounder = shopFounder;
        shopManagersPermissionsController = new ShopManagersPermissionsController();
        shopManagersPermissionsController.setShopID(shopID);
        shopManagersPermissionsController.addPermissions(getAllPermissionsList(), shopFounder.getUserName());
        controllerDAL.saveShopManagersPermissionsController(shopManagersPermissionsController);
        this.shopID = shopID;
    }

    public Shop(String name,String description, User shopFounder, int shopID) {
        numOfVisits = 0;
        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicy = new PurchasePolicy();
        this.discountPolicy.setShopID(shopID);
        this.purchasePolicy.setShopID(shopID);
        inventory = new Inventory();
        orders = new OrderHistory();
        ShopOwners = new LinkedList<>();
        ShopManagers = new LinkedList<>();
        rank = -1;
        this.name = name;
        isOpen = true;
        this.description = description;
        this.ShopFounder = shopFounder;
        this.shopID = shopID;
        shopManagersPermissionsController = new ShopManagersPermissionsController();
        shopManagersPermissionsController.setShopID(shopID);
        shopManagersPermissionsController.addPermissions(getAllPermissionsList(), shopFounder.getUserName());
        bidHandler = new BidHandler();
        appointHandler = new AppointHandler();
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public synchronized boolean addPermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String id) {
        if ( isShopOwner(id) ||shopManagersPermissionsController.canChangeShopManagersPermissions(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot change permissions", id) );
            return false;
        }
    }
    public synchronized boolean addPermissions(ShopManagersPermissions shopManagersPermissionsList, String targetUser, String id) {
        if ( isShopOwner(id) || shopManagersPermissionsController.canChangeShopManagersPermissions(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot change permissions", id) );
            return false;
        }
    }

    public synchronized boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id) throws InvalidSequenceOperationsExc {
        if (isShopOwner(id) || shopManagersPermissionsController.canChangeShopManagersPermissions(id)) {
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

    public ProductImp getProduct(int prodID) throws ProductNotFoundException {
        ProductImp product = inventory.findProduct(prodID);
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
        if(isShopOwner(userId) || shopManagersPermissionsController.canAddProductToInventory(userId))
            return inventory.addProduct(serialNumber, productName, productDesc, productCategory, price, quantity);
        else
            throw new InvalidAuthorizationException("you do not have permission to list a product to this shop");
    }

    public synchronized void removeListing(int prodID,String userId) throws InvalidAuthorizationException {
        if(isShopOwner(userId) ||shopManagersPermissionsController.canRemoveProductFromInventory(userId) )
            inventory.removeProduct(prodID);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editPrice(int prodID, double newPrice,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if (isShopOwner(userId) ||shopManagersPermissionsController.canChangeProductsDetail(userId) )
            inventory.setPrice(prodID, newPrice);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editQuantity(int prodID, int newQuantity,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if(isShopOwner(userId) ||shopManagersPermissionsController.canChangeProductsDetail(userId))
            inventory.setAmount(prodID, newQuantity);
        else
            throw new InvalidAuthorizationException("you can edit quantity of a product in this shop");
    }

    public List<Discount> productDiscount(int prodID){
        return discountPolicy.getAllDiscountsForProd(prodID);
    }

    public double productPriceAfterDiscounts(int prodID, Map<Integer, Integer> productAmountList){
        double productBasePrice;
        double newPrice;
        try {
            productBasePrice = inventory.getPrice(prodID);
        }catch (ProductNotFoundException prodNotFound){
            return 0;
        }
        if(productBasePrice < 0)
            return 0;

        try {
            newPrice = cartItemsPricesAfterDiscounts(productAmountList, new ArrayList<>()).findProduct(prodID).getBasePrice();
        }catch (ProductNotFoundException productNotFoundException){
            return 0;
        }

        return newPrice;
    }


    public Basket IDsToProducts(Map<Integer, Integer> productAmountList){
        ProductImp product;
        Basket basket = new Basket();
        for(Map.Entry<Integer, Integer> set : productAmountList.entrySet()){
            //check purchase policy regarding the Product
            //Product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            try {
                product = inventory.findProduct(set.getKey());
            }catch (ProductNotFoundException productNotFoundException){
                continue;
            }
            basket.put(new ProductImp(product), set.getValue());
        }
        return basket;
    }

    public Double calculateTotalAmountOfOrder(Map<Integer, Integer> productAmountList){
        return cartItemsPricesAfterDiscounts(productAmountList).calculateTotal();
    }

    public Double calculateTotalAmountOfOrder(Map<Integer, Integer> productAmountList, List<Integer> acceptedbidIDs){
        return cartItemsPricesAfterDiscounts(productAmountList, acceptedbidIDs).calculateTotal();
    }

    public Basket cartItemsPricesAfterDiscounts(Map<Integer, Integer> productAmountList, List<Integer> acceptedbidIDs){
        Basket basket = IDsToProducts(productAmountList);
        Basket basketBids = idsToBids(acceptedbidIDs);
        basket.putAll(basketBids);
        basket.setBasePrice(basket.getBasePrice() + basketBids.getBasePrice());
        return discountPolicy.calcPricePerProductForCartTotal(basket);
    }

    public Basket cartItemsPricesAfterDiscounts(Map<Integer, Integer> productAmountList){
        Basket basket = IDsToProducts(productAmountList);
        return discountPolicy.calcPricePerProductForCartTotal(basket);
    }


    public double getCartTotalWithNoDiscounts(Map<Integer, Integer> productAmountList){
        double totalBasePrice = 0;
        double productBasePrice;
        int amount;
        for(Map.Entry<Integer, Integer> set : productAmountList.entrySet()){
            try {
                productBasePrice = inventory.getPrice(set.getKey());
                amount = productAmountList.get(set.getKey());
            }catch (ProductNotFoundException prodNotFound){
                continue;
            }
            if(productBasePrice < 0)
                continue;
            totalBasePrice += productBasePrice * amount;
        }

        return totalBasePrice;
    }





    public boolean isProductIsAvailable(int prodID, int quantity) throws ProductNotFoundException {
        return inventory.getQuantity(prodID) >= quantity;
    }

    public synchronized Product changeProductDetail(int prodID, String name, String description, String category,String userId, int amount, double price) throws InvalidProductInfoException, ProductNotFoundException {
        if(ShopFounder.getUserName().equals(userId)|| isShopOwner(userId)|| shopManagersPermissionsController.canChangeProductsDetail(userId)) {
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


    public boolean purchasePolicyLegal(Basket basket){
        if(purchasePolicy == null)
            return true;

        return purchasePolicy.checkCart_RulesAreMet(basket);
    }


    /**
     * check out from the store the given items to a client
     * @param products the items that the client want to buy and the amount from each
     * @param transaction the info of the client to be charged and supply
     * @return true if successfully created the order and add to the inventory
     */
    public ResponseT<Order> checkout(Map<Integer,Integer> products, List<Integer> acceptedBids, TransactionInfo transaction) throws BlankDataExc {
        Basket basket = IDsToProducts(products);
        Basket basketBids = idsToBids(acceptedBids);
        basket.putAll(basketBids);
        basket.setBasePrice(basket.getBasePrice() + basketBids.getBasePrice());

        if (!purchasePolicyLegal(basket)) {
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
                return new ResponseT<>(String.format("one of items selected is not in stock in shop %d",shopID));
            }
        }

        Map<Integer, Double> product_PricePer = new HashMap<>();
        basket = discountPolicy.calcPricePerProductForCartTotal(basket);

        for(ProductImp productImp : basket.keySet()){
            product_PricePer.put(productImp.getId(), productImp.getBasePrice());
        }
        try {
            int paymentTransactionID = marketSystem.pay(transaction);
            if (paymentTransactionID < 10000 || paymentTransactionID > 100000) {
                synchronized (inventory) {
                    inventory.restoreStock(products);
                }
                return new ResponseT<>(String.format("checkout in shop %d failed: problem with pay system, please try again later", shopID));
            }
            int supplyTransactionID = marketSystem.supply(transaction, products);
            if (supplyTransactionID < 10000 || supplyTransactionID > 100000) {
                marketSystem.cancelPayment(paymentTransactionID);
                return new ResponseT<>(String.format("checkout in shop %d failed: problem with supply system, please try again later", shopID));
            }
        }catch (Exception e){
            return new ResponseT<>(String.format("fail to checkout in shop %d, please try agian later", shopID));
        }
        // creating Order object to store in the Order History with unmutable copy of product
        Order o = createOrder(basket, transaction);
        sendCheckoutMessage(o);
        return new ResponseT<>(o);
    }
    public Basket idsToBids(List<Integer> bidIDs){
        ProductImp product;
        Basket basket = new Basket();
        double basketPrice = 0;
        for(Integer bidID : bidIDs){
            //check purchase policy regarding the Product
            //Product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            try {
                product = bidHandler.getBid(bidID);
            }catch (BidNotFoundException bidNotFoundException){
                continue;
            }
            basket.put(product, product.getAmount());
            basketPrice += product.getPrice();
        }
        basket.setBasePrice(basketPrice);
        return basket;
    }
    private Order createOrder(Basket products, TransactionInfo transaction) {
        List<Product> boughtProducts = new ArrayList<>();
        for(Map.Entry<ProductImp, Integer> product_amounts: products.entrySet()){
            ProductImp p = product_amounts.getKey();
            boughtProducts.add(new ProductHistory(p, p.getBasePrice(), product_amounts.getValue()));
        }
        Order o = new Order(boughtProducts, transaction.getTotalAmount(), transaction.getUserID(), shopID, name);
        orders.addOrder(o);
        return o;
    }

    public boolean isFounder(String id) {
        return ShopFounder.getUserName().equals(id);
    }

    public boolean isOwner(String id) {
        return isShopOwner(id);
    }

    //TODO: check it works

    public void AppointNewShopOwner(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if ( isShopOwner(userId) ||ShopFounder.getUserName().equals(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointOwner(shopID)){
                        managerUser.AppointedMeOwner(this,usertarget);
                        ShopOwners.add(newManager);
                        newManager.addRole(shopID, Role.ShopOwner);
                        eventLogger.logMsg(Level.INFO, String.format("Appoint New Shop Owner User: %s", usertarget));
                        return;
                    }
                }
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
        throw new InvalidSequenceOperationsExc(String.format("attempt to appoint New ShopManager User: %s filed", usertarget));
    }

    public int getShopID() {
        return shopID;
    }

    public void AppointNewShopManager(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if (ShopFounder.getUserName().equals(userId) || isShopOwner(userId) ||shopManagersPermissionsController.canAppointNewShopOwner(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointManager(shopID)){
                        managerUser.AppointedMeManager(this,usertarget);
                        ShopManagers.add(newManager);
                        newManager.addRole(shopID,Role.ShopManager);
                        shopManagersPermissionsController.initManager(newManager.getUserName());
                        eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", usertarget));
                        return;
                    }
                }
            }
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to appoint New ShopManager User: %s failed", usertarget));
        throw new InvalidSequenceOperationsExc(String.format("attempt to appoint New ShopManager User: %s failed", usertarget));
    }


    public synchronized void closeShop(String userID) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if (ShopFounder.getUserName().equals(userID) || shopManagersPermissionsController.canCloseShop(userID)) {
            if (isOpen) {
                isOpen = false;
                User user = ControllersBridge.getInstance().getUser(userID);
                getShopOwners().forEach(owner -> {
                    marketSystem.sendMessage(owner, user, String.format("store %s was closed by %s", name, user.getUserName()));
                });
            } else
                throw new InvalidSequenceOperationsExc(String.format("attempt to Close Closed Shop userID: %s", userID));
        }
       else throw new InvalidSequenceOperationsExc(String.format("attempt to Close Shop without right permission userID: %s", userID));
    }

    public synchronized void openShop(String userID) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if(ShopFounder.getUserName().equals(userID) || isShopOwner(userID) || shopManagersPermissionsController.canOpenShop(userID)) {
            if (!isOpen) {
                isOpen = true;
                User opener = ControllersBridge.getInstance().getUser(userID);
                getShopOwners().forEach(owner -> {
                    String message = String.format("User %s reopened shop %s", opener.getUserName(),name);
                    marketSystem.sendMessage(owner, opener, message);
                });

            }
            else throw new InvalidSequenceOperationsExc(String.format("attempt to Open Opened Shop userID: %s",userID));
        }
        else throw new InvalidSequenceOperationsExc(String.format("attempt to Open Shop without permission userID: %s",userID));
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
        return inventory.getAllProductInfo();
    }

    public Product getInfoOnProduct(int productId) throws ProductNotFoundException {
        Product p;
        synchronized (inventory){
            p = inventory.getProductInfo(productId);
        }
        p.setShopRank(rank);
        return p;
    }

    public Product getInfoOnBid(int bidId) throws BidNotFoundException {
        Product p;
        synchronized (inventory){
            p = bidHandler.getBid(bidId);
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
        if(isShopOwner(userId) || shopManagersPermissionsController.canRequestInformationOnShopsOfficials(userId)) {
            return f.applyFilter(getUserList(),shopID);
        }
        return null;
    }
    public List<Order> RequestInformationOfShopsSalesHistory(SearchOrderFilter f, String userId) {
        if(isShopOwner(userId) ||shopManagersPermissionsController.canRequestInformationOfShopsSalesHistory(userId))
            return f.applyFilter(orders.getOrders());
        else return null;
    }

    private List<User> getUserList(){
        return Stream.concat(ShopOwners.stream(),ShopManagers.stream()).collect(Collectors.toList());
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
        if(!isShopManager(managerUsername) && !isShopOwner(managerUsername) && !ShopFounder.getUserName().equals(managerUsername))
            return new ArrayList<>();
        return shopManagersPermissionsController.getPermissions(managerUsername);
    }

    private void setMarketSystem(MarketSystem ms){
        marketSystem = ms;
    }

    public boolean canBeDismiss(String targetUser) {
        return isOwner(targetUser) | isFounder(targetUser) | isShopManager(targetUser);
    }

    public boolean DismissalOwner(String userName, String targetUser) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if(( ShopFounder.getUserName().equals(userName) ||isShopOwner(userName)) & isShopOwner(targetUser)){
            if (ShopFounder.getUserName().equals(userName)|| isShopOwner(userName) ||shopManagersPermissionsController.canDismissalOfStoreOwner(userName)) {
                ShopOwners.remove(targetUser);
                ControllersBridge.getInstance().getUser(targetUser).removeRole(Role.ShopOwner,shopID);
                return true;
            }
        }
        throw new InvalidSequenceOperationsExc();
    }

    public List<User> getShopOwners() {
        List<User> output = new LinkedList<>();
        ShopOwners.forEach((u)->output.add(u));
        output.add(ShopFounder);
        return output;
    }

    public List<User> getShopsManagers(){
        return this.ShopManagers;
    }

    public User getShopFounder() {
        return ShopFounder;
    }

    public String getShopName() {
        return this.name;
    }

    private void sendCheckoutMessage(Order order){
        MarketSystem  market = MarketSystem.getInstance();
        String message = order.checkoutMessage();
        User buyer = null;
        try {
            buyer = market.getUser(order.getUserID());
        }catch (Exception e){}
        User finalBuyer = buyer;
        ShopOwners.forEach(owner -> {
            market.sendMessage(owner, finalBuyer,message );
        });
        market.sendMessage(ShopFounder, finalBuyer, message);
    }
    public boolean isProductAvailable(int prodID){
        return inventory.isInStock(prodID);
    }

    public boolean isManager(String userName) {
        return isShopManager(userName);
    }


    public int addSimpleProductDiscount(int prodID, double percentage) throws InvalidParamException, ProductNotFoundException {
        String productName = inventory.getName(prodID);
        return discountPolicy.addSimpleProductDiscount(prodID, productName, percentage);
    }

    public int addSimpleCategoryDiscount(String category, double percentage) throws InvalidParamException {
        return discountPolicy.addSimpleCategoryDiscount(category, percentage);
    }

    public int addSimpleShopAllProductsDiscount(double percentage) throws InvalidParamException {
        return discountPolicy.addSimpleShopAllProductsDiscount(percentage);
    }

    public Predicate<Basket> makePredDiscount(ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {

        if(toBuildPredicatesFrom.getPredType().equals(DiscountPredType.price))
            return makePriceEligiblePred(toBuildPredicatesFrom.getPrice());
        else if(toBuildPredicatesFrom.getPredType().equals(DiscountPredType.product))
            return makeProductEligiblePred(toBuildPredicatesFrom.getProductID(), toBuildPredicatesFrom.getProductName(), toBuildPredicatesFrom.getAmount());
        else
            throw new CriticalInvariantException("should never happen.");
    }


    public Predicate<Basket> makePredPurchaseRule(ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {

        if(toBuildPredicatesFrom.getPredType().equals(PRPredType.TimeConstraint))
            return makeTimePred(toBuildPredicatesFrom.getHoursFrom(), toBuildPredicatesFrom.getHoursTo());
        else if(toBuildPredicatesFrom.getPredType().equals(PRPredType.MaximumAmount))
            return makeMaximumPred(toBuildPredicatesFrom.getProdID(), toBuildPredicatesFrom.getProductName(),toBuildPredicatesFrom.getAmount());
        else if(toBuildPredicatesFrom.getPredType().equals(PRPredType.MinimumAmount))
            return makeMinimumPred(toBuildPredicatesFrom.getProdID(), toBuildPredicatesFrom.getProductName(), toBuildPredicatesFrom.getAmount());
        else
            throw new CriticalInvariantException("should never happen.");
    }


    public Predicate<Basket> makeTimePred(double from, double to) {
        return PredicateManager.createTimePredicate(from, to);
    }

    public Predicate<Basket> makeMinimumPred(int productID, String productName,int amount){
        return PredicateManager.createMinimumProductsPredicate(productID, productName,amount);
    }


    public Predicate<Basket> makeMaximumPred(int productID, String productName, int amount){
        return PredicateManager.createMaximumProductsPredicate(productID, productName, amount);
    }



    public int addConditionalProductDiscount(int prodID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException, ProductNotFoundException {
        Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
        String productName = inventory.getName(prodID);
        return discountPolicy.addConditionalProductDiscount(prodID, percentage, pred, productName);
    }

    public int addConditionalCategoryDiscount(String category, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException {
        Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
        return discountPolicy.addConditionalCategoryDiscount(category, percentage, pred);
    }


    public int addConditionalShopAllProductsDiscount(double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException {
        Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
        return discountPolicy.addConditionalShopAllProductsDiscount(percentage, pred);
    }

    public int addProductPurchasePolicy(int prodID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException, ProductNotFoundException {
        Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
        String productName = inventory.getName(prodID);
        return purchasePolicy.addProductPurchaseRule(prodID, pred, productName);
    }

    public int addCategoryPurchasePolicy(String category, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {
        Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
        return purchasePolicy.addCategoryPurchaseRule(category, pred);
    }


    public int addShopAllProductsPurchasePolicy(ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {
        Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
        return purchasePolicy.addGeneralShopPurchaseRule(pred);
    }


    /*public Predicate<Tuple<Map<Integer, Integer>, Double>> makePred(List<Integer> logicalGatesList, List<Integer> predTypeOpCode, List<Tuple<Tuple<Integer, Integer>, Double>> toBuildPredicatesFrom) throws Exception {
        checkPredMakerParameters(logicalGatesList, predTypeOpCode, toBuildPredicatesFrom);
        List<Predicate<Tuple<Map<Integer, Integer>, Double>>> predicateList = new ArrayList<>();
        Iterator<Tuple<Tuple<Integer, Integer>, Double>> toBuildFromIter = toBuildPredicatesFrom.iterator();
        Iterator<Integer> predTypesIter = predTypeOpCode.iterator();
        Iterator<Integer> logicalGatesIter = logicalGatesList.iterator();

        while(predTypesIter.hasNext()){
            int predType = predTypesIter.next();
            switch (predType) {
                case 1 -> predicateList.add(PredicateManager.createPricePredicate(toBuildFromIter.next().y));
                case 2 -> {
                    Tuple<Integer, Integer> toBuildFrom = toBuildFromIter.next().x;
                    predicateList.add(PredicateManager.createProductsPredicate(toBuildFrom.x, toBuildFrom.y));
                }
            }
        }

        Iterator<Predicate<Tuple<Map<Integer, Integer>, Double>>> processedPredicateList = predicateList.iterator();
        int gate;
        Predicate<Tuple<Map<Integer, Integer>, Double>> predToReturn = processedPredicateList.next();
        Predicate<Tuple<Map<Integer, Integer>, Double>> currPredicate;
        while(logicalGatesIter.hasNext()){
            if(!processedPredicateList.hasNext()){
                throw new Exception("should never happen.");
            }
            gate = logicalGatesIter.next();
            currPredicate = processedPredicateList.next();
            switch (gate) {
                case 1 -> predToReturn = PredicateManager.orPredicate(predToReturn, currPredicate);
                case 2 -> predToReturn = PredicateManager.andPredicate(predToReturn, currPredicate);
                case 3 -> predToReturn = PredicateManager.xorPredicate(predToReturn, currPredicate);
            }
        }

        return predToReturn;

    }*/

    public Predicate<Basket> makePriceEligiblePred(double percentage) {
        return PredicateManager.createPricePredicate(percentage);
    }

    public Predicate<Basket> makeProductEligiblePred(int productID, String productName,int amount){
        return PredicateManager.createMinimumProductsPredicate(productID, productName, amount);
    }

    public int addOrDiscount(int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        return discountPolicy.addOrDiscount(dis1ID, dis2ID);
    }

    public int addAndDiscount(int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        return discountPolicy.addAndDiscount(dis1ID, dis2ID);
    }

    public int addXorDiscount(int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        return discountPolicy.addXorDiscount(dis1ID, dis2ID);
    }

    public int addOrPurchaseRule(int pr1ID, int pr2ID) throws PurchaseRuleNotFoundException, CriticalInvariantException {
        return purchasePolicy.addOrPR(pr1ID, pr2ID);
    }

    public int addAndPurchaseRule(int pr1ID, int pr2ID) throws PurchaseRuleNotFoundException, CriticalInvariantException {
        return purchasePolicy.addAndPR(pr1ID, pr2ID);
    }

    public boolean removeDiscount(String userName, int discountID) {
        if (ShopFounder.getUserName().equals(userName) || isShopOwner(userName)  || (isShopManager(userName) && shopManagersPermissionsController.canChangeDiscountShopPolicy(userName)))
            return discountPolicy.removeDiscount(discountID);
        else return false;
    }
//    public boolean removePurchaseRule(String userName, int purchaseRuleID){
//        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeBuyingShopPolicy(userName))
//            return purchasePolicy.removePurchaseRule(purchaseRuleID);
//        else return false;
//    }

    public boolean removePurchaseRule(int purchaseRuleID){ return purchasePolicy.removePurchaseRule(purchaseRuleID); }

    public PurchasePolicy getPurchasePolicy() {
        return purchasePolicy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
    public void RemoveShopOwnerTest(String useID) {
        ShopOwners.remove(useID);
    }

    public void RemoveShopManagerTest(String useID) {
        ShopManagers.remove(useID);
    }

    public int addNewBid(ProductImp product, User buyer) throws ProductNotFoundException {
        return bidHandler.addNewBid(product, getShopOwners(), buyer, inventory.getPrice(product.getId()), this);
    }

    public double calculateBid(int bidID){
        BidFormat bid = null;
        try {
            bid = bidHandler.getBid(bidID);
        } catch (BidNotFoundException e) {
            return 0;
        }
        Basket tempBasket = new Basket();
        tempBasket.put(bid, bid.getAmount());
        tempBasket = discountPolicy.calcPricePerProductForCartTotal(tempBasket);
        return tempBasket.calculateTotal();
    }

    public boolean removeBid(int bidID){
        return bidHandler.removeBid(bidID);
    }

    public void acceptBid(int bidID, User approver) throws BidNotFoundException, CriticalInvariantException {
        bidHandler.acceptBid(bidID, approver);
    }

    public void declineBid(int bidID, User decliner) throws BidNotFoundException, CriticalInvariantException {
        bidHandler.declineBid(bidID, decliner);
    }

    public void acceptAppoint(int appointmentNumber, User approver) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        appointHandler.acceptAppoint(appointmentNumber, approver);
    }

    public void declineAppoint(int appointmentNumber, User decliner) throws BidNotFoundException, CriticalInvariantException {
        appointHandler.declineAppoint(appointmentNumber, decliner);
        appointHandler.removeAppointment(appointmentNumber);
    }

    public void addUserAsOwner(User user){
        if(!isShopOwner(user.getUserName()))
            ShopOwners.add(user);
        try {
            user.addRole(shopID,Role.ShopOwner);
        } catch (InvalidSequenceOperationsExc e) {

        }
        eventLogger.logMsg(Level.INFO, String.format("Appoint New Shop Owner User: %s", user.getUserName()));
    }

//    public void putIfAbsentManager(String userName, User appointUser) {
//        ShopManagers.putIfAbsent(userName, appointUser);
//    }
}