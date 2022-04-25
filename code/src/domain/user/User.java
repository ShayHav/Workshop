package domain.user;


import domain.ErrorLoggerSingleton;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;


public class User {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final String ca = "command approve";
    private String id;
    private UserState us;
    private List<Role> roleList;
    private Cart userCart;
    private boolean loggedIn;
    private List<ManagerAppointment> managerAppointeeList;
    private List<OwnerAppointment> ownerAppointmentList;
    private List<Order> orderHistory;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {
        us = null;
    }

    public User(String id){
        this.id = id;
        loggedIn = false;
    }

    /***
     * enter market - user state is now guest, with empty cart
     */
    public void enterMarket() {
        us = new Guest();
        userCart = new Cart();
    }

    /***
     * leave market - user has no state
     */
    public void leaveMarket(){
        us.leaveMarket(userCart);
        us = null;
    }

    public void closeShop(Shop shop) {
        if (roleList.contains(Role.ShopFounder))
            us.closeShop(shop, this.id);
        else errorLogger.logMsg(Level.WARNING, String.format("Not Founder shop try to close shop. user: %s", this.id));
    }

    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy) {
        roleList.add(Role.ShopFounder);
        us.createShop(name, discountPolicy, purchasePolicy, this.id);
    }

    public void appointOwner(User user, Shop shop) {
        if (roleList.contains(Role.ShopFounder) || roleList.contains(Role.ShopOwner))
            us.appointOwner(user, shop, this.id, ownerAppointmentList);
        else
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner withOut appropriate role by user: %s", id));
    }

    /***
     * login to the system
     */
    public void login() {
        us = new Member();
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
        //TODO: next session iml Cart DataBase
        loggedIn = false;
    }

    public String getId() {
        return this.id;
    }

    public boolean islog() {
        return this.loggedIn;
    }

    public List<String> checkout(String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        List<ResponseT<Order>> checkoutResult = us.checkout(id, userCart, fullName, address, phoneNumber, cardNumber, expirationDate);
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

    public List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f) {
        return us.getInfoOfShops(f);
    }


    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        return us.getInfoOfProductInShop(shopID);
    }


    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        return us.searchProductByName(name, f);
    }

    public List<ProductInfo> searchProductByCategory(String category, Filter<ProductInfo> f) {
        return us.searchProductByCategory(category, f);
    }

    public List<ProductInfo> searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        return us.searchProductByKeyword(keyword, f);
    }

    public void addRole(Role role) {
        if (!roleList.contains(role))
            roleList.add(role);
    }

    public List<OwnerAppointment> getOwnerAppointmentList() {
        return ownerAppointmentList;
    }

    public List<ManagerAppointment> getManagerAppointeeList() {
        return managerAppointeeList;
    }
}
