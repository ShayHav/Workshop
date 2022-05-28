package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.ResponseMap;
import domain.notifications.AdminObserver;
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

    public List<ShopManagersPermissions> checkPermissionsForManager(String managerUsername, int shopID) throws ShopNotFoundException, IllegalArgumentException {
        return ShopController.getInstance().checkPermissionsForManager(managerUsername, shopID);
    }




    private static class MarketHolder {
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
        if (!userController.createSystemManager(userID, password)) {
            return false;
        }
        if (!externalConnector.connectToSupplyService(supply)) {
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
        if (ti == null)
            return false;
        return externalConnector.pay(ti);
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti, Map<Integer, Integer> products) throws BlankDataExc {
        if (ti == null)
            throw new BlankDataExc("parameter is null: TransactionInfo");
        if (products == null)
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
        if (username == null || !userController.HasUserEnteredMarket(username))
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
    public Map<Integer, List<Product>> searchProductByName(String userID, String name, Filter<Product> f) throws BlankDataExc {
        if (userID == null || name == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByName(name, f);
    }

    //TODO: f==null is ok? getAllInfoProduct..
    public Map<Integer, List<Product>> searchProductByKeyword(String userID, String keyword, Filter<Product> f) throws BlankDataExc {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    public Map<Integer, List<Product>> searchProductByCategory(String userID, String category, Filter<Product> f) throws BlankDataExc {
        if (userID == null || category == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().searchProductByCategory(category, f);
    }

    //TODO: need to get inside //not in use
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if (id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    public Shop createShop(String description, String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String foundId) throws BlankDataExc, IncorrectIdentification {
        if (name == null)
            throw new BlankDataExc("parameter is null: name");
        if (foundId == null)
            throw new BlankDataExc("parameter is null: foundId");
        User shopFounder = getUser(foundId);
        Shop s = ShopController.getInstance().createShop(description, name, discountPolicy, purchasePolicy, shopFounder);
        shopFounder.addRole(s.getShopID(), Role.ShopFounder);
        //notificationManager.systeManagerMessage(String.format("ShopId: %d  was Create. ShopFounder: %s",s.getShopID(),shopFounder.getUserName()));
        return s;
    }

    public boolean register(String guestUsername, String registerUsername, String pw) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        if (guestUsername == null)
            throw new BlankDataExc("parameter is null: guestUsername");
        if (registerUsername == null)
            throw new BlankDataExc("parameter is null: registerUsername");
        if (pw == null)
            throw new BlankDataExc("parameter is null: pw");
        if(!userController.HasUserEnteredMarket(guestUsername)){
            throw new InvalidAuthorizationException(String.format("user %s has not entered market", guestUsername));
        }

        if (UserController.getInstance().register(registerUsername, pw)) {
            User u = UserController.getInstance().getUser(registerUsername);
            notificationManager.notifyAdmin();
            return true;
        }
        return false;
    }

    public boolean deleteUserTest(String[] username) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification {

        for (String user : username) {
            if (user == null)
                throw new BlankDataExc("parameter is null: username");
        }
        return UserController.getInstance().deleteUserTest(username);
    }

    public boolean deleteUser(String admin, String username) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (admin == null)
            throw new BlankDataExc("parameter is null: admin");

        if(!userController.isLogin(admin)){
            throw new InvalidSequenceOperationsExc(String.format("user %s us not logged-in",admin));
        }
        if(!userController.isAdmin(admin)){
            throw new InvalidAuthorizationException(String.format("user %s us not authorized to perform this action",admin));
        }
        boolean result =  UserController.getInstance().deleteUserName(username);

        if(result)
            notificationManager.notifyAdmin();

        return result;
    }

    //TODO: need to get inside  //TODO: not in use
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public User login(String guestUsername, String username, String pw, UserObserver o) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        User output;
        if (guestUsername == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: guestUsername");
            throw new BlankDataExc("parameter is null: guestUsername");
        }
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if (pw == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: pw");
            throw new BlankDataExc("parameter is null: password");
        }
        if(!userController.HasUserEnteredMarket(guestUsername)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", guestUsername));
        }

        output = UserController.getInstance().login(guestUsername,username, pw);
        notificationManager.notifyAdmin();
        return output;
    }

    public User LeaveMarket(String username) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException {
        if(username == null){
            throw new BlankDataExc("username given is null");
        }
        if(!userController.HasUserEnteredMarket(username)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to leaveMarket without enterMarket user: %s", username));
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market. cannot leave market", username));
        }
        return userController.leaveMarket(username);
    }

    public User logout(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("username");
        }
        return UserController.getInstance().logout(username);
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopname) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username)) {
            return ShopController.getInstance().RemoveProductFromShopInventory(productId, username, shopname);
        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public void CloseShop(int shopId, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username)) {
            ShopController.getInstance().closeShop(shopId, username);
        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public void OpenShop(int shopId, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username)) {
            ShopController.getInstance().openShop(shopId, username);
        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        if (shopManagersPermissionsList == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: shopManagersPermissionsList");
            throw new BlankDataExc("parameter is null: shopManagersPermissionsList");
        }
        if (targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: targetUser");
        }
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username))
            return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, targetUser, username);
        else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (shopManagersPermissionsList == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: shopManagersPermissionsList");
            throw new BlankDataExc("parameter is null: username");
        }
        if (targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username))
            return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, username);
        else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public void AppointNewShopManager(int shopID, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException, ShopNotFoundException {
        if (targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username)) {
            ShopController.getInstance().AppointNewShopManager(shopID, targetUser, username);
        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public void AppointNewShopOwner(int shopID, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException, ShopNotFoundException {
        if (targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (userController.isLogin(username)) {
            ShopController.getInstance().AppointNewShopOwner(shopID, targetUser, username);

        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }

    }

    public List<String> Checkout(String username, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        if (fullName == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: fullName");
            throw new BlankDataExc("parameter is null: fullName");
        }
        if (address == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: address");
            throw new BlankDataExc("parameter is null: address");
        }
        if (cardNumber == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: cardNumber");
            throw new BlankDataExc("parameter is null: cardNumber");
        }
        if (phoneNumber == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: phoneNumber");
            throw new BlankDataExc("parameter is null: phoneNumber");
        }
        if (expirationDate == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: expirationDate");
            throw new BlankDataExc("parameter is null: expirationDate");
        }
        return userController.checkout(username, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<User> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String username) throws IncorrectIdentification, ShopNotFoundException, InvalidAuthorizationException, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username)) {
            Shop shop1;
            try {
                shop1 = ShopController.getInstance().getShop(shopID);
            } catch (ShopNotFoundException snfe) {
                errorLogger.logMsg(Level.WARNING, String.format("Shop: %d does not exist", shopID));
                throw snfe;
            }
            return shop1.RequestShopOfficialsInfo(f, username);
        } else
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String username) throws IncorrectIdentification, ShopNotFoundException, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username)) {
            Shop shop1;
            try {
                shop1 = ShopController.getInstance().getShop(shopID);
            } catch (ShopNotFoundException snfe) {
                errorLogger.logMsg(Level.WARNING, String.format("Shop: %d does not exist", shopID));
                throw snfe;
            }
            return shop1.RequestInformationOfShopsSalesHistory(f, username);
        } else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public User EnterMarket() {
        return userController.enterMarket();
    }

    public Response AddProductToCart(String username, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        return userController.addProductToCart(username, shopID, productId, amount);
    }

    public Response EditShoppingCart(String username, int shopId, int productId, int amount) throws InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, IncorrectIdentification {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        return userController.updateAmountOfProduct(username, shopId, productId, amount);
    }

    public Response removeProductFromCart(String username, int shopId, int productId) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        return userController.removeProductFromCart(username, shopId, productId);
    }

    public Map<Shop, List<Order>> getOrderHistoryForShops(String username, Filter<Order> f) throws InvalidAuthorizationException, IncorrectIdentification, ShopNotFoundException, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username))
            return userController.getOrderHistoryForShops(username, f);
        else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public List<Order> getOrderHistoryOfUser(String username) throws InvalidAuthorizationException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username))
            return userController.getOrderHistoryOfUser(username);
        else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public Map<User, List<Order>> getOrderHistoryForUsers(String username, Filter<Order> f) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username))
            return userController.getOrderHistoryForUsers(username, f);
        else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public Product getProduct(String username, int shopId, int serialNumber) throws ShopNotFoundException, ProductNotFoundException, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        Shop shop = ShopController.getInstance().getShop(shopId);
        return shop.getProduct(serialNumber);

    }

    public boolean DismissalUser(String usernames, String targetUser) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if (usernames == null)
            throw new BlankDataExc("parameter is null: usernames");
        if (targetUser == null)
            throw new BlankDataExc("parameter is null: targetUser");
        User u = userController.getUser(usernames);
        if (u.DismissalUser(targetUser)) {
            return true;
        } else return false;
    }

    public boolean DismissalOwner(String usernames, String targetUser, int shop) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, ShopNotFoundException {
        if (usernames == null)
            throw new BlankDataExc("parameter is null: usernames");
        if (targetUser == null)
            throw new BlankDataExc("parameter is null: targetUser");
        User u = userController.getUser(usernames);
        if (u.DismissalOwner(targetUser, shop)) {
            String message = String.format("User: %s remove you from being an owner of shop %s", usernames, getShop(shop).getShopName());
            notificationManager.sendMessage(getUser(targetUser), "", u);
            return true;
        }
        return false;

    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        return userController.RequestUserInfo(f, userName);
    }

    public boolean createSystemManager(String systemManager, String username, String pw) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (pw == null)
            throw new BlankDataExc("parameter is null: pw");
        if (systemManager == null)
            throw new BlankDataExc("parameter is null: systemManager");
        if (UserController.getInstance().getUser(systemManager).isSystemManager()) {
            if (UserController.getInstance().createSystemManager(username, pw)) {
                User u = UserController.getInstance().getUser(username);
                return true;
            }
        }
        return false;
    }

    public void removeManger(int shopID, String remover, String managerToRemove) throws IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        if (remover == null || managerToRemove == null)
            throw new BlankDataExc("parameters cannot be null");
        User user = userController.getUser(remover);
        if (!userController.isLogin(remover)) {
            throw new InvalidAuthorizationException("user is not logged in");
        }
        User manager = userController.getUser(managerToRemove);
        //TODO remove the manager
    }

    public List<Shop> GetAllUserShops(String username) throws BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username)) {
            return ShopController.getInstance().getAllUserShops(username);
        } else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public void registerForMessages(String username, UserObserver observer) throws IncorrectIdentification, BlankDataExc {
        if(username == null || username.isEmpty() || observer == null){
            throw new BlankDataExc("Illegal parameters");
        }
        User user = userController.getUser(username);
        notificationManager.registerObserver(user, observer);
    }

    public void registerForAdminMessages(String username, AdminObserver observer) throws IncorrectIdentification, BlankDataExc {
        if(username == null || username.isEmpty() || observer == null){
            throw new BlankDataExc("Illegal parameters");
        }
        User user = userController.getUser(username);
        notificationManager.registerAdminObserver(user, observer);
    }

    public void removeFromNotificationCenter(String username){
        notificationManager.removeFromObserversList(username);
    }

    public void sendMessage(User addressee, User sender, String content){
        notificationManager.sendMessage(addressee, content, sender);
    }

    public Integer getCurrentActiveUsers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if(username == null){
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.HasUserEnteredMarket(username) || !userController.isLogin(username)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", username));
        }
        return userController.getCurrentActiveUsers(username);
    }

    public Integer getCurrentActiveMembers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if(username == null){
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.HasUserEnteredMarket(username) || !userController.isLogin(username)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", username));
        }
        return userController.getCurrentActiveMembers(username);
    }

    public Integer getCurrentActiveGuests(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if(username == null){
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.HasUserEnteredMarket(username) || !userController.isLogin(username)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", username));
        }
        return userController.getCurrentActiveGuests(username);
    }

    public Integer getTotalMembers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if(username == null){
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.HasUserEnteredMarket(username) || !userController.isLogin(username)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", username));
        }
        return userController.getTotalMembers(username);
    }

    public Map<Integer, User> getAllUsers(String username) throws BlankDataExc, IncorrectIdentification, InvalidAuthorizationException, InvalidSequenceOperationsExc {
        if(username == null){
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.HasUserEnteredMarket(username) || !userController.isLogin(username)){
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market", username));
        }
        return userController.getAllUsers(username);
    }
}
