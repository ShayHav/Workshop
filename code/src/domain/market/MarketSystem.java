package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;

import java.util.ArrayList;
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
    public boolean start(PaymentService payment, SupplyService supply, String userID, String password){
        if(!userController.createSystemManager(userID,password)){
            return false;
        }
        if(!externalConnector.connectToSupplyService(supply)){
            return false;
        }
        return externalConnector.connectToPaymentService(payment);
    }

    /***
     *
     * @param ti - contains amount, method of payment etc
     * @return true if approve, false if otherwise
     */
    public boolean pay(TransactionInfo ti) {
        if(ti == null)
            return false;
        return externalConnector.pay(ti);
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti, Map<Integer, Integer> products) {
        if(ti == null || products == null){
            return false;
        }
        return externalConnector.supply(ti, products);
    }

    public void setSupplierConnection(boolean b) {

    }

    public void setPaymentConnection(boolean b) {

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
        if(id == null || id.isEmpty())
            return null;
        return UserController.getInstance().getUser(id);
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String foundId) {
        if(name == null || discountPolicy == null || purchasePolicy == null || foundId == null)
            return -1;
        return ShopController.getInstance().createShop(name, discountPolicy, purchasePolicy, foundId);
    }

    public boolean register(String userId, String pass) {
        if(userId == null || pass == null)
            return false;
        return UserController.getInstance().register(userId, pass);
    }

    public boolean deleteUserTest(String[] username) {
        for(String user: username){
            if(user == null)
                return false;
        }
        return UserController.getInstance().deleteUserTest(username);
    }

    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public boolean logIn(String username, String pw) {
        if(username == null || pw == null){
            return false;
        }
        return UserController.getInstance().logIn(username, pw);
    }

    public String LeaveMarket() {
        return null;
    }

    public String logout(String username) {
        if(username == null){
            return null;
        }
        return UserController.getInstance().logOut(username);
    }

    public int RemoveProductFromShopInventory(int productId, String userID, int shopname) throws InvalidAuthorizationException {
        if(userID == null)
            return -1;
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveProductFromShopInventory(productId, userID, shopname);
        return -1;
    }

    public String CloseShop(int shopId, String userID) {
        if(userID == null)
            return null;
        if(userController.isLogin(userID))
            return ShopController.getInstance().closeShop(shopId, userID);
        return null;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) {
        if(shopManagersPermissionsList == null || targetUser == null || userID == null)
            return null;
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) {
        if(shopManagersPermissionsList == null || targetUser == null || userID == null)
            return null;
        if(userController.isLogin(userID))
            return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AppointNewShopManager(int key, String targetUser, String userID) {
        if(targetUser == null || userID == null)
            return null;
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopManager(key, targetUser, userID);
        return null;
    }

    public String AppointNewShopOwner(int key, String targetUser, String userID) {
        if(targetUser == null || userID == null)
            return null;
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopOwner(key, targetUser, userID);
        return null;
    }

    public List<String> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        if(userID == null || fullName == null || address == null || phoneNumber == null || cardNumber == null ||
        expirationDate == null)
            return null;
        return userController.checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<UserSearchInfo> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String userID) {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                return null;
            }
            return shop1.RequestShopOfficialsInfo(f, userID);
        }
        else
            return null;
    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String userID) {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                return null;
            }
            return shop1.RequestInformationOfShopsSalesHistory(f, userID);
        }
        else
            return null;
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

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID) throws InvalidAuthorizationException {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForShops(userID, f, shopID);
        else return null;
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String> userIDs) throws InvalidAuthorizationException {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForUser(userID, f, userIDs);
        else return null;
    }
}
