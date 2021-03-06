package domain.user;


import domain.*;
import domain.Exceptions.*;
import domain.Responses.Response;
import domain.Responses.ResponseT;
import domain.shop.*;
import domain.user.EntranceLogger.Entrance;
import domain.user.EntranceLogger.EntranceLogger;
import domain.user.filter.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;

public class User {
    private String userName;
    private UserState2 us;
    private Map<Integer,List<Role>> roleList;
    @OneToOne
    private Cart userCart;
    private boolean loggedIn;
    private List<ManagerAppointment> managerAppointeeList;
    private List<OwnerAppointment> ownerAppointmentList;
    private List<Order> orderHistory;
    private boolean isSystemManager;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private boolean enteredMarket;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {
        us = null;
    }

    public User(String userName) {
        this.userName = userName;
        loggedIn = false;
        us = UserState2.disconnected;
        userCart = new Cart();
        isSystemManager = false;
        managerAppointeeList = new ArrayList<>();
        ownerAppointmentList = new ArrayList<>();
        orderHistory = new ArrayList<>();
        roleList = new HashMap<>();
    }

    private UserState2 getUserState(){
        return us;
    }

    public boolean isEnteredMarket() {
        return enteredMarket;
    }

    public void setEnteredMarket(boolean enteredMarket){
        this.enteredMarket = enteredMarket;
    }

    public void setSystemManager(boolean systemManager) {
        isSystemManager = systemManager;
    }

    public boolean isSystemManager() {
        return isSystemManager;
    }

    private ArrayList<String> systemManagerActions(){
        ArrayList<String> systemManagerActions = new ArrayList<>();
        systemManagerActions.add("get order history of a user");
        systemManagerActions.add("get order history of a shop");
        return systemManagerActions;
    }

    private ArrayList<String> memberActions(){
        ArrayList<String> memberActions = new ArrayList<>();
        memberActions.add("create a new shop");
        return memberActions;
    }

    private ArrayList<String> guestActions(){
        ArrayList<String> guestActions = new ArrayList<>();
        guestActions.add("my order history");
        guestActions.add("show cart");
        guestActions.add("change amount of product in cart");
        guestActions.add("remove product from cart");
        guestActions.add("checkout");
        guestActions.add("leave marketplace");
        return guestActions;
    }

    private ArrayList<String> disconnectedActions(){
        ArrayList<String> guestActions = new ArrayList<>();
        guestActions.add("enter marketplace");
        return guestActions;
    }


    /***
     * enter market - user state is now guest, with empty cart
     */
    public void enterMarket() {
        us = UserState2.guest;
        enteredMarket=true;
    }


    /***
     * leave market - user has no state
     */
    public void leaveMarket() {
        us = null;
        enteredMarket=false;
    }

    private boolean isAppointedMeOwner(String id) {
        List<OwnerAppointment> Appointmentusers = ownerAppointmentList;
        for (OwnerAppointment run : Appointmentusers) {
            if (run.getAppointed().getUserName().equals(id))
                return true;
        }
        return false;
    }

    //TODO: why there is a difference
    public void AppointedMeOwner(Shop s,String id) throws IncorrectIdentification, BlankDataExc {
        ownerAppointmentList.add(new OwnerAppointment(s,userName,ControllersBridge.getInstance().getUser(id)));
    }
    public void AppointedMeManager(Shop s,String id) throws IncorrectIdentification, BlankDataExc {
        managerAppointeeList.add(new ManagerAppointment(s,this,ControllersBridge.getInstance().getUser(id)));
    }

