package domain.user;


import domain.ErrorLoggerSingleton;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.*;
import java.util.logging.Level;


public class User {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final String ca = "command approve";
    private String id;
    private UserState us;
    private Map<Integer,List<Role>> roleList;
    private Cart userCart;
    private boolean loggedIn;
    private List<ManagerAppointment> managerAppointeeList;
    private List<OwnerAppointment> ownerAppointmentList;
    private List<Order> orderHistory;
    private boolean isSystemManager;
    private boolean inMarket;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {
        us = null;
    }

    public User(String id) {
        this.id = id;
        loggedIn = false;
        isSystemManager = false;
        inMarket = false;
    }

    /***
     * enter market - user state is now guest, with empty cart
     */
    public void enterMarket() {
        us = new Guest();
        userCart = new Cart();
        inMarket = true;
    }


    /***
     * leave market - user has no state
     */
    public void leaveMarket() {
        us.leaveMarket(userCart);
        us = null;
        inMarket = false;
    }

    public void closeShop(String shop) {
        List<Role> useRoleList = roleList.get(shop);
        if (useRoleList!=null) {
            if(useRoleList.contains(Role.ShopFounder))
                us.closeShop(shop, this.id);
        }
        else errorLogger.logMsg(Level.WARNING, String.format("Not Founder shop try to close shop. user: %s", this.id));
    }

    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy) {
        us.createShop(name, discountPolicy, purchasePolicy, this.id);
        roleList.put(ShopController.getInstance().getShopCounter(),new LinkedList<>());
        roleList.get(name).add(Role.ShopFounder);
    }

    public void appointOwner(String userId, int shopName) {
        List<Role> useRolelist = roleList.get(shopName);
        if (useRolelist.contains(Role.ShopFounder) || useRolelist.contains(Role.ShopOwner))
            us.appointOwner(userId, shopName, this.id, ownerAppointmentList);
        else
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner withOut appropriate role by user: %s", id));
    }

    public void appointManager(String userId, int shopName){
        List<Role> useRolelist = roleList.get(shopName);
        if (useRolelist.contains(Role.ShopFounder) || useRolelist.contains(Role.ShopOwner))
            us.appointManager(userId, shopName, this.id, managerAppointeeList);
        else
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner withOut appropriate role by user: %s", id));
    }

    public List<Order> getHistoryOfOrders(){
        return orderHistory;
    }

    /***
     * login to the system
     */
    public void login() {
        if(isSystemManager) {
            us = new SystemManager();
        }
        else {
            us = new Member();
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

    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByName(name, f);
    }

    public List<ProductInfo>  searchProductByCategory(String category, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByCategory(category, f);
    }

    public List<ProductInfo>  searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByKeyword(keyword, f);
    }

    public Cart.CartInfo showCart() {
        return userCart.showCart();
    }

    public void addProductToCart(int shopID, int productID, int amount) {
        userCart.addProductToCart(shopID, productID, amount);
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
}
