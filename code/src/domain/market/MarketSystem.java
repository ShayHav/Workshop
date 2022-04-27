package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketSystem {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static MarketSystem instance = null;
    private ExternalConnector externalConnector;
    private UserController userController = UserController.getInstance();
    private ShopController shopController = ShopController.getInstance();

    private MarketSystem() {
        eventLogger.logMsg(Level.INFO, "System start");
        externalConnector = new ExternalConnector();
    }

    public static MarketSystem getInstance() {
        if (instance == null) {
            instance = new MarketSystem();
        }
        return instance;
    }


    /***
     * init the Market system:
     * Connect to payment service
     * Connect to supply service
     * Ensures there is at least 1 System manager
     */
    public boolean start(PaymentService payment, SupplyService supply, String userID, String password) {
        return false;
    }

    /***
     *
     * @param ti - contains amount, method of payment etc
     * @return true if approve, false if otherwise
     */
    public boolean pay(TransactionInfo ti) {
        return externalConnector.pay(ti);
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti, Map<Integer, Integer> products) {
        return externalConnector.supply(ti, products);
    }

    public void setSupplierConnection(boolean b) {

    }

    public void setPaymentConnection(boolean b) {

    }

    public void createSystemManger(String username, String pw) {

    }


    public List<ShopInfo> getInfoOfShops(String userID, Filter<ShopInfo> f) {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().getInfoOfShops(f);

    }

    public List<ProductInfo> getInfoOfProductInShop(String userID, int shopID, Filter<ProductInfo> f) {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }

    public List<ProductInfo> searchProductByName(String userID, String name, Filter<ProductInfo> f) {
        if (userID == null || name == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().searchProductByName(name, f);
    }

    public List<ProductInfo> searchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    public User getUser(String id) {
        return UserController.getInstance().getUser(id);
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        return ShopController.getInstance().createShop(name, discountPolicy, purchasePolicy, id);
    }

    public boolean register(String userId, String pass) {
        return UserController.getInstance().register(userId, pass);
    }

    public boolean deleteUserTest(String[] username) {
        return UserController.getInstance().deleteUserTest(username);
    }

    public Shop getShop(int shopID) {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public boolean logIn(String username, String pw) {
        return UserController.getInstance().logIn(username, pw);
    }

    public String LeaveMarket() {
        return null;
    }

    public String logOut(String username) {
        return UserController.getInstance().logOut(username);
    }

    public int RemoveProductFromShopInventory(int productId, String userID, int shopname) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveProductFromShopInventory(productId, userID, shopname);
        else return -1;
    }

    public String CloseShop(int shopId, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().closeShop(shopId, userID);
        else return null;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String userID){
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, tragetUser, userID);
        else return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, tragetUser, userID);
        else return null;
    }

    public String AppointNewShopManager(int key, String targetUser, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopManager(key, targetUser, userID);
        else return null;
    }

    public String AppointNewShopOwner(int key, String targetUser, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopOwner(key, targetUser, userID);
        else return null;
    }

    public List<String> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return userController.checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<UserSearchInfo> RequestShopOfficialsInfo(int shopname, SearchOfficialsFilter f, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().getShop(shopname).RequestShopOfficialsInfo(f, userID);
        else return null;
    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopname, SearchOrderFilter f, String userID) {
        if(userController.isLogin(userID))
            return ShopController.getInstance().getShop(shopname).RequestInformationOfShopsSalesHistory(f, userID);
        else return null;
    }

    public String EnterMarket() {
        return userController.enterMarket();
    }

    public boolean AddProductToCart(String userID, int shopID, int productId, int amount) {
        return userController.addProductToCart(userID, shopID, productId, amount);
    }


    public boolean EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return userController.updateAmountOfProduct(userId, shopId, productId, amount);
    }

    public boolean removeProductFromCart(String userId, int shopId, int productId) {
        return userController.removeProductFromCart(userId, shopId, productId);
    }

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID) {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForShops(userID, f, shopID);
        else return null;
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String> userIDs) {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForUser(userID, f, userIDs);
        else return null;
    }
}