    public boolean appointManager(int shopName) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        List<Role> userolelist = roleList.get(shopName);
        if(userolelist==null)
            throw new InvalidSequenceOperationsExc();
        if ((userolelist.contains(Role.ShopFounder) || userolelist.contains(Role.ShopOwner)) && us == UserState2.member) {
            return true;
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager withOut appropriate role by user: %s", userName));
            throw new InvalidSequenceOperationsExc();
        }
    }
    public boolean appointOwner(int shopName) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        List<Role> useRolelist = roleList.get(shopName);
        if(useRolelist==null)
            throw new InvalidSequenceOperationsExc();
        if ((useRolelist.contains(Role.ShopFounder) || useRolelist.contains(Role.ShopOwner)) && us == UserState2.member) {
            return true;
        }
        else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to appointOwner withOut appropriate role by user: %s", userName));
            throw new InvalidSequenceOperationsExc();
        }
    }


   /* private void memberAppointManager(String targetUser, int shop, String id, List<ManagerAppointment> managerAppointmentList) throws IncorrectIdentification, BlankDataExc {
        Shop shop1;
        try{
            shop1 = getShop(shop);
        }catch (ShopNotFoundException snfe){
            errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
            return;
        }
        User user1 = getUser(targetUser);
        synchronized (this) {
            if (shop1.isOwner(id)) {

                shop1.AppointNewShopManager(targetUser, id);
                if (isAppointedMeManager(user1, id)) {
                    ManagerAppointment newAppointment = new ManagerAppointment(shop1, id, user1);
                    managerAppointmentList.add(newAppointment);
                    eventLogger.logMsg(Level.INFO, String.format("appointManager = {appointeeId: %s , appointedId: %s , ShopId %s}", id, targetUser, shop));
                    //return true;
                } else
                    errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}", id, targetUser, shop));
            } else
                errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %s , appointedId: %s , ShopId %d}", id, targetUser, shop));
        }
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        //return false;
    }
    */

    private boolean isAppointedMeManager(User user, String id) {
        List<ManagerAppointment> Appointmentusers = user.getManagerAppointeeList();
        for (ManagerAppointment run : Appointmentusers) {
            if (run.getAppointed().getUserName().equals(id))
                return true;
        }
        return false;
    }

    private User getUser(String targetUser) throws IncorrectIdentification, BlankDataExc {
        return ControllersBridge.getInstance().getUser(targetUser); //TODO: new class
    }

    private Shop getShop(int shop) throws ShopNotFoundException {
        return ControllersBridge.getInstance().getShop(shop); //TODO: new class
    }


    public List<Order> getOrderHistory(){
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    /***
     * login to the system
     */
    public void login() {
        if(isSystemManager) {
            us = UserState2.systemManager;
        }
        else {
            us = UserState2.member;
        }
        loggedIn = true;
    }

    /***
     *  logout from the system
     */
    public void logout() {
        //TODO: save the user cart
        us = UserState2.disconnected;
        loggedIn = false;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }


    public List<String> checkout(String fullName, String address, String city, String country, String zip, String phoneNumber, String cardNumber, String ccv, String expirationDate) throws BlankDataExc {
        List<ResponseT<Order>> checkoutResult = userCart.checkout(userName, fullName, address, city, country, zip, phoneNumber, cardNumber, ccv, expirationDate);
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


    public void addRole(int shop,Role role) throws InvalidSequenceOperationsExc {
        if (roleList == null) {
            roleList = new HashMap<>();
        }
        List<Role> useRoleList = roleList.get(shop);
        if (useRoleList != null) {
            if (!useRoleList.contains(role)) {
                useRoleList.add(role);
            }
            else throw new InvalidSequenceOperationsExc("Trying to give a role that already exists");
        }
        else {
            useRoleList = new ArrayList<>();
            useRoleList.add(role);
            roleList.put(shop, useRoleList);
        }
    }

    public List<OwnerAppointment> getOwnerAppointmentList() {
        return ownerAppointmentList;
    }

    public List<ManagerAppointment> getManagerAppointeeList() {
        return managerAppointeeList;
    }

    public Cart.ServiceCart showCart() {
        return userCart.showCart();
    }

    public Response addProductToCart(int shopID, int productID, int amount) throws ShopNotFoundException {
        return userCart.addProductToCart(shopID, productID, amount);
    }


    public ResponseT<Integer> addNewBid(int shopID, int productID, int amount, double price) throws ShopNotFoundException {
        if(getUserState().equals(UserState2.guest))
            return new ResponseT<>("guests may not submit bids");
        return userCart.addNewBidToCart(shopID, productID, amount, this, price);
    }

    public void bidApproved(int shopID, int bidID) throws BidNotFoundException, CriticalInvariantException {
        userCart.bidApproved(shopID, bidID);
    }

    public void removeBid(int shopID, int bidID) throws BidNotFoundException, CriticalInvariantException {
        userCart.removeBid(shopID, bidID);
        userCart.getTotalAmount();
    }



    public Response updateAmountOfProduct(int shopID, int productID, int amount) {
        return userCart.updateAmountOfProduct(shopID, productID, amount);
    }

    public Response removeProductFromCart(int shopID, int productID) {
        return userCart.removeProductFromCart(shopID, productID);
    }

    /*public void addManagerPermissions(String targetUser,String shop,String userId,List<ShopManagersPermissions> shopManagersPermissionsList) {

    }
     */

    public Map<Integer, List<Role>> getRoleList() {
        return roleList;
    }

    public void makeSystemManager() {
        isSystemManager = true;
    }

    public UserSearchInfo getUserInfo(){
        return new UserSearchInfo(userName);
    }

    public Map<Shop, List<Order>> getOrderHistoryForShops(Filter<Order> f) throws InvalidAuthorizationException, ShopNotFoundException {
        if(isSystemManager && us == UserState2.systemManager)
            return systemManagerGetOrderHistoryForShops(f);
        else {
            errorLogger.logMsg(Level.WARNING,"only system manager is allowed to perform this action");
            throw new InvalidAuthorizationException("SystemManager", us.toString());
        }
    }

    public Map<User, List<Order>> getOrderHistoryForUsers(Filter<Order> f) throws InvalidAuthorizationException {
        if(isSystemManager && us == UserState2.systemManager)
            return systemManagerGetOrderHistoryForUsers(f);
        else {
            errorLogger.logMsg(Level.WARNING,"only system manager is allowed to perform this action");
            throw new InvalidAuthorizationException("SystemManager", us.toString());
        }
    }

    public List<Entrance> getEntrances(LocalDate from, LocalDate to) throws InvalidAuthorizationException {
        if(isSystemManager && us == UserState2.systemManager)
            return EntranceLogger.getInstance().getEntrances(from,to);
        else {
            errorLogger.logMsg(Level.WARNING,"only system manager is allowed to perform this action");
            throw new InvalidAuthorizationException("SystemManager", us.toString());
        }
    }


    private Map<Shop, List<Order>> systemManagerGetOrderHistoryForShops(Filter<Order> f) throws ShopNotFoundException {
        ControllersBridge cb = ControllersBridge.getInstance();
        return cb.getOrderHistoryForShops(f);
    }

    private Map<User, List<Order>> systemManagerGetOrderHistoryForUsers(Filter<Order> f) throws InvalidAuthorizationException {
        UserController uc = UserController.getInstance();
        return uc.getOrderHistoryForUsers(f);

    }


    /**
     * @param targetUser
     * @param shop
     * @param userId
     * @param shopManagersPermissionsList
     * @return
     */
    public boolean addManagerPermissions(String targetUser, int shop, String userId, List<ShopManagersPermissions> shopManagersPermissionsList) throws IncorrectIdentification, BlankDataExc {
        synchronized (this) {
            Shop shop1;
            try{
                shop1 = getShop(shop);
            }catch (ShopNotFoundException snfe){
                errorLogger.logMsg(Level.WARNING ,String.format("Shop: %d does not exist", shop));
                return false;
            }
            //User user = MarketSystem.getInstance().getUser(targetUser);  //TODO: new class
            return shop1.addPermissions(shopManagersPermissionsList, targetUser, userId);
        }
    }


    /**
     * Checks whether the perpetrator may perform it and operate
     * @param targetUser
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean DismissalUser(String targetUser) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, ShopNotFoundException {
        if(isSystemManager & loggedIn){
            ControllersBridge.getInstance().DismissalUser(targetUser);
            eventLogger.logMsg(Level.INFO,String.format("user has been dismiss: %s",targetUser));
            return true;
        }
        errorLogger.logMsg(Level.WARNING,String.format("attempt to dismiss user by not system manager: %s",targetUser));
        throw new InvalidSequenceOperationsExc("");
    }

    /**
     * Checks whether the perpetrator may perform it and operate
     * @param targetUser
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean CanDismissalOwner(String targetUser) throws InvalidSequenceOperationsExc {
        if(loggedIn){
            if(isAppointedMeOwner(targetUser)) {
                return true;
            }
            else {
                errorLogger.logMsg(Level.WARNING, String.format("attempt to dismiss user by not Appointed user: %s", userName));
                throw new InvalidSequenceOperationsExc(String.format("attempt to dismiss user by not Appointed user: %s", userName));
            }
        }
        errorLogger.logMsg(Level.WARNING,String.format("attempt to dismiss user by not loggedIn user: %s",userName));
        throw new InvalidSequenceOperationsExc(String.format("attempt to dismiss user by not loggedIn user: %s",userName));
    }

    public void DismissalOwner(String targetUser, int shop) throws InvalidSequenceOperationsExc, ShopNotFoundException, IncorrectIdentification, BlankDataExc {
        ControllersBridge.getInstance().DismissalOwner(userName, targetUser, shop);
        eventLogger.logMsg(Level.INFO, String.format("user has been dismiss: %s", targetUser));
    }

    public void removeRole(Role shopOwner, int shopID) {
        roleList.get(shopID).remove(shopOwner);
    }

    public UserState2 getUs() {
        return us;
    }


}


