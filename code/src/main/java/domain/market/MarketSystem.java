package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.ResponseT;
import domain.notifications.NotificationManager;
import domain.notifications.UserObserver;
import domain.Response;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.*;

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

    public List<ShopManagersPermissions> checkPermissionsForManager(String managerUsername, int shopID) throws ShopNotFoundException, IllegalArgumentException{
        return ShopController.getInstance().checkPermissionsForManager(managerUsername,shopID);
    }

    private static class MarketHolder{
        private static final MarketSystem market = new MarketSystem();
    }

    public static MarketSystem getInstance() {
        return MarketHolder.market;
    }


    /***
     * init the Market system:
     * Connect to payment service
     * Connect to supply service
     * Ensures there is at least 1 System manager
     */
    public boolean start(PaymentService payment, SupplyService supply, String userID, String password) throws InvalidSequenceOperationsExc, IncorrectIdentification {
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

    public Cart.ServiceCart showCart(String username) throws BlankDataExc {
        if(username==null || !userController.HasUserEnteredMarket(username))
            throw new BlankDataExc("username is null or not entered market");
        return userController.showCart(username);
    }

    //TODO: f==null is ok? getAllInfoProduct..
    public List<Product> getInfoOfProductInShop(String userID, int shopID, Filter<Product> f) throws BlankDataExc {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public Map<Integer,List<Product>> searchProductByName(String userID, String name, Filter<Product> f) throws BlankDataExc {
        if (userID == null || name == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByName(name, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public Map<Integer,List<Product>> searchProductByKeyword(String userID, String keyword, Filter<Product> f) throws BlankDataExc {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    public Map<Integer,List<Product>> searchProductByCategory(String userID, String category, Filter<Product> f) throws BlankDataExc {
        if (userID == null || category == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByCategory(category, f);
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
        if(foundId == null )
            throw new BlankDataExc("parameter is null: foundId");
        User shopFounder = getUser(foundId);
        Shop s = ShopController.getInstance().createShop(description ,name, discountPolicy, purchasePolicy, shopFounder);
        shopFounder.addRole(s.getShopID(), Role.ShopFounder);
        //notificationManager.systeManagerMessage(String.format("ShopId: %d  was Create. ShopFounder: %s",s.getShopID(),shopFounder.getUserName()));
        return s;
    }

    public boolean register(String username, String pw) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification {
        if(username == null )
            throw new BlankDataExc("parameter is null: username");
        if(pw == null)
            throw new BlankDataExc("parameter is null: pw");

        if(UserController.getInstance().register(username, pw)){
            User u = UserController.getInstance().getUser(username);
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
    public User logIn(String username, String pw, UserObserver o) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
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
        //notificationManager.newSocketChannel(output,observer);
        return output;
    }

    public User LeaveMarket(String s) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        User u = UserController.getInstance().getUser(s);
        if(!u.isEnteredMarket()) {
            errorLogger.logMsg(Level.WARNING,String.format("attempt to leaveMarket without enterMarket user: %s",s));
            throw new InvalidSequenceOperationsExc(String.format("attempt to leaveMarket without enterMarket user: %s",s));
        }
        if(u.isLoggedIn())
            logout(s);
        notificationManager.disConnected(u);
        return null;
    }

    public User logout(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        if(username == null){
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: username");
            throw new BlankDataExc("username");
        }
        User u = UserController.getInstance().logOut(username);
        notificationManager.disConnected(u);
        return u;
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

    public void CloseShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            notificationManager.systeManagerMessage(String.format("ShopId: %d  is Close",shopId));
            ShopController.getInstance().closeShop(shopId, userID);
        }
    }
    public void OpenShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null) {
            errorLogger.logMsg(Level.WARNING,"BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if(userController.isLogin(userID)) {
            notificationManager.systeManagerMessage(String.format("ShopId: %d  is Open",shopId));
            ShopController.getInstance().openShop(shopId, userID);
        }
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

    public List<User> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String userID) throws IncorrectIdentification, ShopNotFoundException {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = ShopController.getInstance().getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                throw snfe;
            }
            return shop1.RequestShopOfficialsInfo(f, userID);
        }
        else
            return null;
    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String userID) throws IncorrectIdentification, ShopNotFoundException, InvalidSequenceOperationsExc {
        if(userController.isLogin(userID)) {
            Shop shop1;
            try{
                shop1 = ShopController.getInstance().getShop(shopID);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shopID));
                throw snfe;
            }
            return shop1.RequestInformationOfShopsSalesHistory(f, userID);
        }
        else throw new InvalidSequenceOperationsExc();
    }

    public User EnterMarket() {
        return userController.enterMarket();
    }

    public Response AddProductToCart(String userID, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException {

        return userController.addProductToCart(userID, shopID, productId, amount);
    }

    public Response EditShoppingCart(String userId, int shopId, int productId, int amount) throws InvalidSequenceOperationsExc {
        return userController.updateAmountOfProduct(userId, shopId, productId, amount);
    }

    public Response removeProductFromCart(String userId, int shopId, int productId) throws InvalidSequenceOperationsExc {
        userController.removeProductFromCart(userId, shopId, productId);
        return new Response();
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

    public Product getProduct(String username, int shopId, int serialNumber) throws ShopNotFoundException, ProductNotFoundException {
        Shop shop = ShopController.getInstance().getShop(shopId);
        return shop.getProduct(serialNumber);
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

    public boolean DismissalOwner(String usernames, String targetUser, int shop) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, ShopNotFoundException {
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
        if(UserController.getInstance().getUser(systemManager).isSystemManager()) {
            if(UserController.getInstance().createSystemManager(username, pw)){
                User u = UserController.getInstance().getUser(username);
                return true;
            }
        }
        return false;
    }

    public void removeManger(int shopID, String remover, String managerToRemove) throws IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        if(remover == null || managerToRemove == null)
            throw new BlankDataExc("parameters cannot be null");
        User user = userController.getUser(remover);
        if(!userController.isLogin(remover)){
            throw new InvalidAuthorizationException("user is not logged in");
        }
        User manager = userController.getUser(managerToRemove);
        //TODO remove the manager
    }

}
