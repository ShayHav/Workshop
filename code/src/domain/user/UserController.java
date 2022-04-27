package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.market.ExternalConnector;
import domain.market.MarketSystem;
import domain.shop.Order;
import domain.shop.ProductInfo;
import domain.shop.ShopController;
import domain.shop.ShopInfo;

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
    public boolean logIn(String id, String pass) {
        if (!isUserisLog(id)) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for %s failed.", id));
            return false;
        } else if (memberList.get(id) != null) {
            if (securePasswordStorage.passwordCheck(id, pass)) {
                activeUser.put(id,memberList.get(id));
                getUser(id).login();
                eventLogger.logMsg(Level.INFO, String.format(" logIn for user: %s.", id));
                return true;
            } else {
                return false;
            }
        } else
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for unregistered user with id: %d.", id));
        return false;
    }

    private boolean isUserisLog(String id){
        return activeUser.containsKey(id);
    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency
    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public String logOut(String userId) {
        if (activeUser != null) {
            if(activeUser.containsKey(userId)) {
                User u = getUser(userId);
                u.logout();
                activeUser.remove(userId);
                eventLogger.logMsg(Level.INFO, String.format("logOut for user: %s.", userId));
            }
        } else {
            errorLogger.logMsg(Level.WARNING, "attempt of logOut for user who is not logged in.");
            return null;
        }
        return enterMarket();
    }

    /***
     * register to the system
     * pre-condition - user is not registered
     * @param id the unique identifier of the user
     * @param pass password given by the user
     */
    public boolean register(String id, String pass) {
        if (!memberList.containsKey(id)) {
            User user = new User(id);
            memberList.put(id, user);
            SecurePasswordStorage.getSecurePasswordStorage_singleton().inRole(id, pass);
            eventLogger.logMsg(Level.INFO, String.format("Registered for user: %d.", id));
            return true;
        } else {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of registered for exist id %d failed.", id));
            return false;
        }
    }

    /***
     * enter to the market - active user is now a guest
     */
    public String enterMarket(){
        User temp = new User(String.format("-Guest%d",guestCounter));
        guestCounter++;
        temp.enterMarket();
        activeUser.put(temp.getId(), temp);
        eventLogger.logMsg(Level.INFO, "User entered Market.");
        return temp.getId();
    }

    public User getUser(String id) {
        if(id == null)
            return null;
        return memberList.getOrDefault(id, null);
    }

    public boolean deleteUserTest(String[] userId) {
        for (int i = 0; i < userId.length; i++) {
            deleteUser(userId[i]);
        }
        return true;
    }

    private void deleteUser(String useID) {
        for (Map.Entry<String, User> entry : memberList.entrySet()) {
            if (entry.getKey().equals(useID)) {
                Map<Integer, List<Role>> useRoleList = entry.getValue().getRoleList();
                for(Map.Entry<Integer, List<Role>> run : useRoleList.entrySet()){
                    for(Role runn :run.getValue())
                        if(runn == Role.ShopFounder)
                            ShopController.getInstance().closeShop(run.getKey(), useID);
                }
                memberList.remove(entry.getKey());
            }
        }
        ShopController.getInstance().DeleteShops();
    }

    public List<String> checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate){
        User user = getUser(userID);
        return user.checkout(fullName,address,phoneNumber,cardNumber,expirationDate);
    }

    public boolean createSystemManager(String id, String pass){
        register(id,pass);
        User u = memberList.get(id);
        adminUser.add(u);
        u.makeSystemManager();
        return true;

    }

    public List<Order> getOrderHistoryForUser(List<String>  userID){
        List<Order> orders = new ArrayList<>();
        if(userID == null){
            for(User user: memberList.values()){
                orders.addAll(user.getHistoryOfOrders());
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

    public boolean HasUserEnteredMarket(String userID) {
        return activeUser.containsKey(userID);
    }

    public boolean addProductToCart(String userID, int shopID, int productId, int amount) {
        if(!HasUserEnteredMarket(userID))
            //log
            return false;
        User u = activeUser.get(userID);
        return u.addProductToCart(shopID,productId,amount);
    }

    public boolean updateAmountOfProduct(String userID, int shopID, int productId, int amount) {
        if(!HasUserEnteredMarket(userID))
            //log
            return false;
        User u = activeUser.get(userID);
        return u.updateAmountOfProduct(shopID,productId,amount);
    }

    public boolean removeProductFromCart(String userID, int shopID, int productId) {
        if(!HasUserEnteredMarket(userID))
            //log
            return false;
        User u = activeUser.get(userID);
        return u.removeProductFromCart(shopID,productId);
    }

    public List<Order> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID){
        if(!activeUser.containsKey(userID)){
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
        }
        User u = activeUser.get(userID);
        return u.getOrderHistoryForShops(f,shopID);
    }

    public List<Order> getOrderHistoryForUser(String userID, Filter<Order> f, List<String>  userIDs){
        if(!activeUser.containsKey(userID)){
            errorLogger.logMsg(Level.WARNING, "user %id tried to perform action when he is not logged in");
        }
        User u = activeUser.get(userID);
        return u.getOrderHistoryForUser(f,userIDs);
    }

    public boolean isLogin(String userID) {
        return getUser(userID).isLoggedIn();
    }
}
