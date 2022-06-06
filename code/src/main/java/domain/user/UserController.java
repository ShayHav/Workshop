package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.Response;
import domain.shop.Order;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.user.filter.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class UserController {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static final SecurePasswordStorage securePasswordStorage = SecurePasswordStorage.getSecurePasswordStorage_singleton();
    private Map<String, User> memberList; //TODO: At a later stage there will be a list of Thread by users
    private Map<String, User> activeUser; //TODO: temporary
    private Map<String, User> guestUser;
    private static UserController instance = null;
    private List<User> adminUser;
    private int guestCounter = 0;

    private UserController() {
        memberList = new HashMap<>();
        activeUser = new HashMap<>();
        adminUser = new LinkedList<>();
        guestUser = new HashMap<>();
    }

    public boolean canBeDismiss(String targetUser) throws IncorrectIdentification {
        AtomicBoolean output = new AtomicBoolean(true);
        User u =getUser(targetUser);
        if(u.isSystemManager())
            return false;
        u.getRoleList().values().forEach((r) -> {
            if(r.contains(Role.ShopFounder) | r.contains(Role.ShopOwner) | r.contains(Role.ShopManager))
                output.set(false);
        });
        return output.get();
    }

    public boolean DismissalOwner(String usernames, String targetUser, int shop) throws IncorrectIdentification, InvalidSequenceOperationsExc, ShopNotFoundException, BlankDataExc {
        User u1 = getUser(usernames);
        User u2 = getUser(targetUser);
        if(u1.CanDismissalOwner(targetUser)){
            List<OwnerAppointment> run = u2.getOwnerAppointmentList();
            if(run !=null) {
                run.forEach((o) -> {
                    try {
                        DismissalOwner(targetUser, o.getAppointed().getUserName(), o.getShop().getShopID());
                    } catch (IncorrectIdentification e) {
                        e.printStackTrace();
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (ShopNotFoundException | BlankDataExc e) {
                        e.printStackTrace();
                    }
                });
            }
            u1.DismissalOwner(targetUser,shop);
            return true;
        }
        return false;
    }

    public boolean userExist(String userName) {
        return memberList.containsKey(userName) | guestUser.containsKey(userName);
    }



    private static class UserControllerHolder {
        private static final UserController uc = new UserController();
    }

    public static UserController getInstance() {
        return UserControllerHolder.uc;
    }

    //TODO: add logger and validate pre condition

    /***
     * login to the market system
     * pre-condition - user is registered to the system, not yet logged in
     * @param username the unique identifier of the user
     * @param pass password given by the user
     */
    public User login(String guestUsername, String username, String pass) throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        User output;
        if (isUserisLog(username)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for %s failed. user is already logged in", username));
            throw new InvalidSequenceOperationsExc("attempt of logIn for logged in user");
        } else if (memberList.get(username) != null) {
            if (securePasswordStorage.passwordCheck(username, pass)) {
                synchronized (activeUser) {
                    activeUser.put(username, memberList.get(username));
                    output = getUser(username);
                    output.login();

                    //remove the old instance of guest we gave to the user.
                    activeUser.remove(guestUsername);
                    synchronized (guestUser) {
                        guestUser.remove(guestUsername);
                    }
                }
                eventLogger.logMsg(Level.INFO, String.format("logIn for user: %s.", username));
                return output;
            } else {
                throw new InvalidAuthorizationException("Identifier not correct");
            }
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for unregistered user with id: %s.", username));
            throw new InvalidAuthorizationException();
        }
    }

    public Cart.ServiceCart showCart(String username) {
        User u = memberList.get(username);
        if (u == null) {
            u = guestUser.get(username);
        }
        return u.showCart();
    }


    private synchronized boolean isUserisLog(String id) {
        return activeUser.containsKey(id);
    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency

    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public User logout(String userName) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if (activeUser != null) {
            if (activeUser.containsKey(userName)) {
                synchronized (activeUser) {
                    User u = getUser(userName);
                    if(u!=null) {
                        u.logout();
                        activeUser.remove(userName);
                    }
                    else {
                        errorLogger.logMsg(Level.WARNING, "attempt of logOut for user who is not Registered.");
                        throw new InvalidSequenceOperationsExc("attempt of logOut for user who is not Registered.");
                    }
                }
                eventLogger.logMsg(Level.INFO, String.format("logOut for user: %s.", userName));
            }
        } else {
            errorLogger.logMsg(Level.WARNING, "attempt of logOut for user who is not logged in.");
            throw new InvalidSequenceOperationsExc("attempt of logOut for user who is not logged in.");
        }
        return enterMarket();
    }

    /***
     * register to the system
     * pre-condition - user is not registered
     * @param id the unique identifier of the user
     * @param pass password given by the user
     */
    public boolean register(String id, String pass) throws InvalidSequenceOperationsExc {
        if (!memberList.containsKey(id)) {
            User user = new User(id);
            synchronized (memberList) {
                memberList.put(id, user);
            }
            SecurePasswordStorage.getSecurePasswordStorage_singleton().inRole(id, pass);
            eventLogger.logMsg(Level.INFO, String.format("Registered for user: %s.", id));
            return true;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of registered for exist id %s failed.", id));
            throw new InvalidSequenceOperationsExc("attempt of registered for exist id");
        }
    }

    /***
     * enter to the market - active user is now a guest
     */
    public User enterMarket() {
        User temp = new User(String.format("-Guest%d", guestCounter));
        guestCounter++;
        temp.enterMarket();
        activeUser.put(temp.getUserName(), temp);
        eventLogger.logMsg(Level.INFO, "User entered Market.");
        guestUser.put(temp.getUserName(), temp);
        return temp;
    }

    public User leaveMarket(String username) throws IncorrectIdentification {
        User u = getUser(username);
        //if the user is member and wants to leave market before logout
        if(u.isLoggedIn()){
            u.logout();
            u.leaveMarket();
            activeUser.remove(username);
        }
        //else, the user is guest, and we should remove him from guest list.
        else{
            u.leaveMarket();
            activeUser.remove(username);
            guestUser.remove(username);
        }
        return u;
    }


    public User getUser(String id) throws IncorrectIdentification {
        if (id == null)
            throw new IncorrectIdentification("id not exist");
        User u = memberList.getOrDefault(id, null);
        if (u == null) {
            u = guestUser.get(id);
        }
        return u;
    }

    public boolean deleteUserTest(String[] userName) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        for (int i = 0; i < userName.length; i++) {
            deleteUser(userName[i]);
        }
        return true;
    }

    public boolean deleteUserName(String s) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if (!memberList.containsKey(s)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to delete not exist user: %s", s));
            throw new InvalidSequenceOperationsExc(String.format("attempt to delete not exist user: %s", s));
        }
        deleteUser(s);
        return !memberList.containsKey(s);
    }

    /**
     * for each user that we delete we need to close this shop (Founder of shop)
     *
     * @param useID
     * @throws InvalidSequenceOperationsExc
     */
    private void deleteUser(String useID) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        User u = memberList.get(useID);
        if (u != null) {
            Map<Integer, List<Role>> useRoleList = u.getRoleList();
            for (Map.Entry<Integer, List<Role>> run : useRoleList.entrySet()) {
                for (Role runn : run.getValue())
                    if (runn == Role.ShopFounder)
                        ShopController.getInstance().closeShop(run.getKey(), useID);
            }
            memberList.remove(useID);
        }
    }

    public List<String> checkout(String userName, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        User user = getUser(userName);
        return user.checkout(fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    /**
     * Create system manager
     *
     * @param id   - user identifier
     * @param pass - given password
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean createSystemManager(String id, String pass) throws InvalidSequenceOperationsExc {
        System.out.println("createSystemManager: "+id);
        register(id,pass);
        System.out.println("register: "+id);
        User u = memberList.get(id);
        synchronized (adminUser) {
            adminUser.add(u);
        }
        System.out.println("makeSystemManager: "+id);
        u.makeSystemManager();
        return true;
    }

    public Map<User, List<Order>> getOrderHistoryForUsers(Filter<Order> f) {
        Map<User, List<Order>> orders = new HashMap<>();
        synchronized (memberList) {
            for (User user : memberList.values()) {
                List<Order> userOrders = user.getOrderHistory();
                if(userOrders.size() > 0)
                    orders.put(user,f.applyFilter(userOrders));
            }
        }
        else{
            for(String id: userName){
                User user = memberList.get(id);
                if(user == null){
                  errorLogger.logMsg(Level.WARNING,String.format("user not exist: %s",id));
                  return null;
                }
                orders.addAll(user.getHistoryOfOrders());
            }
        }
        return orders;
    }

    public synchronized boolean HasUserEnteredMarket(String userName) {
        return activeUser.containsKey(userName);
    }

    public Response addProductToCart(String userName, int shopNumber, int productId, int amount) throws
            InvalidSequenceOperationsExc, ShopNotFoundException {
        if (!HasUserEnteredMarket(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userName);
        return u.addProductToCart(shopNumber, productId, amount);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param shopID
     * @param productId
     * @param amount
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public Response updateAmountOfProduct(String userName, int shopID, int productId, int amount) throws
            InvalidSequenceOperationsExc {
        if (!HasUserEnteredMarket(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userName);
        return u.updateAmountOfProduct(shopID, productId, amount);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param shopNumber
     * @param productId
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public Response removeProductFromCart(String userName, int shopNumber, int productId) throws
            InvalidSequenceOperationsExc {
        if (!HasUserEnteredMarket(userName))
            throw new InvalidSequenceOperationsExc();
        User u = activeUser.get(userName);
        return u.removeProductFromCart(shopNumber, productId);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param f
     * @param shopNumber
     * @return
     * @throws InvalidAuthorizationException
     */
    public Map<Shop, List<Order>> getOrderHistoryForShops(String userName, Filter<Order> f) throws
            InvalidAuthorizationException, ShopNotFoundException {
        if (!activeUser.containsKey(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
            throw new InvalidAuthorizationException();
        }
        User u = activeUser.get(userName);
            return u.getOrderHistoryForShops(f, shopNumber);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param f
     * @return
     * @throws InvalidAuthorizationException
     */
    public Map<User, List<Order>> getOrderHistoryForUsers(String userName, Filter<Order> f) throws
            InvalidAuthorizationException {
        if (!activeUser.containsKey(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
        }
        User u = activeUser.get(userName);
        return u.getOrderHistoryForUsers(f);
    }

    public boolean isLogin(String userName) throws IncorrectIdentification {
        return getUser(userName).isLoggedIn();
    }

    public List<Order> getOrderHistoryOfUser(String username, Filter<Order> filter) throws InvalidSequenceOperationsExc {
        User user = memberList.get(username);
        if(user == null){
            throw new InvalidSequenceOperationsExc(String.format("guest user: %s has no access to past orders since he is not registered",username));
        }
        return filter.applyFilter(user.getOrderHistory());
    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws
            InvalidSequenceOperationsExc, IncorrectIdentification {
        if (getUser(userName).isSystemManager()) {
            User[] result = (User[]) memberList.values().toArray(); //TODO: better way to solve.
            return f.applyFilter(Arrays.asList(result));
        }
        throw new InvalidSequenceOperationsExc();
    }

    public Integer getCurrentActiveUsers(String username) throws IncorrectIdentification, InvalidAuthorizationException {
        User u = getUser(username);
        if(!u.isSystemManager()){
            throw new InvalidAuthorizationException(String.format("current user %s is not a system manager",username));
        }
        return activeUser.keySet().size();
    }

    public Integer getCurrentActiveMembers(String username) throws IncorrectIdentification, InvalidAuthorizationException {
        User u = getUser(username);
        if(!u.isSystemManager()){
            throw new InvalidAuthorizationException(String.format("current user %s is not a system manager",username));
        }
        int activeMembers = activeUser.keySet().size() - guestUser.keySet().size();
        return activeMembers;
    }

    public Integer getCurrentActiveGuests(String username) throws IncorrectIdentification, InvalidAuthorizationException {
        User u = getUser(username);
        if(!u.isSystemManager()){
            throw new InvalidAuthorizationException(String.format("current user %s is not a system manager",username));
        }
        return guestUser.keySet().size();
    }

    public Integer getTotalMembers(String username) throws IncorrectIdentification, InvalidAuthorizationException {
        User u = getUser(username);
        if(!u.isSystemManager()){
            throw new InvalidAuthorizationException(String.format("current user %s is not a system manager",username));
        }
        return memberList.keySet().size();
    }

    public Map<Integer, User> getAllUsers(String username) throws InvalidAuthorizationException, IncorrectIdentification {
        User u = getUser(username);
        if(!u.isSystemManager()){
            throw new InvalidAuthorizationException(String.format("current user %s is not a system manager",username));
        }
        Map<Integer,User> users = new HashMap<>();
        int index = 1;
        for(User user: memberList.values()){
            users.put(index,user);
            index++;
        }
        for (User user: guestUser.values()){
            users.put(index,user);
            index++;
        }
        return users;
    }

    public boolean isAdmin(String username) throws IncorrectIdentification {
        User u = getUser(username);
        return u.isSystemManager();
    }

    public List<User> getAdminUser() {
        return adminUser;
    }
}
