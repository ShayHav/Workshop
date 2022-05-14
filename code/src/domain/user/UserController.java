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
    public User logOut(String userName) throws IncorrectIdentification, InvalidSequenceOperationsExc {
        if (activeUser != null) {
            if(activeUser.containsKey(userName)) {
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

    public boolean deleteUserTest(String[] userName) throws InvalidSequenceOperationsExc {
        for (int i = 0; i < userName.length; i++) {
            deleteUser(userName[i]);
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

    /**
     * for each user that we delete we need to close this shop (Founder of shop)
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
            memberList.remove(u);
        }
    }

    public List<String> checkout(String userName,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) throws IncorrectIdentification, BlankDataExc {
        User user = getUser(userName);
        return user.checkout(fullName,address,phoneNumber,cardNumber,expirationDate);
    }

    /**
     * Create system manager
     * @param id - user identifier
     * @param pass - given password
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean createSystemManager(String id, String pass) throws InvalidSequenceOperationsExc {
        register(id,pass);
        User u = memberList.get(id);
        synchronized (adminUser) {
            adminUser.add(u);
        }
        u.makeSystemManager();
        return true;
    }
    public List<Order> getOrderHistoryForUser(List<String>  userName){
        List<Order> orders = new ArrayList<>();
        if(userName == null) {
            synchronized (memberList) {
                for (User user : memberList.values()) {
                    orders.addAll(user.getHistoryOfOrders());
                }
            }
        }
        else{
            for(String id: userName){
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

    public synchronized boolean HasUserEnteredMarket(String userName) {
        return activeUser.containsKey(userName);
    }

    public boolean addProductToCart(String userName, int shopNumber, int productId, int amount) throws InvalidSequenceOperationsExc, ShopNotFoundException {
        if(!HasUserEnteredMarket(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userName);
        return u.addProductToCart(shopNumber,productId,amount);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param shopNumber
     * @param productId
     * @param amount
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean updateAmountOfProduct(String userName, int shopNumber, int productId, int amount) throws InvalidSequenceOperationsExc {
        if(!HasUserEnteredMarket(userName)) {
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not entered Market");
            throw new InvalidSequenceOperationsExc();
        }
        User u = activeUser.get(userName);
        return u.updateAmountOfProduct(shopNumber,productId,amount);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param shopNumber
     * @param productId
     * @return
     * @throws InvalidSequenceOperationsExc
     */
    public boolean removeProductFromCart(String userName, int shopNumber, int productId) throws InvalidSequenceOperationsExc {
        if(!HasUserEnteredMarket(userName))
            throw new InvalidSequenceOperationsExc();
        User u = activeUser.get(userName);
        return u.removeProductFromCart(shopNumber,productId);
    }

    /**
     * Checking operation validity and performing
     * @param userName
     * @param f
     * @param shopNumber
     * @return
     * @throws InvalidAuthorizationException
     */
    public List<Order> getOrderHistoryForShops(String userName, Filter<Order> f, List<Integer> shopNumber) throws InvalidAuthorizationException, ShopNotFoundException {
        if(!activeUser.containsKey(userName)){
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
     * @param userNames
     * @return
     * @throws InvalidAuthorizationException
     */
    public List<Order> getOrderHistoryForUser(String userName, Filter<Order> f, List<String>  userNames) throws InvalidAuthorizationException {
        if(!activeUser.containsKey(userName)){
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
        }
        User u = activeUser.get(userName);
        return u.getOrderHistoryForUser(f,userNames);
    }

    public boolean isLogin(String userName) throws IncorrectIdentification {
        return getUser(userName).isLoggedIn();
    }

    public List<User> RequestUserInfo(SearchUserFilter f, String userName) throws InvalidSequenceOperationsExc, IncorrectIdentification {
        if(getUser(userName).isSystemManager()){
            User[] result = (User[]) memberList.values().toArray(); //TODO: better way to solve.
            return f.applyFilter(Arrays.asList(result));
        }
        throw new InvalidSequenceOperationsExc();
    }
}
