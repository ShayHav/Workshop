package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.TransactionInfo;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class MarketSystem {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static MarketSystem instance = null;
    private ExternalConnector externalConnector;
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
    public boolean start(PaymentService payment, SupplyService supply, String userID, String password) throws InvalidSequenceOperationsExc {
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

    public List<ShopInfo> getInfoOfShops(String userID, Filter<ShopInfo> f) throws BlankDataExc {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            throw new BlankDataExc();
        return ShopController.getInstance().getInfoOfShops(f);

    }


    //TODO: f==null is ok? getAllInfoProduct..
    public List<ProductInfo> getInfoOfProductInShop(String userID, int shopID, Filter<ProductInfo> f) {
        if (userID == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().getInfoOfProductInShop(shopID, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public List<ProductInfo> searchProductByName(String userID, String name, Filter<ProductInfo> f) {
        if (userID == null || name == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().searchProductByName(name, f);
    }
    //TODO: f==null is ok? getAllInfoProduct..
    public List<ProductInfo> searchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        if (userID == null || keyword == null || f == null || !userController.HasUserEnteredMarket(userID))
            return null;
        return ShopController.getInstance().searchProductByKeyword(keyword, f);
    }

    //TODO: need to get inside //not in use
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if(id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    public Shop createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String foundId) throws BlankDataExc, IncorrectIdentification {
        if(name == null )
            throw new BlankDataExc("name");
        if(discountPolicy == null )
            throw new BlankDataExc("discountPolicy");
        if(foundId == null )
            throw new BlankDataExc("foundId");
        if(purchasePolicy == null )
            throw new BlankDataExc("purchasePolicy");
        return ShopController.getInstance().createShop(name, discountPolicy, purchasePolicy, foundId);
    }

    public boolean register(String username, String pw) throws BlankDataExc, InvalidSequenceOperationsExc {
        if(username == null )
            throw new BlankDataExc("username");
        if(pw == null)
            throw new BlankDataExc("pw");
        //System.out.println("Line 133");
        return UserController.getInstance().register(username, pw);
    }

    public boolean deleteUserTest(String[] username) throws InvalidSequenceOperationsExc {
        for(String user: username){
            if(user == null)
                return false;
        }
        return UserController.getInstance().deleteUserTest(username);
    }
    public boolean deleteUser(String username) throws BlankDataExc, InvalidSequenceOperationsExc {
            if(username == null)
                throw new BlankDataExc();
        return UserController.getInstance().deleteUserName(username);
    }
    //TODO: need to get inside  //TODO: not in use
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }


    //TODO: Services start here :)
    public User logIn(String username, String pw) throws InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        if(username == null )
            throw new BlankDataExc("username");
        if(pw == null)
            throw new BlankDataExc("pw");
        return UserController.getInstance().logIn(username, pw);
    }

    public User LeaveMarket(String s) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if(s!=null & !s.isEmpty()){
            return userController.logOut(s);
        }
        return null;
    }

    public User logout(String username) throws BlankDataExc, IncorrectIdentification, InvalidSequenceOperationsExc {
        if(username == null){
            throw new BlankDataExc("username");
        }
        return UserController.getInstance().logOut(username);
    }

    public int RemoveProductFromShopInventory(int productId, String userID, int shopname) throws InvalidAuthorizationException, IncorrectIdentification, BlankDataExc {
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveProductFromShopInventory(productId, userID, shopname);
        return -1;
    }

    public String CloseShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().closeShop(shopId, userID);
        return null;
    }
    public String OpenShop(int shopId, String userID) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().openShop(shopId, userID);
        return null;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc {
        if(shopManagersPermissionsList == null)
            throw new BlankDataExc("shopManagersPermissionsList");
        if(targetUser == null)
            throw new BlankDataExc("targetUser");
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().RemoveShopManagerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc {
        if(shopManagersPermissionsList == null)
            throw new BlankDataExc("shopManagersPermissionsList");
        if(targetUser == null)
            throw new BlankDataExc("targetUser");
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, userID);
        return null;
    }

    public String AppointNewShopManager(int key, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc {
        if(targetUser == null)
            throw new BlankDataExc("targetUser");
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopManager(key, targetUser, userID);
        return null;
    }

    public String AppointNewShopOwner(int key, String targetUser, String userID) throws IncorrectIdentification, BlankDataExc {
        if(targetUser == null )
            throw new BlankDataExc("targetUser");
        if(userID == null)
            throw new BlankDataExc("userID");
        if(userController.isLogin(userID))
            return ShopController.getInstance().AppointNewShopOwner(key, targetUser, userID);
        return null;
    }

    public List<String> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        if (userID == null)
            throw new BlankDataExc("userID");
        if (fullName == null)
            throw new BlankDataExc("fullName");
        if (address == null)
            throw new BlankDataExc("address");
        if (cardNumber == null)
            throw new BlankDataExc("cardNumber");
        if (phoneNumber == null)
            throw new BlankDataExc("phoneNumber");
        if (expirationDate == null)
            throw new BlankDataExc("expirationDate");
        return userController.checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    public void setExternalConnector(ExternalConnector ec) {
        externalConnector = ec;
    }

    public List<UserSearchInfo> RequestShopOfficialsInfo(int shopID, SearchOfficialsFilter f, String userID) throws IncorrectIdentification {
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

    public List<Order> RequestInformationOfShopsSalesHistory(int shopID, SearchOrderFilter f, String userID) throws IncorrectIdentification {
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
        else
            return null;
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

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID) throws InvalidAuthorizationException, IncorrectIdentification {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForShops(userID, f, shopID);
        else return null;
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String> userIDs) throws InvalidAuthorizationException, IncorrectIdentification {
        if(userController.isLogin(userID))
            return userController.getOrderHistoryForUser(userID, f, userIDs);
        else return null;
    }
}
