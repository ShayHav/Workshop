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
        eventLogger.logMsg(Level.INFO,"System start");
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
    public boolean supply(TransactionInfo ti, Map<Integer,Integer> products) {
        return externalConnector.supply(ti,products);
    }

    public void setSupplierConnection(boolean b)
    {

    }

    public void setPaymentConnection(boolean b) {

    }

    public void createSystemManger(String username, String pw) {

    }


    public List<ShopInfo> getInfoOfShops(String userID, Filter<ShopInfo> f) {
        return ShopController.getInstance().getInfoOfShops(f);
    }

    public List<ProductInfo> getInfoOfProductInShop(String userID, int shopID, Filter<ProductInfo> f){
       return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }

    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        return ShopController.getInstance().searchProductByName(name,f);
    }


    public List<ProductInfo> searchProductByCategory(String category, Filter<ProductInfo> f) {
        return ShopController.getInstance().searchProductByCategory(category,f);
    }


    public List<ProductInfo> searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    public User getUser(String id) {
        return UserController.getInstance().getUser(id);
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        return ShopController.getInstance().createShop(name,discountPolicy, purchasePolicy,id);
    }

    public boolean register(String userId,String pass){
       return UserController.getInstance().register(userId,pass);
    }

    public boolean deleteUserTest(String[] username){
        return UserController.getInstance().deleteUserTest(username);
    }

    public Shop getShop(int shopID) {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public boolean logIn(String username, String pw){
        return UserController.getInstance().logIn(username, pw);
    }
    public String LeaveMarket(){return null;}

    public boolean logOut(String username){
        return UserController.getInstance().logOut(username);
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopname){
        return ShopController.getInstance().RemoveProductFromShopInventory(productId,username,shopname);
    }
    public String CloseShop(int shopId,String userId){
        return ShopController.getInstance().closeShop(shopId,userId);
    }

    public String RemoveShopManagerPermissions(int key,List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id){
        return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList,tragetUser ,id);
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String id){
        return ShopController.getInstance().AddShopMangerPermissions(key,shopManagersPermissionsList,tragetUser,id);
    }

    public String AppointNewShopManager(int key,String targetUser, String userId){
        return ShopController.getInstance().AppointNewShopManager(key,targetUser,userId);
    }

    public String AppointNewShopOwner(int key,String targetUser, String userId){
        return ShopController.getInstance().AppointNewShopOwner(key,targetUser,userId);
    }

    public List<String> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate){
        return UserController.getInstance().checkout( userID, fullName,  address,  phoneNumber,  cardNumber,  expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec){
        externalConnector = ec;
    }
}
