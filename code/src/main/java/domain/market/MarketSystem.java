package domain.market;

import domain.*;
import domain.Exceptions.*;
import domain.notifications.AdminObserver;
import domain.notifications.NotificationManager;
import domain.notifications.UserObserver;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.shop.predicate.ToBuildDiscountPredicate;
import domain.shop.predicate.ToBuildPRPredicateFrom;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.*;
import io.github.cdimascio.dotenv.Dotenv;

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
    private final ShopController shopController = ShopController.getInstance();

    private MarketSystem() {
        eventLogger.logMsg(Level.INFO, "System start");
        externalConnector = new ExternalConnector();
    }

    public List<ShopManagersPermissions> checkPermissionsForManager(String managerUsername, int shopID) throws ShopNotFoundException, IllegalArgumentException {
        return ShopController.getInstance().checkPermissionsForManager(managerUsername, shopID);
    }

    public ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID) throws BlankDataExc {
        //ShopController controller = ShopController.getInstance();
        if (serialNumber<1)
            throw new BlankDataExc("parameter is null: serialNumber");
        if(pName == null | pName == "")
            throw new BlankDataExc("parameter is null: pName");
        if(pCat == null | pCat =="")
            throw new BlankDataExc("parameter is null: pCat");
        if(pDis == null | pDis =="")
            throw new BlankDataExc("parameter is null: pDis");
        if (price<1)
            throw new BlankDataExc("parameter is null: price");
        if (amount<1)
            throw new BlankDataExc("parameter is null: amount");
        try {
            if(!userController.userExist(username))
                return new ResponseT<>(new InvalidSequenceOperationsExc("user not registered in").getLocalizedMessage());
            if (!userController.isLogin(username))
                return new ResponseT<>(new InvalidSequenceOperationsExc("user not logged in").getLocalizedMessage());
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new ResponseT<>(incorrectIdentification.getLocalizedMessage());
        }
        Shop shop;
        try {
            shop = shopController.getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        }
        Product p;
        try {
            p = shop.addListing(serialNumber, pName, pDis, pCat, price, amount, username);
        } catch (InvalidAuthorizationException iae) {
            return new ResponseT<>(null, iae.getLocalizedMessage());
        } catch (InvalidProductInfoException ipie) {
            return new ResponseT<>(null, ipie.getLocalizedMessage());
        }
        if (p == null) {
            return null;
        }
        return new ResponseT<>(p);
    }

    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        if(!userController.userExist(username))
            throw new InvalidSequenceOperationsExc("user not registered in");
        if (!userController.isLogin(username)) {
            throw new InvalidSequenceOperationsExc("user not logged in");
        }
        Shop shop;
        try {
            shop = shopController.getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        }
        int newAmount = p.getAmount();
        double newPrice = p.getPrice();
        Product changed;
        try {
            changed = shop.changeProductDetail(p.getId(), p.getName(), p.getDescription(), p.getCategory(), username, newAmount, newPrice);
        } catch (ProductNotFoundException pnfe) {
            return new ResponseT<>(null, pnfe.getLocalizedMessage());
        } catch (InvalidProductInfoException ipie) {
            return new ResponseT<>(null, ipie.getLocalizedMessage());
        }
        return new ResponseT<>(changed);
    }

    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID) {
        Shop shop;
        try {
            shop = shopController.getShop(shopID);
            return new ResponseT<>(shop.getProduct(p.getId()) != null);
        } catch (ShopNotFoundException snfe) {
            return new ResponseT<>(null, snfe.getLocalizedMessage());
        } catch (ProductNotFoundException e) {
            return new ResponseT<>(e.getMessage());
        }


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
    public boolean start(PaymentService payment, SupplyService supply) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        String adminUsername = dotenv.get("Admin_username"), password = dotenv.get("Admin_password");
        String mod = dotenv.get("Mod");
        if (!userController.createSystemManager(adminUsername, password)) {
            return false;
        }
        if(mod.equals("production")) {
            return externalConnector.connectToSupplyService(new SupplyServiceImp()) && externalConnector.connectToPaymentService(new PaymentServiceImp());
        }
        //TODO figure out what to do if we are at release
        return true;
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

    public List<Shop> getInfoOfShops(String userID, Filter<Shop> f) throws BlankDataExc, InvalidSequenceOperationsExc {
        if (userID == null || f == null )
            throw new BlankDataExc();
        isEnter(userID);
        return ShopController.getInstance().getInfoOfShops(f);

    }

    public Cart.ServiceCart showCart(String username) throws BlankDataExc, InvalidSequenceOperationsExc {
        if (username == null)
            throw new BlankDataExc("username is null or not entered market");
        isEnter(username);
        return userController.showCart(username);
    }

    //TODO: f==null is ok? getAllInfoProduct..
    public List<Product> getInfoOfProductInShop(String userID, int shopID, Filter<Product> f) throws BlankDataExc, InvalidSequenceOperationsExc {
        if (userID == null || f == null )
            throw new BlankDataExc("username is null or not entered market");
        isEnter(userID);
        return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }

    //TODO: f==null is ok? getAllInfoProduct..
    public Map<Integer, List<Product>> searchProductByName(String userID, String name, Filter<Product> f) throws BlankDataExc, InvalidSequenceOperationsExc {
        if (userID == null || name == null || f == null )
            throw new BlankDataExc("parameter is null");
        isEnter(userID);
        return ShopController.getInstance().searchProductByName(name, f);
    }

    //TODO: f==null is ok? getAllInfoProduct..
    public Map<Integer, List<Product>> searchProductByKeyword(String userID, String keyword, Filter<Product> f) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc("parameter is null");
        isEnter(userID);
        if(isExist(userID))
            isLogin(userID);
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    public Map<Integer, List<Product>> searchProductByCategory(String userID, String category, Filter<Product> f) throws BlankDataExc {
        if (userID == null || category == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc("parameter is null");
        return ShopController.getInstance().searchProductByCategory(category, f);
    }

    //TODO: need to get inside //not in use
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if (id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    public Shop createShop(String description, String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String foundId) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        if (name == null)
            throw new BlankDataExc("parameter is null: name");
        if (foundId == null)
            throw new BlankDataExc("parameter is null: foundId");
        isEnter(foundId);
        isExist(foundId);
        isLogin(foundId);
        User shopFounder = getUser(foundId);
        Shop s = ShopController.getInstance().createShop(description, name, discountPolicy, purchasePolicy, shopFounder);
        //shopFounder.addRole(s.getShopID(), Role.ShopFounder);
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
        if (!userController.HasUserEnteredMarket(guestUsername)) {
            throw new InvalidAuthorizationException(String.format("user %s has not entered market", guestUsername));
        }

        if (UserController.getInstance().register(registerUsername, pw)) {
            User u = UserController.getInstance().getUser(registerUsername);
            notificationManager.notifyAdmin();
            return true;
        }
        return false;
    }

    public boolean deleteUserTest(String[] username) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, ShopNotFoundException {

        for (String user : username) {
            if (user == null)
                throw new BlankDataExc("parameter is null: username");
        }
        return UserController.getInstance().deleteUserTest(username);
    }

    public boolean deleteUser(String admin, String username) throws BlankDataExc, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, ShopNotFoundException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (admin == null)
            throw new BlankDataExc("parameter is null: admin");
        isLogin(admin);
        if (!userController.isAdmin(admin)) {
            throw new InvalidAuthorizationException(String.format("user %s us not authorized to perform this action", admin));
        }
        boolean result = UserController.getInstance().deleteUserName(username);

        if (result)
            notificationManager.notifyAdmin();

        return result;
    }

    //TODO: need to get inside  //TODO: not in use
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public User login(String guestUsername, String username, String pw) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
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
        isEnter(guestUsername);
        output = UserController.getInstance().login(guestUsername, username, pw);
        notificationManager.notifyAdmin();
        return output;
    }

    public User LeaveMarket(String username) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException {
        if (username == null) {
            throw new BlankDataExc("username given is null");
        }
        if (!userController.HasUserEnteredMarket(username)) {
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
        isExist(username);
        isLogin(username);
        return UserController.getInstance().logout(username);
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopname) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidProductInfoException {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return ShopController.getInstance().RemoveProductFromShopInventory(productId, username, shopname);
    }

    public void CloseShop(int shopId, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException, ShopNotFoundException {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: username");
            throw new BlankDataExc("parameter is null: username");
        }
        if(!userController.userExist(username))
            throw new InvalidSequenceOperationsExc("user not registered in");
        if (userController.isLogin(username)) {
            ShopController.getInstance().closeShop(shopId, username);
        } else {
            throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
        }
    }

    public void OpenShop(int shopId, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException, ShopNotFoundException {
        if (username == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: userID");
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        ShopController.getInstance().openShop(shopId, username);
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidAuthorizationException, InvalidSequenceOperationsExc, ShopNotFoundException {
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
        isEnter(username);
        isExist(username);
        isExist(targetUser);
        isLogin(username);
        return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, targetUser, username);

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
        isExist(username);
        isExist(targetUser);
        isLogin(username);
        return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, username);

    }

    public void AppointNewShopManager(int shopID, String targetUser, String username) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, InvalidAuthorizationException, ShopNotFoundException {
        if (targetUser == null) {
            errorLogger.logMsg(Level.WARNING, "BlankDataExc: targetUser");
            throw new BlankDataExc("parameter is null: username");
        }
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isExist(targetUser);
        isLogin(username);
        ShopController.getInstance().AppointNewShopManager(shopID, targetUser, username);

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
        isEnter(username);
        isExist(username);
        isExist(targetUser);
        isLogin(username);
        ShopController.getInstance().AppointNewShopOwner(shopID, targetUser, username);
    }





    public List<String> Checkout(String username, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
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
        isEnter(username);
        return userController.checkout(username, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<User> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String username) throws IncorrectIdentification, ShopNotFoundException, InvalidAuthorizationException, BlankDataExc, InvalidSequenceOperationsExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isLogin(username);
        Shop shop1;
        try {
            shop1 = ShopController.getInstance().getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.WARNING, String.format("Shop: %d does not exist", shopID));
            throw snfe;
        }
        return shop1.RequestShopOfficialsInfo(f, username);

    }

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String username) throws IncorrectIdentification, ShopNotFoundException, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isLogin(username);
        Shop shop1;
        try {
            shop1 = ShopController.getInstance().getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.WARNING, String.format("Shop: %d does not exist", shopID));
            throw snfe;
        }
        return shop1.RequestInformationOfShopsSalesHistory(f, username);
    }

    public User EnterMarket() {
        return userController.enterMarket();
    }

    public Response AddProductToCart(String username, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        return userController.addProductToCart(username, shopID, productId, amount);
    }

    public Response EditShoppingCart(String username, int shopId, int productId, int amount) throws InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, IncorrectIdentification {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        return userController.updateAmountOfProduct(username, shopId, productId, amount);
    }

    public Response removeProductFromCart(String username, int shopId, int productId) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        return userController.removeProductFromCart(username, shopId, productId);
    }

    public Map<Shop, List<Order>> getOrderHistoryForShops(String username, Filter<Order> f) throws InvalidAuthorizationException, IncorrectIdentification, ShopNotFoundException, BlankDataExc, InvalidSequenceOperationsExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getOrderHistoryForShops(username, f);

    }

    public List<Order> getOrderHistoryOfUser(String username, Filter<Order> filter) throws InvalidAuthorizationException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getOrderHistoryOfUser(username, filter);
    }

    public Map<User, List<Order>> getOrderHistoryForUsers(String username, Filter<Order> f) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getOrderHistoryForUsers(username, f);

    }

    public Product getProduct(String username, int shopId, int serialNumber) throws ShopNotFoundException, ProductNotFoundException, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc, InvalidSequenceOperationsExc {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        isEnter(username);
        Shop shop = ShopController.getInstance().getShop(shopId);
        return shop.getProduct(serialNumber);

    }

    public boolean DismissalUser(String usernames, String targetUser) throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, ShopNotFoundException {
        if (usernames == null)
            throw new BlankDataExc("parameter is null: usernames");
        if (targetUser == null)
            throw new BlankDataExc("parameter is null: targetUser");
        isExist(usernames);
        isExist(targetUser);
        isLogin(usernames);
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
        isExist(usernames);
        isExist(targetUser);
        isLogin(usernames);
        User u = userController.getUser(usernames);
        if (userController.DismissalOwner(usernames, targetUser, shop)) {
            String message = String.format("User: %s remove you from being an owner of shop %s", usernames, getShop(shop).getShopName());
            notificationManager.sendMessage(getUser(targetUser), "", u);
            return true;
        }
        return false;

    }

    private boolean isExist(String userName) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        if(!userController.userExist(userName))
            throw new InvalidSequenceOperationsExc(String.format("user not registered in",userName));
        return true;
    }
    private void isLogin(String userName) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if (!userController.isLogin(userName))
            throw  new InvalidSequenceOperationsExc(String.format("user not logged in",userName));
    }
    private void isEnter(String userName) throws InvalidSequenceOperationsExc {
        if (!userController.HasUserEnteredMarket(userName)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to leaveMarket without enterMarket user: %s", userName));
            throw new InvalidSequenceOperationsExc(String.format("user %s has not entered market. cannot leave market", userName));
        }
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
        //isExist(username);
        isExist(systemManager);
        isLogin(systemManager);
        if (UserController.getInstance().getUser(systemManager).isSystemManager()) {
            if (UserController.getInstance().createSystemManager(username, pw)) {
                User u = UserController.getInstance().getUser(username);
                return true;
            }
        }
        return false;
    }

    public void removeManger(int shopID, String remover, String managerToRemove) throws IncorrectIdentification, BlankDataExc, InvalidAuthorizationException, InvalidSequenceOperationsExc {
        if (remover == null || managerToRemove == null)
            throw new BlankDataExc("parameters cannot be null");
        User user = userController.getUser(remover);
        isEnter(remover);
        isExist(remover);
        isExist(managerToRemove);
        isLogin(remover);
        User manager = userController.getUser(managerToRemove);
        //TODO remove the manager
    }

    public List<Shop> GetAllUserShops(String username, Filter<Shop> filter) throws BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if (username == null)
            throw new BlankDataExc("parameter is null: username");
        if (userController.isLogin(username)) {
            return ShopController.getInstance().getAllUserShops(username, filter);
        } else throw new InvalidAuthorizationException(String.format("user %s is not logged in", username));
    }

    public void registerForMessages(String username, UserObserver observer) throws IncorrectIdentification, BlankDataExc {
        if (username == null || username.isEmpty() || observer == null) {
            throw new BlankDataExc("Illegal parameters");
        }
        User user = userController.getUser(username);
        notificationManager.registerObserver(user, observer);
    }

    public void registerForAdminMessages(String username, AdminObserver observer) throws IncorrectIdentification, BlankDataExc {
        if (username == null || username.isEmpty() || observer == null) {
            throw new BlankDataExc("Illegal parameters");
        }
        User user = userController.getUser(username);
        notificationManager.registerAdminObserver(user, observer);
    }

    public void removeFromNotificationCenter(String username) {
        notificationManager.removeFromObserversList(username);
    }

    public void sendMessage(User addressee, User sender, String content) {
        notificationManager.sendMessage(addressee, content, sender);
    }

    public Integer getCurrentActiveUsers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getCurrentActiveUsers(username);
    }

    public Integer getCurrentActiveMembers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getCurrentActiveMembers(username);
    }

    public Integer getCurrentActiveGuests(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getCurrentActiveGuests(username);
    }

    public Integer getTotalMembers(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        if (username == null) {
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getTotalMembers(username);
    }

    public Map<Integer, User> getAllUsers(String username) throws BlankDataExc, IncorrectIdentification, InvalidAuthorizationException, InvalidSequenceOperationsExc {
        if (username == null) {
            throw new BlankDataExc("parameter is null: username");
        }
        isEnter(username);
        isExist(username);
        isLogin(username);
        return userController.getAllUsers(username);
    }

    public long getNumberOfUnreadMessages(String username) throws IncorrectIdentification, BlankDataExc {
        User user = getUser(username);
        return notificationManager.getNumberOfUnreadMessage(user);
    }


    public int addProductDiscount(String userName, int shopID, int prodID, double percentage) throws InvalidParamException, ShopNotFoundException, ProductNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addSimpleProductDiscount(userName,shopID, prodID, percentage);
    }

    public int addCategoryDiscount(String userName, int shopID, String category, double percentage) throws InvalidParamException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addSimpleCategoryDiscount(userName,shopID, category, percentage);
    }

    public int addShopAllProductsDiscount(String userName, int shopID, double percentage) throws InvalidParamException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addSimpleShopAllProductsDiscount(userName,shopID, percentage);
    }

    public int addConditionalProductDiscount(String userName, int shopID, int prodID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws InvalidParamException, AccessDeniedException, CriticalInvariantException, ShopNotFoundException, ProductNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addConditionalProductDiscount(userName,shopID, prodID, percentage, toBuildPredicatesFrom);
    }

    public int addConditionalCategoryDiscount(String userName, int shopID, String category, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws InvalidParamException, AccessDeniedException, CriticalInvariantException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addConditionalCategoryDiscount(userName,shopID, category, percentage, toBuildPredicatesFrom);
    }

    public int addConditionalShopAllProductsDiscount(String userName, int shopID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws InvalidParamException, AccessDeniedException, CriticalInvariantException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addConditionalShopAllProductsDiscount(userName,shopID, percentage, toBuildPredicatesFrom);
    }

    public int addProductPurchasePolicy(String userName, int shopID, int prodID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException, ProductNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addProductPurchasePolicy(userName,shopID, prodID, toBuildPredicatesFrom);
    }

    public int addCategoryPurchasePolicy(String userName, int shopID, String category, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addCategoryPurchasePolicy(userName,shopID, category, toBuildPredicatesFrom);
    }

    public int addShopAllProductsPurchasePolicy(String userName, int shopID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addShopAllProductsPurchasePolicy(userName,shopID, toBuildPredicatesFrom);
    }

    public int addOrDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws DiscountNotFoundException, CriticalInvariantException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addOrDiscount(userName,dis1ID, dis2ID, shopID);
    }

    public int addAndDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws DiscountNotFoundException, CriticalInvariantException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addAndDiscount(userName,dis1ID, dis2ID, shopID);
    }

    public int addXorDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws DiscountNotFoundException, CriticalInvariantException, ShopNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addXorDiscount(userName,dis1ID, dis2ID, shopID);
    }

    public int addOrPurchaseRule(String userName, int pr1ID, int pr2ID, int shopID) throws PurchaseRuleNotFoundException, CriticalInvariantException, ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addOrPurchaseRule(userName,pr1ID, pr2ID, shopID);
    }

    public int addAndPurchaseRule(String userName, int pr1ID, int pr2ID, int shopID) throws PurchaseRuleNotFoundException, CriticalInvariantException, ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.addAndPurchaseRule(userName,pr1ID, pr2ID, shopID);
    }

    public boolean removeDiscount(String userName, int discountID, int shopID) throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification {
        isEnter(userName);
        isExist(userName);
        isLogin(userName);
        return shopController.removeDiscount(userName,discountID, shopID);
    }



    public boolean removePurchaseRule(String userName, int purchaseRuleID, int shopID) throws ShopNotFoundException {
        return shopController.removePurchaseRule(userName,purchaseRuleID, shopID);
    }
}
