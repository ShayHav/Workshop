package domain.user;


import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.*;
import java.util.logging.Level;


public class User {
    private static final String ca = "command approve";
    private String id;
    private UserState2 us;
    private Map<Integer,List<Role>> roleList;
    private Cart userCart;
    private boolean loggedIn;
    private List<ManagerAppointment> managerAppointeeList;
    private List<OwnerAppointment> ownerAppointmentList;
    private List<Order> orderHistory;
    private boolean isSystemManager;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {
        us = null;
    }

    public User(String id) {
        this.id = id;
        loggedIn = false;
        us = UserState2.disconnected;
        isSystemManager = false;
    }

    /***
     * enter market - user state is now guest, with empty cart
     */
    public void enterMarket() {
        us = UserState2.disconnected;
        userCart = new Cart();
    }


    /***
     * leave market - user has no state
     */
    public void leaveMarket() {
        us = null;
    }

    public void closeShop(int shopId) {
        List<Role> useRoleList = roleList.get(shopId);
        if (useRoleList!=null) {
            if(useRoleList.contains(Role.ShopFounder))
                founderCloseShop(shopId, this.id);
        }
        else errorLogger.logMsg(Level.WARNING, String.format("Not Founder shop try to close shop. user: %s", this.id));
    }

    private void founderCloseShop(int shop, String id) {
        Shop shop1;
        try{
            shop1 = MarketSystem.getInstance().getShop(shop);
        }catch (ShopNotFoundException snfe){
            errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
            return;
        }
        shop1.closeShop(id);
        if (!shop1.isOpen()) {
            eventLogger.logMsg(Level.INFO, String.format("close shop protocol shop id: %s", shop));
        } else {
            eventLogger.logMsg(Level.WARNING, String.format("attempt to close shop failed shop id: %s , user id:%s", shop, id));
        }
    }

    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy) {
        if(us == UserState2.member){
            memberCreateShop(name, discountPolicy, purchasePolicy, this.id);
            roleList.put(ShopController.getInstance().getShopCounter(),new LinkedList<>());
            roleList.get(name).add(Role.ShopFounder);
        }
        else
            errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
    }

    private int memberCreateShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        return MarketSystem.getInstance().createShop(name, discountPolicy, purchasePolicy, id);
    }



    public void appointOwner(String userId, int shopName) {
        List<Role> useRolelist = roleList.get(shopName);
        if (useRolelist.contains(Role.ShopFounder) || useRolelist.contains(Role.ShopOwner))
            memberAppointOwner(userId, shopName, this.id, ownerAppointmentList);
        else
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner with out appropriate role by user: %s", id));
    }


    private boolean memberAppointOwner(String targetUser, int shop, String id, List<OwnerAppointment> ownerAppointmentList) {
        if(us == UserState2.member){
            Shop shop1;
            try{
                shop1 = getShop(shop);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
                return false;
            }
            User user = getUser(targetUser);
            if (shop1.isFounder(id) || shop1.isOwner(id)) {
                user.addRole(shop1.getShopID(), Role.ShopOwner);
                shop1.AppointNewShopOwner(targetUser, id);
                if (isAppointedMeOwner(user, id)) {
                    OwnerAppointment newAppointment = new OwnerAppointment(shop1, id, user);
                    ownerAppointmentList.add(newAppointment);
                    eventLogger.logMsg(Level.INFO, String.format("appointOwner = {appointeeId: %s , appointedId: %s , ShopId %s}", id, user.getId(), shop));
                    return true;
                }
                else
                    errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner without permissions = {appointeeId: %s , appointedId: %s , ShopId %s}", id, targetUser, shop));
            }
            else
                errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner without permissions = {appointeeId: %s , appointedId: %s , ShopId %s}", id, targetUser, shop));
            return false;
        }
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return false;
    }

    private boolean isAppointedMeOwner(User user, String id) {
        List<OwnerAppointment> Appointmentusers = user.getOwnerAppointmentList();
        for (OwnerAppointment run : Appointmentusers) {
            if (run.getAppointed().getId().equals(id))
                return true;
        }
        return false;
    }

    public void appointManager(String userId, int shopName){
        List<Role> useRolelist = roleList.get(shopName);
        if (useRolelist.contains(Role.ShopFounder) || useRolelist.contains(Role.ShopOwner))
            memberAppointManager(userId, shopName, this.id, managerAppointeeList);
        else
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner withOut appropriate role by user: %s", id));
    }


    public boolean memberAppointManager(String targetUser, int shop, String id, List<ManagerAppointment> managerAppointmentList) {
        Shop shop1;
        try{
            shop1 = getShop(shop);
        }catch (ShopNotFoundException snfe){
            errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
            return false;
        }
        User user1 = getUser(targetUser);
        synchronized (this) {
            if (shop1.isOwner(id)) {
                user1.addRole(shop, Role.ShopManager);
                shop1.AppointNewShopManager(targetUser, id);
                if (isAppointedMeManager(user1, id)) {
                    ManagerAppointment newAppointment = new ManagerAppointment(shop1, id, user1);
                    managerAppointmentList.add(newAppointment);
                    eventLogger.logMsg(Level.INFO, String.format("appointManager = {appointeeId: %s , appointedId: %s , ShopId %s}", id, targetUser, shop));
                    return true;
                } else
                    errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}", id, targetUser, shop));
            } else
                errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %s , appointedId: %s , ShopId %d}", id, targetUser, shop));
        }
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return false;
    }

    private boolean isAppointedMeManager(User user, String id) {
        List<ManagerAppointment> Appointmentusers = user.getManagerAppointeeList();
        for (ManagerAppointment run : Appointmentusers) {
            if (run.getAppointed().getId().equals(id))
                return true;
        }
        return false;
    }

    private User getUser(String targetUser) {
        return MarketSystem.getInstance().getUser(targetUser);
    }

    private Shop getShop(int shop) throws ShopNotFoundException {
        return MarketSystem.getInstance().getShop(shop);
    }

    public List<Order> getHistoryOfOrders(){
        return orderHistory;
    }

    /***
     * login to the system
     */
    public void login() {
        //todo: checks if is actual user????
        if(isSystemManager) {
            us = UserState2.member;
        }
        else {
            us = UserState2.member;
        }
        if(roleList == null)
            roleList = new HashMap<>();
        if (ownerAppointmentList == null)
            ownerAppointmentList = new ArrayList<>();
        if (managerAppointeeList == null)
            managerAppointeeList = new ArrayList<>();
        if (orderHistory == null)
            orderHistory = new ArrayList<>();
        loggedIn = true;
    }

    /***
     *  logout from the system
     */
    public void logout() {
        //TODO: save the user cart
        loggedIn = false;
    }

    public String getId() {
        return this.id;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public List<String> checkout(String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        List<ResponseT<Order>> checkoutResult = userCart.checkout(id, fullName, address, phoneNumber, cardNumber, expirationDate);
        List<String> errors = new ArrayList<>();
        for (ResponseT<Order> r : checkoutResult) {
            if (r.isErrorOccurred()) {
                errors.add(r.errorMessage);
            } else {
                orderHistory.add(r.getValue());
            }
        }
        return errors;
    }


    public void addRole(int shop,Role role) {
        List<Role> useRoleList = roleList.get(shop);
        if(useRoleList!=null)
            if(useRoleList.contains(role))
                useRoleList.add(role);
    }

    public List<OwnerAppointment> getOwnerAppointmentList() {
        return ownerAppointmentList;
    }

    public List<ManagerAppointment> getManagerAppointeeList() {
        return managerAppointeeList;
    }

    public Cart.CartInfo showCart() {
        return userCart.showCart();
    }

    public boolean addProductToCart(int shopID, int productID, int amount) {
        return userCart.addProductToCart(shopID, productID, amount);
    }

    public boolean updateAmountOfProduct(int shopID, int productID, int amount) {
        return userCart.updateAmountOfProduct(shopID, productID, amount);
    }

    public boolean removeProductFromCart(int shopID, int productID) {
        return userCart.removeProductFromCart(shopID, productID);
    }

    public void addManagerPermissions(String targetUser,String shop,String userId,List<ShopManagersPermissions> shopManagersPermissionsList) {

    }

    public Map<Integer, List<Role>> getRoleList() {
        return roleList;
    }

    public void makeSystemManager() {
        isSystemManager = true;
    }

    public UserSearchInfo getUserInfo(){
        return new UserSearchInfo(id);
    }

    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) throws InvalidAuthorizationException {
        if(isSystemManager)
            return systemManagerGetOrderHistoryForShops(f,shopID);
        else {
            errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
            throw new InvalidAuthorizationException("SystemManager", us.toString());
        }
    }

    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String>  userID) throws InvalidAuthorizationException {
        if(isSystemManager)
            return systemManagerGetOrderHistoryForUser(f,userID);
        else {
            errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
            throw new InvalidAuthorizationException("SystemManager", us.toString());
        }
    }


    public List<Order> systemManagerGetOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) {
        ShopController sc = ShopController.getInstance();
        List<Order> result = sc.getOrderHistoryForShops(shopID);
        return f.applyFilter(result);
    }

    public List<Order> systemManagerGetOrderHistoryForUser(Filter<Order> f, List<String>  userID){
        UserController uc = UserController.getInstance();
        List<Order> result = uc.getOrderHistoryForUser(userID);
        return f.applyFilter(result);
    }


    /**
     * @param targetUser
     * @param shop
     * @param userId
     * @param shopManagersPermissionsList
     * @return
     */
    public boolean addManagerPermissions(String targetUser, int shop, String userId, List<ShopManagersPermissions> shopManagersPermissionsList) {
        synchronized (this) {
            Shop shop1;
            try{
                shop1 = getShop(shop);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
                return false;
            }
            User user = MarketSystem.getInstance().getUser(targetUser);
            return shop1.addPermissions(shopManagersPermissionsList, targetUser, userId);
        }
    }

    public boolean removeManagerPermissions(String targetUser, int shop, String userId, List<ShopManagersPermissions> shopManagersPermissionsList) {
        synchronized (this) {
            Shop shop1;
            try{
                shop1 = getShop(shop);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
                return false;
            }
            User user = MarketSystem.getInstance().getUser(targetUser);
            return shop1.removePermissions(shopManagersPermissionsList, targetUser, userId);
        }
    }

    public boolean saveCart(Cart cart) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    public void requestInfoOnOfficials(Filter f) {
        throw new UnsupportedOperationException();
    }

}


