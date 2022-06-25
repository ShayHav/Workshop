package domain.shop;

import domain.ControllersBridge;
import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.Responses.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchaseFormats.BidFormat;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Basket;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.shop.predicate.*;
import domain.user.*;
import domain.user.filter.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Shop {
    private String name;
    @Id
    private final int shopID;
    private int rank;
    private final User ShopFounder;
    private String description;
    private Map<String,User> ShopOwners;
    private Map<String,User> ShopManagers;
    private ShopManagersPermissionsController shopManagersPermissionsController;
    @Embedded
    private final Inventory inventory;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private final OrderHistory orders;
    private boolean isOpen;
    private BidHandler bidHandler;
    private MarketSystem marketSystem = MarketSystem.getInstance();

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
        bidHandler = new BidHandler();
    }

    public Shop(String name,String description, User shopFounder, int shopID) {
        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicy = new PurchasePolicy();
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
        if ( ShopOwners.containsKey(id) ||shopManagersPermissionsController.canChangeShopManagersPermissions(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot change permissions", id) );
            return false;
        }
    }
    public synchronized boolean addPermissions(ShopManagersPermissions shopManagersPermissionsList, String targetUser, String id) {
        if ( ShopOwners.containsKey(id) || shopManagersPermissionsController.canChangeShopManagersPermissions(id))
            return shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, targetUser);
        else {
            errorLogger.logMsg(Level.WARNING, String.format("user: %s cannot change permissions", id) );
            return false;
        }
    }

    public synchronized boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id) throws InvalidSequenceOperationsExc {
        if (ShopFounder.getUserName().equals(id) || ShopOwners.containsKey(id) || shopManagersPermissionsController.canChangeShopManagersPermissions(id)) {
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
        if(ShopOwners.containsKey(userId) || shopManagersPermissionsController.canAddProductToInventory(userId))
            return inventory.addProduct(serialNumber, productName, productDesc, productCategory, price, quantity);
        else
            throw new InvalidAuthorizationException("you do not have permission to list a product to this shop");
    }

    public synchronized void removeListing(int prodID,String userId) throws InvalidAuthorizationException, InvalidProductInfoException {
        if(ShopOwners.containsKey(userId) ||shopManagersPermissionsController.canRemoveProductFromInventory(userId) )
            inventory.removeProduct(prodID);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editPrice(int prodID, double newPrice,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if (ShopOwners.containsKey(userId) ||shopManagersPermissionsController.canChangeProductsDetail(userId) )
            inventory.setPrice(prodID, newPrice);
        else
            throw new InvalidAuthorizationException("you do not have permission to unlist a product from this shop");
    }

    public synchronized void editQuantity(int prodID, int newQuantity,String userId) throws InvalidProductInfoException, ProductNotFoundException, InvalidAuthorizationException {
        if(ShopOwners.containsKey(userId) ||shopManagersPermissionsController.canChangeProductsDetail(userId))
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
        basket.setBasePrice(getCartTotalWithNoDiscounts(productAmountList));
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
        if(ShopFounder.getUserName().equals(userId)|| ShopOwners.containsKey(userId)|| shopManagersPermissionsController.canChangeProductsDetail(userId)) {
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

        //calculate price

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
        Order o = createOrder(basket, transaction);
        sendCheckoutMessage(o);
        return new ResponseT<>(o);
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
        return ShopOwners.containsKey(id);
    }

    //TODO: check it works

    public void AppointNewShopOwner(String usertarget, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if ( ShopOwners.containsKey(userId) ||ShopFounder.getUserName().equals(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointOwner(shopID)){
                        managerUser.AppointedMeOwner(this,usertarget);
                        ShopOwners.putIfAbsent(usertarget, newManager);
                        newManager.addRole(shopID,Role.ShopOwner);
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
        if (ShopFounder.getUserName().equals(userId) || ShopOwners.containsKey(userId) ||shopManagersPermissionsController.canAppointNewShopOwner(userId)) {
            synchronized (this) {
                User newManager = ControllersBridge.getInstance().getUser(usertarget);
                User managerUser = ControllersBridge.getInstance().getUser(userId);
                if (newManager != null) {
                    if(managerUser.appointManager(shopID)){
                        managerUser.AppointedMeManager(this,usertarget);
                        ShopManagers.putIfAbsent(usertarget, newManager);
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
        if (ShopFounder.getUserName().equals(userID) || ShopOwners.containsKey(userID) || shopManagersPermissionsController.canCloseShop(userID)) {
            if (isOpen) {
                isOpen = false;
                User user = ControllersBridge.getInstance().getUser(userID);
                getShopOwners().forEach(owner -> marketSystem.sendMessage(owner, user, String.format("store %s was closed by %s", name, user.getUserName())));
            } else
                throw new InvalidSequenceOperationsExc(String.format("attempt to Close Closed Shop userID: %s", userID));
        }
       else throw new InvalidSequenceOperationsExc(String.format("attempt to Close Shop without right permission userID: %s", userID));
    }

    public synchronized void openShop(String userID) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if(ShopFounder.getUserName().equals(userID) || ShopOwners.containsKey(userID) || shopManagersPermissionsController.canOpenShop(userID)) {
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
        if(ShopOwners.containsKey(userId) || shopManagersPermissionsController.canRequestInformationOnShopsOfficials(userId)) {
            return f.applyFilter(getUserList(),shopID);
        }
        return null;
    }
    public List<Order> RequestInformationOfShopsSalesHistory(SearchOrderFilter f, String userId) {
        if(ShopOwners.containsKey(userId) ||shopManagersPermissionsController.canRequestInformationOfShopsSalesHistory(userId))
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
        return new LinkedList<>(Arrays.asList(SMP));
    }

    public List<ShopManagersPermissions> requestInfoOnManagerPermissions(String managerUsername) throws IllegalArgumentException {
        if(!ShopManagers.containsKey(managerUsername) && !ShopOwners.containsKey(managerUsername) && !ShopFounder.getUserName().equals(managerUsername))
            return new ArrayList<>();
        return shopManagersPermissionsController.getPermissions(managerUsername);
    }

    public void setMarketSystem(MarketSystem ms){
        marketSystem = ms;
    }

    public boolean canBeDismiss(String targetUser) {
        return !(isOwner(targetUser) | isFounder(targetUser) | ShopManagers.containsKey(targetUser));
    }

    public boolean DismissalOwner(String userName, String targetUser) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if(( ShopFounder.getUserName().equals(userName) ||ShopOwners.containsKey(userName)) & ShopOwners.containsKey(targetUser)){
            if (ShopFounder.getUserName().equals(userName)|| ShopOwners.containsKey(userName) ||shopManagersPermissionsController.canDismissalOfStoreOwner(userName)) {
                ShopOwners.remove(targetUser);
                ControllersBridge.getInstance().getUser(targetUser).removeRole(Role.ShopOwner,shopID);
                return true;
            }
        }
        throw new InvalidSequenceOperationsExc();
    }

    public List<User> getShopOwners() {
        List<User> output = new LinkedList<>(ShopOwners.values());
        output.add(ShopFounder);
        return output;
    }

    public List<User> getShopsManagers(){
        return new ArrayList<>(this.ShopManagers.values());
    }

    public User getShopFounder() {
        return ShopFounder;
    }

    public String getShopName() {
        return this.name;
    }

    private void sendCheckoutMessage(Order order){
        MarketSystem  market = marketSystem;
        String message = order.checkoutMessage();
        User buyer = null;
        try {
            buyer = market.getUser(order.getUserID());
        }catch (Exception e){}
        User finalBuyer = buyer;
        ShopOwners.values().forEach(owner -> market.sendMessage(owner, finalBuyer, message));
        market.sendMessage(ShopFounder, finalBuyer, message);
    }
    public boolean isProductAvailable(int prodID){
        return inventory.isInStock(prodID);
    }

    public boolean isManager(String userName) {
        return ShopManagers.containsKey(userName);
    }


    public int addSimpleProductDiscount(String userName, int prodID, double percentage) throws InvalidParamException, ProductNotFoundException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeProductsDiscountShopPolicy(userName)) {
            String productName = inventory.getName(prodID);
            return discountPolicy.addSimpleProductDiscount(prodID, productName, percentage);
        } else return -1;
    }

    public int addSimpleCategoryDiscount(String userName, String category, double percentage) throws InvalidParamException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.addSimpleCategoryDiscount(category, percentage);
        return -1;
    }

    public int addSimpleShopAllProductsDiscount(String userName, double percentage) throws InvalidParamException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.addSimpleShopAllProductsDiscount(percentage);
        return -1;
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



    public int addConditionalProductDiscount(String userName, int prodID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException, ProductNotFoundException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeProductsDiscountShopPolicy(userName)) {
            Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
            String productName = inventory.getName(prodID);
            return discountPolicy.addConditionalProductDiscount(prodID, percentage, pred, productName);
        }
        return -1;
    }

    public int addConditionalCategoryDiscount(String userName, String category, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName)) {
            Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
            return discountPolicy.addConditionalCategoryDiscount(category, percentage, pred);
        }
        return -1;
    }


    public int addConditionalShopAllProductsDiscount(String userName, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws CriticalInvariantException, InvalidParamException, AccessDeniedException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName)) {
            Predicate<Basket> pred = makePredDiscount(toBuildPredicatesFrom);
            return discountPolicy.addConditionalShopAllProductsDiscount(percentage, pred);
        }
        return -1;
    }

    public int addProductPurchasePolicy(String userName, int prodID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException, ProductNotFoundException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeProductsBuyingShopPolicy(userName)) {
            Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
            String productName = inventory.getName(prodID);
            return purchasePolicy.addProductPurchaseRule(prodID, pred, productName);
        }
        return -1;
    }

    public int addCategoryPurchasePolicy(String userName, String category, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeBuyingShopPolicy(userName)) {
            Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
            return purchasePolicy.addCategoryPurchaseRule(category, pred);
        }
        return -1;
    }


    public int addShopAllProductsPurchasePolicy(String userName, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, AccessDeniedException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeBuyingShopPolicy(userName)) {
            Predicate<Basket> pred = makePredPurchaseRule(toBuildPredicatesFrom);
            return purchasePolicy.addGeneralShopPurchaseRule(pred);
        }
        return -1;
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

    public int addOrDiscount(String userName, int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.addOrDiscount(dis1ID, dis2ID);
        return -1;
    }

    public int addAndDiscount(String userName, int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.addAndDiscount(dis1ID, dis2ID);
        return -1;
    }

    public int addXorDiscount(String userName, int dis1ID, int dis2ID) throws DiscountNotFoundException, CriticalInvariantException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.addXorDiscount(dis1ID, dis2ID);
        return -1;
    }

    public int addOrPurchaseRule(String userName, int pr1ID, int pr2ID) throws PurchaseRuleNotFoundException, CriticalInvariantException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeProductsBuyingShopPolicy(userName))
            return purchasePolicy.addOrPR(pr1ID, pr2ID);
        return -1;
    }

    public int addAndPurchaseRule(String userName, int pr1ID, int pr2ID) throws PurchaseRuleNotFoundException, CriticalInvariantException {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeProductsBuyingShopPolicy(userName))
            return purchasePolicy.addAndPR(pr1ID, pr2ID);
        return -1;
    }

    public boolean removeDiscount(String userName, int discountID) {
        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeDiscountShopPolicy(userName))
            return discountPolicy.removeDiscount(discountID);
        else return false;
    }
//    public boolean removePurchaseRule(String userName, int purchaseRuleID){
//        if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeBuyingShopPolicy(userName))
//            return purchasePolicy.removePurchaseRule(purchaseRuleID);
//        else return false;
//    }

    public void removePurchaseRule(String userName, int purchaseRuleID){ 
      if (ShopFounder.getUserName().equals(userName) || ShopOwners.containsKey(userName) || shopManagersPermissionsController.canChangeBuyingShopPolicy(userName))
        purchasePolicy.removePurchaseRule(purchaseRuleID); 
    }

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
}