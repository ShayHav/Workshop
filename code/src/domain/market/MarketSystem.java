package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.notifications.NotificationManager;
import domain.notifications.Observer;
import domain.notifications.UserObserver;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filters.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class MarketSystem {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static MarketSystem instance = null;
    private ExternalConnector externalConnector;
    private NotificationManager notificationManager = NotificationManager.getInstance();
    private final UserController userController = UserController.getInstance();
    //private final ShopController shopController = ShopController.getInstance();

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
    public boolean start(PaymentService payment, SupplyService supply, String userID, String password, UserObserver observer) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        if(!userController.createSystemManager(userID,password)){
            return false;
        }
        notificationManager.newSocketChannel(UserController.getInstance().getUser(userID), observer);
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
    public boolean supply(TransactionInfo ti, Map<Integer, Integer> products) throws BlankDataExc {
        if(ti==null)
            throw new BlankDataExc("parameter is null: TransactionInfo");
        if(products==null)
            throw new BlankDataExc("parameter is null: products");
        return externalConnector.supply(ti, products);
    }

    public void setSupplierConnection(boolean b) {

    }

    public void setPaymentConnection(boolean b) {

    }

    public List<Shop> getInfoOfShops(String userID, Filter<Shop> f) throws BlankDataExc {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().getInfoOfShops(f);

    }


    //TODO: f==null is ok? getAllInfoProduct..
    public List<Product> getInfoOfProductInShop(String userID, int shopID, Filter<Product> f) throws BlankDataExc {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public List<Product> searchProductByName(String userID, String name, Filter<Product> f) throws BlankDataExc {
        if (userID == null || name == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByName(name, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public List<Product> searchProductByKeyword(String userID, String keyword, Filter<Product> f) throws BlankDataExc {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    //TODO: need to get inside //not in use
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if(id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    public Shop createShop(String description ,String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String foundId) throws BlankDataExc, IncorrectIdentification {
        if(name == null )
            throw new BlankDataExc("parameter is null: name");
        if(discountPolicy == null )
            throw new BlankDataExc("parameter is null: discountPolicy");
        if(foundId == null )
            throw new BlankDataExc("parameter is null: foundId");
        if(purchasePolicy == null )
            throw new BlankDataExc("parameter is null: purchasePolicy");
        User shopFounder = getUser(foundId);
        Shop s = ShopController.getInstance().createShop(description ,name, discountPolicy, purchasePolicy, shopFounder);
        notificationManager.systeManagerMessage(String.format("ShopId: %d  was Create. ShopFounder: %s",s.getShopID(),shopFounder.getUserName()));
        return s;
    }

    public boolean register(String username, String pw,UserObserver observer) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification {
        if(username == null )
            throw new BlankDataExc("parameter is null: username");
        if(pw == null)
            throw new BlankDataExc("parameter is null: pw");

        if(UserController.getInstance().register(username, pw)){
            User u = UserController.getInstance().getUser(username);
            notificationManager.newSocketChannel(u,observer);
            return true;
        }
        return false;
    }

    public boolean deleteUserTest(String[] username) throws InvalidSequenceOperationsExc, BlankDataExc {

        for(String user: username){
            if(user == null)
                throw new BlankDataExc("parameter is null: username");
        }
        return UserController.getInstance().deleteUserTest(username);
    }
    public boolean deleteUser(String username) throws BlankDataExc, InvalidSequenceOperationsExc {
            if(username == null)
                throw new BlankDataExc("parameter is null: username");
        return UserController.getInstance().deleteUserName(username);
    }
    //TODO: need to get inside  //TODO: not in use
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public User logIn(String username, String pw) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        User output;
        if(username == null ) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if(pw == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: pw");
            throw new BlankDataExc("parameter is null: password");
        }
        output = UserController.getInstance().logIn(username, pw);
        notificationManager.getMessage(output);
        return output;
    }

    public User LeaveMarket(String s) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if(UserController.getInstance().getUser(s).isLoggedIn())
            logout(s);
        return null;
    }

    public User logout(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        User output;
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        output = UserController.getInstance().logOut(username);
        return output;
    }

    public int RemoveProductFromShopInventory(int productId, String userID, int shopname) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc {
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            return ShopController.getInstance().RemoveProductFromShopInventory(productId, userID, shopname);
        }
        return -1;
    }

    public String CloseShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            notificationManager.systeManagerMessage(String.format("ShopId: %d  is Close",shopId));
            return ShopController.getInstance().closeShop(shopId, userID);
        }
        return null;
    }
    public String OpenShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            notificationManager.systeManagerMessage(String.format("ShopId: %d  is Open",shopId));
            return ShopController.getInstance().openShop(shopId, userID);
        }
        return null;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc {
        if(shopManagersPermissionsList == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: shopManagersPermissionsList");
            throw new BlankDataExc("parameter is null: shopManagersPermissionsList");
        }
        if(targetUser == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: targetUser");
        }
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(shopManagersPermissionsList == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: shopManagersPermissionsList");
            throw new BlankDataExc("parameter is null: username");
        }
        if(targetUser == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID))
            return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AppointNewShopManager(int shopID, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        String output;
        if(targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userID == null)
            throw new BlankDataExc("parameter is null: username");
        if(userController.isLogin(userID)) {
            output = ShopController.getInstance().AppointNewShopManager(shopID, targetUser, userID);
            return output;
        }
        return null;
    }

    public String AppointNewShopOwner(int shopID, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        String output;
        if(targetUser == null ) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            output = ShopController.getInstance().AppointNewShopOwner(shopID, targetUser, userID);
            return output;
        }
        return null;
    }

    public List<String> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        if (userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (fullName == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: fullName");
            throw new BlankDataExc("parameter is null: fullName");
        }
        if (address == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: address");
            throw new BlankDataExc("parameter is null: address");
        }
        if (cardNumber == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: cardNumber");
            throw new BlankDataExc("parameter is null: cardNumber");
        }
        if (phoneNumber == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: phoneNumber");
            throw new BlankDataExc("parameter is null: phoneNumber");
        }
        if (expirationDate == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: expirationDate");
            throw new BlankDataExc("parameter is null: expirationDate");
        }
        List<String> output =userController.checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
        return output;
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<User> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String userID) throws IncorrectIdentification {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = ShopController.getInstance().getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                return null;
            }
            return shop1.RequestShopOfficialsInfo(f, userID);
        }
        else
            return null;
    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String userID) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = ShopController.getInstance().getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                return null;
            }
            return shop1.RequestInformationOfShopsSalesHistory(f, userID);
        }
        else throw new InvalidSequenceOperationsExc();
    }

    public User EnterMarket() {
        return userController.enterMarket();
    }

    public boolean AddProductToCart(String userID, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException {
        return userController.addProductToCart(userID, shopID, productId, amount);
    }


    public boolean EditShoppingCart(String userId, int shopId, int productId, int amount) throws InvalidSequenceOperationsExc {
        return userController.updateAmountOfProduct(userId, shopId, productId, amount);
    }

    public boolean removeProductFromCart(String userId, int shopId, int productId) throws InvalidSequenceOperationsExc {
        return userController.removeProductFromCart(userId, shopId, productId);
    }

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID) throws InvalidAuthorizationException, IncorrectIdentification, ShopNotFoundException {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForShops(userID, f, shopID);
        else return null;
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String> userIDs) throws InvalidAuthorizationException, IncorrectIdentification {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForUser(userID, f, userIDs);
        else return null;
    }

    public boolean DismissalUser(String usernames, String targetUser) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if(usernames==null)
            throw new BlankDataExc("parameter is null: usernames");
        if(targetUser== null)
            throw new BlankDataExc("parameter is null: targetUser");
        User u = userController.getUser(usernames);
        notificationManager.disConnected(u);
        if(u.DismissalUser(targetUser)){
            notificationManager.systeManagerMessage(String.format("user has dismissal: %s",targetUser));
            return true;
        }
        else return false;
    }

    public boolean DismissalOwner(String usernames, String targetUser, int shop, Observer observer) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, ShopNotFoundException {
        if(usernames==null)
            throw new BlankDataExc("parameter is null: usernames");
        if(targetUser== null)
            throw new BlankDataExc("parameter is null: targetUser");
        User u = userController.getUser(usernames);
        notificationManager.disConnected(u);
        if(u.DismissalOwner(targetUser,shop)){
            notificationManager.shopOwnerMessage(shop,String.format("Owner of shop: %d has dismissal: %s",shop,targetUser));
            return true;
        }
        return false;

    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        return userController.RequestUserInfo(f,userName);
    }

    public boolean createSystemManager(String systemManager, String username, String pw) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification {
        if(username == null )
            throw new BlankDataExc("parameter is null: username");
        if(pw == null)
            throw new BlankDataExc("parameter is null: pw");
        if(systemManager==null)
            throw new BlankDataExc("parameter is null: systemManager");
        if(UserController.getInstance().getUser(systemManager).isSystemManager())
            return UserController.getInstance().createSystemManager(username, pw);
        else return false;
    }
}
