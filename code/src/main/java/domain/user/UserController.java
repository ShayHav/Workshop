package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.Response;
import domain.shop.Order;
import domain.shop.ShopController;
import domain.user.filter.*;

import java.util.*;
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
                    u.logout();
                    activeUser.remove(userName);
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

    public boolean deleteUserTest(String[] userName) throws InvalidSequenceOperationsExc {
        for (int i = 0; i < userName.length; i++) {
            deleteUser(userName[i]);
        }
        return true;
    }

    public boolean deleteUserName(String s) throws InvalidSequenceOperationsExc {
        if (!memberList.containsKey(s)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to delete not exist user: %s", s));
            throw new InvalidSequenceOperationsExc(String.format("attempt to delete not exist user: %s", s));
        }
        deleteUser(s);
        return memberList.containsKey(s);
    }

    /**
     * for each user that we delete we need to close this shop (Founder of shop)
     *
     * @param useID
     * @throws InvalidSequenceOperationsExc
     */
    private void deleteUser(String useID) throws InvalidSequenceOperationsExc {
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
        register(id, pass);
        User u = memberList.get(id);
        synchronized (adminUser) {
            adminUser.add(u);
        }
        u.makeSystemManager();
        return true;
    }

    public List<Order> getOrderHistoryForUsers() {
        List<Order> orders = new ArrayList<>();
        synchronized (memberList) {
            for (User user : memberList.values()) {
                orders.addAll(user.getHistoryOfOrders());
            }

            return orders;
        }
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
     *
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
     *
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
     *
     * @param userName
     * @param f
     * @return
     * @throws InvalidAuthorizationException
     */
    public List<Order> getOrderHistoryForShops(String userName, Filter<Order> f) throws
            InvalidAuthorizationException, ShopNotFoundException {
        if (!activeUser.containsKey(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
            throw new InvalidAuthorizationException();
        }
        User u = activeUser.get(userName);
        return u.getOrderHistoryForShops(f);
    }

    /**
     * Checking operation validity and performing
     *
     * @param userName
     * @param f
     * @return
     * @throws InvalidAuthorizationException
     */
    public List<Order> getOrderHistoryForUsers(String userName, Filter<Order> f) throws
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

    public List<Order> getOrderHistoryOfUser(String username) throws InvalidSequenceOperationsExc {
        User user = memberList.get(username);
        if(user == null){
            throw new InvalidSequenceOperationsExc(String.format("guest user: %s has no access to past orders since he is not registered",username));
        }
        return user.getHistoryOfOrders();
    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws
            InvalidSequenceOperationsExc, IncorrectIdentification {
        if (getUser(userName).isSystemManager()) {
            User[] result = (User[]) memberList.values().toArray(); //TODO: better way to solve.
            return f.applyFilter(Arrays.asList(result));
        }
        throw new InvalidSequenceOperationsExc();
    }


    public List<User> getAdminUser() {
        return adminUser;
    }
}
