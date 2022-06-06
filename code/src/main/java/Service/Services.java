package Service;

import domain.Exceptions.*;
import domain.Response;
import domain.ResponseList;
import domain.ResponseMap;
import domain.ResponseT;
import domain.market.*;
import domain.notifications.AdminObserver;
import domain.notifications.UserObserver;
import domain.shop.*;
import domain.user.*;
import domain.user.filter.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Services {
    private final MarketSystem marketSystem;

    private Services() {
        marketSystem = MarketSystem.getInstance();
    }

    private static class ServicesHolder {
        private static final Services instance = new Services();
    }

    public ResponseList<ShopManagersPermissions> CheckPermissionsForManager(String managerUsername, int shopID) {
        try {
            List<ShopManagersPermissions> permissions = marketSystem.checkPermissionsForManager(managerUsername, shopID);
            return new ResponseList<>(permissions);
        } catch (ShopNotFoundException | IllegalArgumentException e) {
            return new ResponseList<>(e.getMessage());
        }
    }

    public static Services getInstance() {
        return ServicesHolder.instance;
    }


    /***
     * get user in market
     * @param username user identifier
     * @return response with the user if succeed or error message if not
     */
    public ResponseT<User> GetUser(String username) {
        try {
            User u = marketSystem.getUser(username);
            return new ResponseT<>(u);
        } catch (BlankDataExc | IncorrectIdentification e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * return all the shops in which the user is the founder
     * @param username username of the user who requesting his shops
     * @return response with a list of the user's shop if succeed or error message if not
     */
    public ResponseList<Shop> GetAllUserShops(String username, Filter<Shop> filter){
        try{
            List<Shop> shops = marketSystem.GetAllUserShops(username, filter);
            return new ResponseList<>(shops);
        }catch (BlankDataExc | IncorrectIdentification | InvalidAuthorizationException e){
            return new ResponseList<>(e.getMessage());
        }
    }


    /***
     * get user in market
     * @param shopID shops identifier
     * @return
     */
    public ResponseT<Shop> GetShop(int shopID) {
        try {
            Shop s = marketSystem.getShop(shopID);
            return new ResponseT<>(s);
        } catch (ShopNotFoundException e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    //General Guest-Visitor
    //Make:nitay InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification

    /**
     * User logged in to the system
     * @param guestUsername - user current identity
     * @param username - presented username credential for login
     * @param pw - given password
     * @return - Response object
     */
    public ResponseT<User> Login(String guestUsername, String username, String pw, UserObserver o) {
        ResponseT<User> output;
        try {
            User b = marketSystem.login(guestUsername,username, pw, null);
            output = new ResponseT<>(b);
            return output;
        } catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification blankDataExc) {
            return new ResponseT<>(blankDataExc.getMessage());
        } catch (InvalidAuthorizationException e) {
            return new ResponseT<>(e.actualAuthorization);
        }
    }

    //Make:nitay

    /**
     * User registered to the market
     * @param guestUsername - given user identifier
     * @param registerUsername - desired username for register
     * @param pw - given user password
     * @return
     */
    public Response Register(String guestUsername, String registerUsername, String pw) {
        try {
            marketSystem.register(guestUsername, registerUsername, pw);
            return new Response();
        } catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }

    }

    /**
     * User enters the system
     * @return response object
     */
    public ResponseT<User> EnterMarket() { //
        User guest = marketSystem.EnterMarket();
        return new ResponseT<>(guest);
    }

    //Make:nitay   IncorrectIdentification, InvalidSequenceOperationsExc
    //TODO: need to check

    /**
     * User closes the system
     * @return - null
     */
    public Response LeaveMarket(String username) {
        try {
            User output = marketSystem.LeaveMarket(username);
            return new Response();
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | BlankDataExc | InvalidAuthorizationException e) {
            return new Response(e.getLocalizedMessage());
        }
    }


    //General Member-Visitor

    /**
     * User logs off
     * @param username - user identifier
     * @return Response object
     */
    public ResponseT<User> Logout(String username) {
        try {
            User guest = marketSystem.logout(username);
            return new ResponseT<>(guest);
        } catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification e) {
            return new ResponseT<>(e.getLocalizedMessage());
        }
    }
    //Make:nitay

    /**
     * Create a store object to represent the relevant store in the app
     * @param description -  description of the created shop
     * @param username - user identifier
     * @param shopName - shop name
     * @return Response with the shop is created successfully or error message if not
     */
    public ResponseT<Shop> CreateShop(String description ,String username, String shopName) {
        try {
            Shop output = marketSystem.createShop(description, shopName, null, null, username);
            ;
            return new ResponseT<>(output);
        } catch (BlankDataExc | IncorrectIdentification | InvalidSequenceOperationsExc e) {
            return new ResponseT<>(e.getLocalizedMessage());
        }
    }

    //TODO: impl on later version
    //System
    public Response RealTimeNotification(List<String> users, String msg) {
        return null;
    }

    //shay  //TODO: only for test?
    public ResponseT<Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        try {
            MarketSystem m = MarketSystem.getInstance();
            boolean ans = m.supply(ti, products);
            return new ResponseT<>(ans);
        } catch (BlankDataExc blankDataExc) {
            return null;
        }
    }

    //shay  //TODO: only for test?
    public ResponseT<Boolean> Payment(TransactionInfo ti) {
        //MarketSystem m = MarketSystem.getInstance();
        boolean ans = marketSystem.pay(ti);
        return new ResponseT<>(ans);
    }

    /**
     * Boot of a user who started the system
     * @param payment - External device for payment
     * @param supply - External device for supply
     * @param userName - user identifier
     * @param password - user's given password
     * @return Response object
     */
    public Response StartMarket(PaymentService payment, SupplyService supply, String userName, String password) {
        try {
            marketSystem.start(payment, supply, userName, password);
            return new Response();
        } catch (InvalidSequenceOperationsExc | IncorrectIdentification invalidSequenceOperationsExc) {
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }

//    public Result<Boolean, String> AddSupplyService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, Boolean> RemoveSupplyService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, String> AddPaymentService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, String> RemovePaymentService(String path)
//    {
//        return null;
//    }

    //make: shahar
    //Guest-Visitor Shop options
    /**
     * Query for certain shops
     * @param userName - user identifier
     * @param filter - Identifying parameters
     * @return list of Response object
     */
    public ResponseList<Shop> GetShopsInfo(String userName, Filter<Shop> filter) {
        try {
            List<Shop> shops = Collections.unmodifiableList(marketSystem.getInfoOfShops(userName, filter));
            return new ResponseList<>(shops);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseList<>(blankDataExc.getMessage());
        }
    }

    /***
     * Query for certain products in the relevant store.
     * @param userName - user identifier
     * @param shopID - shop identifier
     * @param f filter object to filter the result of the search querying by
     * @return list of Response object
     */
    public ResponseList<Product> GetProductInfoInShop(String userName, int shopID, Filter<Product> f) {
        try {
            List<Product> products = marketSystem.getInfoOfProductInShop(userName, shopID, f);
            return new ResponseList<>(products);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseList<>(blankDataExc.getMessage());
        }
    }//display information of a product?


    /**
     * Query for certain products by name in the relevant store
     * @param userName - user identifier
     * @param pName - shop's name
     * @param f filter object to filter the product search results by
     * @return list of Response object
     */
    public ResponseMap<Integer,List<Product>> SearchProductByName(String userName, String pName, Filter<Product> f) //done
    {
        try {
            Map<Integer,List<Product>> products = Collections.unmodifiableMap(marketSystem.searchProductByName(userName, pName, f));
            return new ResponseMap<>(products);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseMap<>(blankDataExc.getMessage());
        }
    }

    /**
     * Query for certain products by keyword in the relevant store
     * @param userName
     * @param keyword
     * @param f
     * @return list Response object
     */
    public ResponseMap<Integer,List<Product>> SearchProductByKeyword(String userName, String keyword, Filter<Product> f) {
        try {
            Map<Integer,List<Product>> products = Collections.unmodifiableMap(marketSystem.searchProductByKeyword(userName, keyword, f));
            return new ResponseMap<>(products);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseMap<>(blankDataExc.getMessage());
        }
    }

    /**
     * Search product in market (without specify specific shop) by desired category
     * @param userName user requesting
     * @param category category desired
     * @param f other filters
     * @return
     */
    public ResponseMap<Integer,List<Product>> SearchProductByCategory(String userName, String category, Filter<Product> f) {
        try {
            Map<Integer,List<Product>> products = Collections.unmodifiableMap(marketSystem.searchProductByCategory(userName, category, f));
            return new ResponseMap<>(products);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseMap<>(blankDataExc.getMessage());
        }
    }

    /***
     * show user's cart content
     * @param username user identifier
     * @return
     */
    public ResponseT<Cart.ServiceCart> ShowCart(String username){
        try{
            Cart.ServiceCart c = marketSystem.showCart(username);
            return new ResponseT<>(c);
        }catch (BlankDataExc e){
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Add a product to the user's shopping cart (member or guest)
     * @param userName - user identifier
     * @param shopID - shop identifier
     * @param productId - product identifier
     * @param amount
     * @return Response object
     */
    public Response AddToShoppingCart(String userName, int shopID, int productId, int amount) {
        try {
            return marketSystem.AddProductToCart(userName, shopID, productId, amount);
        } catch (InvalidSequenceOperationsExc | ShopNotFoundException | BlankDataExc | IncorrectIdentification | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * update amount of a certain product quantity in the user's shopping cart
     * @param userName - user identifier
     * @param shopId - shop identifier
     * @param productId - product identifier
     * @param amount
     * @return Response object
     */
    public Response EditShoppingCart(String userName, int shopId, int productId, int amount) {
        try {
            return marketSystem.EditShoppingCart(userName, shopId, productId, amount);
        } catch (InvalidSequenceOperationsExc | IncorrectIdentification | InvalidAuthorizationException | BlankDataExc e) {
            return new Response(e.getMessage());
        }
    }

    //make:shahar

    /**
     * remove a certain product quantity in the user's shopping cart
     * @param userName
     * @param shopId
     * @param productId
     * @return
     */
    public Response RemoveFromShoppingCart(String userName, int shopId, int productId) {
        try {
            return marketSystem.removeProductFromCart(userName, shopId, productId);
        } catch (InvalidSequenceOperationsExc | BlankDataExc | IncorrectIdentification | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Checkout
     * @param userName - user identifier
     * @param fullName - user full name
     * @param address - user address
     * @param phoneNumber - user phone number
     * @param cardNumber - user credit card number
     * @param expirationDate - user credit card expiration date
     * @return list of Response object
     */
    public ResponseList<String> Checkout(String userName, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        try {
            List<String> list = marketSystem.Checkout(userName, fullName, address, phoneNumber, cardNumber, expirationDate);
            return new ResponseList<>(list);
        } catch (IncorrectIdentification | BlankDataExc e) {
            return new ResponseList<>(e.getMessage());
        }
    }


    /**
     * Check If Product Available in the shop
     * @param p - relevant product object
     * @param shopID - shop identifier
     * @return Response object
     */
    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID) {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try {
            shop = controller.getShop(shopID);
            return new ResponseT<>(shop.getProduct(p.getId()) != null);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        } catch (ProductNotFoundException e) {
            return new ResponseT<>(e.getMessage());
        }


    }

    /**
     * Add a product to the shop inventory
     * @param pName - product name
     * @param pDis - product description
     * @param pCat - product category
     * @param price - product price
     * @param amount - amount of added product
     * @param username - active user identifier
     * @param shopID - shop identifier
     * @return Response object
     */
    public ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try {
            shop = controller.getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        }
        Product p;
        try {
            p = shop.addListing(serialNumber, pName, pDis, pCat, price, amount, username);
        } catch (InvalidAuthorizationException | InvalidProductInfoException iae) {
            return new ResponseT<>(null, iae.getLocalizedMessage());
        }
        if (p == null) {
            return null;
        }
        return new ResponseT<>(p);
    }

    /**
     *edit product properties
     * @param username
     * @param p
     * @param shopID
     * @return Response object
     */
    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try {
            shop = controller.getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        }
        int newAmount = ((ServiceProduct) p).getAmount();
        double newPrice = ((ServiceProduct) p).getPrice();
        Product changed;
        try {
            changed = shop.changeProductDetail(p.getId(), p.getName(), p.getDescription(), p.getCategory(), username, newAmount, newPrice);
        } catch (ProductNotFoundException | InvalidProductInfoException e) {
            return new ResponseT<>(e.getMessage());
        }
        return new ResponseT<>(changed);
    }

    /**
     * Appoint a new Shop owner
     * @param key - shop identifier
     * @param targetUser - Passive user identifier
     * @param userName - Active user identifier
     * @return Response object
     */
    //done IncorrectIdentification, BlankDataExc
    public Response AppointNewShopOwner(int key, String targetUser, String userName) {
        try {
            marketSystem.AppointNewShopOwner(key, targetUser, userName);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc | InvalidSequenceOperationsExc | ShopNotFoundException | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Appoint a new Shop manager
     * @param key - shop identifier
     * @param targetUser - the username of the user requested to be appointing to shop manager
     * @param userName - the username of the appointed of the new manager
     * @return Response object
     */
    //done  IncorrectIdentification, BlankDataExc
    public Response AppointNewShopManager(int key, String targetUser, String userName) {
        try {
            marketSystem.AppointNewShopManager(key, targetUser, userName);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc | ShopNotFoundException | InvalidSequenceOperationsExc | InvalidAuthorizationException e) {
            return new Response( e.getMessage());
        }
    }

    /**
     * add a specific Shop manager's permission
     * @param key - shop identifier
     * @param shopManagersPermissionsList - wanted permissions to add
     * @param targetUser - the manager who receives the permissions identifier.
     * @param ownerID - the user who gives  the permissions identifier.
     * @return Response object
     */
    public Response AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        try {
            String s = marketSystem.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
            if (s != null)
                return new Response(s);
            return null;
        } catch (IncorrectIdentification | BlankDataExc | InvalidSequenceOperationsExc | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Remove a specific Shop manager's permission
     * @param key
     * @param shopManagersPermissionsList
     * @param managerUser
     * @param ownerID
     * @return Response object
     */
    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        try {
            String s = marketSystem.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
            if (s != null)
                return new Response(s);
            return null;
        } catch (IncorrectIdentification | BlankDataExc | InvalidAuthorizationException incorrectIdentification) {
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
    }

    /**
     * Close an active shop
     * @param shopId
     * @param userName
     * @return Response object
     */
    public Response closeShop(int shopId, String userName) {
        try {
            marketSystem.CloseShop(shopId, userName);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc | InvalidSequenceOperationsExc | InvalidAuthorizationException incorrectIdentification) {
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
    }

    /**
     * Make a closed shop -> active
     * @param shopId
     * @param userName
     * @return Response object
     */
    public Response reopenShop(int shopId, String userName) {
        try {
            marketSystem.OpenShop(shopId, userName);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc | InvalidSequenceOperationsExc | InvalidAuthorizationException incorrectIdentification) {
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
    }

    /**
     * Query for information about the relevant shop's employees
     * @param shopName - shop identifier
     * @param f
     * @param userName - user requesting
     * @return Response object
     */
    public ResponseList<User> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName) {
        try {
            List<User> s = marketSystem.RequestShopOfficialsInfo(shopName, f, userName);
            return new ResponseList<>(s);
        } catch (IncorrectIdentification | ShopNotFoundException | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseList<>(e.getMessage());
        }
    }

    /**
     * Query for store purchase history
     * @param shopID - shop identifier
     * @param f
     * @param userName - user requesting
     * @return list of Response object
     */
    public ResponseList<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String userName) {
        try {
            List<Order> orders = marketSystem.RequestInformationOfShopsSalesHistory(shopID, f, userName);
            return new ResponseList<>(orders);
        } catch (IncorrectIdentification | ShopNotFoundException | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseList<>(e.getMessage());
        }
    }

    //Make:nitay
    public Response DeleteUserTest(String[] usernames) {
        try {
            marketSystem.deleteUserTest(usernames);
            return new Response();
        } catch (InvalidSequenceOperationsExc | BlankDataExc | IncorrectIdentification e) {
            return new Response(e.getMessage());
        }

    }


    public Response DeleteUser(String admin, String username) {
        try {
            if (marketSystem.deleteUser(admin, username))
                return new Response();
        } catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification | InvalidAuthorizationException e) {
            return new Response(e.getMessage());
        }
        return null;
    }

    /**
     * Remove a product from the shop inventory
     * @param productId
     * @param username user requesting
     * @param shopName
     * @return Response object
     */
    public Response RemoveProductFromShopInventory(int productId, String username, int shopName) {
        int removedProductID;
        try {
            removedProductID = marketSystem.RemoveProductFromShopInventory(productId, username, shopName);
        }catch (InvalidAuthorizationException | IncorrectIdentification | BlankDataExc iae){
            return new Response(iae.getLocalizedMessage());
        }
        if(removedProductID != -1)
            return new Response();
        return null;
    }

    /**
     *
     * @param userName user requesting
     * @param f
     * @param shopID
     * @return
     */
    public ResponseMap<Shop, List<Order>> getOrderHistoryForShops(String userName, Filter<Order> f, List<Integer> shopID) {
        try {
            Map<Shop, List<Order>> result = marketSystem.getOrderHistoryForShops(userName, f);
            return new ResponseMap<Shop, List<Order>>(result);
        } catch (IncorrectIdentification | ShopNotFoundException | InvalidAuthorizationException | BlankDataExc exception) {
            return new ResponseMap<>(exception.getLocalizedMessage());
        }

    }

    /**
     * get all sale's history for user
     * @param username user requesting
     * @return
     */
    public ResponseList<Order> getOrderHistoryOfUser(String username, Filter<Order> filter){
        try {
            List<Order> result = marketSystem.getOrderHistoryOfUser(username, filter);
            return new ResponseList<>(result);
        } catch (InvalidAuthorizationException | IncorrectIdentification | InvalidSequenceOperationsExc | BlankDataExc e) {
            return new ResponseList<>(e.getMessage());
        }
    }

    /**
     * Query for the user's purchase history
     * @param userName user requesting
     * @param f
     * @param userNames
     * @return list of Response object
     */
    public ResponseMap<User, List<Order>>  getOrderHistoryForUsers(String userName, Filter<Order> f, List<String> userNames) {
        try {
            Map<User, List<Order>> result  = marketSystem.getOrderHistoryForUsers(userName, f);
            return new ResponseMap<>(result);
        } catch (InvalidAuthorizationException | IncorrectIdentification | BlankDataExc e) {
            return new ResponseMap<>(e.getMessage());
        }
    }

    /**
     * get certain product from market
     * @param username user requesting
     * @param shopId
     * @param serialNumber
     * @return
     */
    public ResponseT<Product> getProduct(String username, int shopId, int serialNumber) {
        try {
            Product p = marketSystem.getProduct(username, shopId, serialNumber);
            return new ResponseT<>(p);
        } catch (ShopNotFoundException | ProductNotFoundException | IncorrectIdentification | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Remove registered user from the Market if it has no permissions
     * @param usernames - active user identifier
     * @param targetUser - positive user identifier
     * @return Response object
     */
    public Response DismissalUserBySystemManager(String usernames,String targetUser) {
        try {
            if (marketSystem.DismissalUser(usernames, targetUser))
                return new Response();
            return new Response("operation failed.");
        }
        catch (BlankDataExc | IncorrectIdentification | InvalidSequenceOperationsExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
    }

    /**
     * Remove a store owner's permission in a particular store
     * @param usernames - active user identifier
     * @param targetUser - positive user identifier
     * @param shop - shop identifier
     * @return Response object
     */
    public Response DismissalOwnerByOwner(String usernames,String targetUser,int shop) {
        try {
            if(marketSystem.DismissalOwner(usernames,targetUser,shop))
                return new Response();
            return new Response("");
        }
        catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification | ShopNotFoundException e){
            return new Response(e.getMessage());
        }
    }

    /**
     * System manager query for user's Info
     * @param f
     * @param userName
     * @return list of Response object
     */
    public ResponseList<User> RequestUserInfo(SearchUserFilter f, String userName)
    {
        try {
            List<User> s = marketSystem.RequestUserInfo(f, userName);
            return new ResponseList<User>(s);
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc e){
            return new ResponseList<User>(e.getMessage());
        }
    }

    /**
     * Registering SystemManager by registered System Manager
     * @param systemManager
     * @param username
     * @param pw
     * @return
     */
    public Response CreateSystemManager(String systemManager,String username, String pw) {
        try {
            if(marketSystem.createSystemManager(systemManager,username, pw))
                return new Response();
        } catch (BlankDataExc | IncorrectIdentification | InvalidSequenceOperationsExc blankDataExc) {
            return new Response(blankDataExc.getLocalizedMessage());
        }
        return new Response();
    }

    /**
     * remove manager in shop
     * @param shopID
     * @param remover
     * @param managerToRemove
     * @return
     */
    public Response removeManager(int shopID, String remover, String managerToRemove){
        try{
            marketSystem.removeManger(shopID, remover, managerToRemove);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response registerForMessages(String username, UserObserver observer){
        try{
            marketSystem.registerForMessages(username, observer);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc incorrectIdentification) {
            return new Response(incorrectIdentification.getMessage());
        }
    }

    public Response registerForAdminMessages(String username, AdminObserver observer){
        try{
            marketSystem.registerForAdminMessages(username, observer);
            return new Response();
        } catch (IncorrectIdentification | BlankDataExc incorrectIdentification) {
            return new Response(incorrectIdentification.getMessage());
        }
    }

    public Response removeFromNotificationCenter(String username){
        try{
            marketSystem.removeFromNotificationCenter(username);
            return new Response();
        } catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Integer> getCurrentActiveUsers(String username){
        try{
            Integer result = marketSystem.getCurrentActiveUsers(username);
            return new ResponseT<>(result);
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Integer> getCurrentActiveMembers(String username){
        try{
            Integer result = marketSystem.getCurrentActiveMembers(username);
            return new ResponseT<>(result);
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Integer> getCurrentActiveGuests(String username){
        try{
            Integer result = marketSystem.getCurrentActiveGuests(username);
            return new ResponseT<>(result);
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Integer> getTotalMembers(String username){
        try{
            Integer result = marketSystem.getTotalMembers(username);
            return new ResponseT<>(result);
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseMap<Integer,User> getAllUsers(String username){
        try{
            Map<Integer,User> result = marketSystem.getAllUsers(username);
            return new ResponseMap<>(result);
        } catch (IncorrectIdentification | InvalidSequenceOperationsExc | InvalidAuthorizationException | BlankDataExc e) {
            return new ResponseMap<>(e.getMessage());
        }
    }

    public ResponseT<Long> getNumberOfUnreadMessages(String username){
        try{
            return new ResponseT<>(marketSystem.getNumberOfUnreadMessages(username));
        } catch (IncorrectIdentification | BlankDataExc error) {
            return new ResponseT<>(error.getMessage());
        }
    }
}