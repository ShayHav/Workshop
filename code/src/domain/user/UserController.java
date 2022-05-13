package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.shop.Order;
import domain.shop.ShopController;
import domain.user.filters.Filter;
import domain.user.filters.SearchUserFilter;

import java.util.*;
import java.util.logging.Level;

public class UserController {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static final SecurePasswordStorage securePasswordStorage = SecurePasswordStorage.getSecurePasswordStorage_singleton();
    private Map<String, User> memberList; //TODO: At a later stage there will be a list of Thread by users
    private Map<String,User> activeUser; //TODO: temporary
    private static UserController instance = null;
    private List<User> adminUser;
    private int guestCounter = 0;

    private UserController() {
        memberList = new HashMap<>();
        activeUser = new HashMap<>();
        adminUser = new LinkedList<>();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    //TODO: add logger and validate pre condition
    /***
     * login to the market system
     * pre-condition - user is registered to the system, not yet logged in
     * @param id the unique identifier of the user
     * @param pass password given by the user
     */
    public User logIn(String id, String pass) throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        User output;
        if (isUserisLog(id)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for %s failed. user is already logged in", id));
            throw new InvalidSequenceOperationsExc("attempt of logIn for logged in user");
        } else if (memberList.get(id) != null) {
            if (securePasswordStorage.passwordCheck(id, pass)) {
                synchronized (activeUser) {
                    activeUser.put(id, memberList.get(id));
                    output = getUser(id);
                    output.login();
                }
                eventLogger.logMsg(Level.INFO, String.format("logIn for user: %s.", id));
                return output;
            } else {
                throw new InvalidAuthorizationException("Identifier not correct");
            }
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for unregistered user with id: %d.", id));
            throw new InvalidAuthorizationException();
        }
    }

    private synchronized boolean isUserisLog(String id){
        return activeUser.containsKey(id);
    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency
    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public User logOut(String userId) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if (activeUser != null) {
            if(activeUser.containsKey(userId)) {
                synchronized (activeUser) {
                    User u = getUser(userId);
                    u.logout();
                    activeUser.remove(userId);
                }
                eventLogger.logMsg(Level.INFO, String.format("logOut for user: %s.", userId));
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
    public User enterMarket(){
        User temp = new User(String.format("-Guest%d",guestCounter));
        guestCounter++;
        temp.enterMarket();
        activeUser.put(temp.getUserName(), temp);
        eventLogger.logMsg(Level.INFO, "User entered Market.");
        return temp;
    }

    public User getUser(String id) throws IncorrectIdentification {
        if(id == null)
            throw new IncorrectIdentification("id not exist");
        return memberList.getOrDefault(id, null);
    }

    public boolean deleteUserTest(String[] userId) throws InvalidSequenceOperationsExc {
        for (int i = 0; i < userId.length; i++) {
            deleteUser(userId[i]);
        }
        return true;
    }
    public boolean deleteUserName(String s) throws InvalidSequenceOperationsExc {
        if(!memberList.containsKey(s)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt to delete not exist user: %s", s));
            throw new InvalidSequenceOperationsExc(String.format("attempt to delete not exist user: %s", s));
        }
        deleteUser(s);
        return memberList.containsKey(s);
    }

    private void deleteUser(String useID) throws InvalidSequenceOperationsExc {
        User u = memberList.get(useID);
        if (u != null) {
            Map<Integer, List<Role>> useRoleList = u.getRoleList();
            for (Map.Entry<Integer, List<Role>> run : useRoleList.entrySet()) {
                for (Role runn : run.getValue())
                    if (runn == Role.ShopFounder)
                        ShopController.getInstance().closeShop(run.getKey(), useID);
            }
            memberList.remove(u);
        }
    }

    public List<String> checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        User user = getUser(userID);
        return user.checkout(fullName,address,phoneNumber,cardNumber,expirationDate);
    }

    public boolean createSystemManager(String id, String pass) throws InvalidSequenceOperationsExc {
        register(id,pass);
        User u = memberList.get(id);
        synchronized (adminUser) {
            adminUser.add(u);
        }
        u.makeSystemManager();
        return true;
    }

    public List<Order> getOrderHistoryForUser(List<String>  userID){
        List<Order> orders = new ArrayList<>();
        if(userID == null) {
            synchronized (memberList) {
                for (User user : memberList.values()) {
                    orders.addAll(user.getHistoryOfOrders());
                }
            }
        }
        else{
            for(String id: userID){
                User user = memberList.get(id);
                if(user == null){
                  //log
                  return null;
                }
                orders.addAll(user.getHistoryOfOrders());
            }
        }
        return orders;
    }

    public synchronized boolean HasUserEnteredMarket(String userID) {
        return activeUser.containsKey(userID);
    }

    public boolean addProductToCart(String userID, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException {
        if(!HasUserEnteredMarket(userID)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userID);
        return u.addProductToCart(shopID,productId,amount);
    }

    public boolean updateAmountOfProduct(String userID, int shopID, int productId, int amount) throws InvalidSequenceOperationsExc {
        if(!HasUserEnteredMarket(userID)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userID);
        return u.updateAmountOfProduct(shopID,productId,amount);
    }

    public boolean removeProductFromCart(String userID, int shopID, int productId) throws InvalidSequenceOperationsExc {
        if(!HasUserEnteredMarket(userID))
            throw new InvalidSequenceOperationsExc();
        User u = activeUser.get(userID);
        return u.removeProductFromCart(shopID,productId);
    }

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID) throws InvalidAuthorizationException {
        if(!activeUser.containsKey(userID)){
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
            throw new InvalidAuthorizationException();
        }
        User u = activeUser.get(userID);
            return u.getOrderHistoryForShops(f, shopID);
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String>  userIDs) throws InvalidAuthorizationException {
        if(!activeUser.containsKey(userID)){
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
        }
        User u = activeUser.get(userID);
        return u.getOrderHistoryForUser(f,userIDs);
    }

    public boolean isLogin(String userID) throws IncorrectIdentification {
        return getUser(userID).isLoggedIn();
    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        if(getUser(userName).isSystemManager()){
            User[] result = (User[]) memberList.values().toArray(); //TODO: better way to solve.
            return f.applyFilter(Arrays.asList(result));
        }
        throw new InvalidSequenceOperationsExc();
    }
}
