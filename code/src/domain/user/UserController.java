package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.ProductInfo;
import domain.shop.ShopInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class UserController {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static final SecurePasswordStorage securePasswordStorage = SecurePasswordStorage.getSecurePasswordStorage_singleton();
    private Map<String, User> memberList; //TODO: At a later stage there will be a list of Thread by users
    private User activeUser; //TODO: temporary

    public UserController() {
        memberList = new HashMap<>();
        activeUser = null;
    }

    //TODO: add logger and validate pre condition
    /***
     * login to the market system
     * pre-condition - user is registered to the system, not yet logged in
     * @param id the unique identifier of the user
     * @param pass password given by the user
     */
    public boolean logIn(String id, String pass) {
        if (!activeUser.isLoggedIn()) {
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for %s failed.", id));
            return false;
        } else if (memberList.get(id) != null) {
            if (securePasswordStorage.passwordCheck(id, pass)) {
                activeUser = memberList.get(id);
                activeUser.login();
                eventLogger.logMsg(Level.INFO, String.format(" logIn for user: %s.", id));
                return true;
            } else {
                return false;
            }
        } else
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for unregistered user with id: %d.", id));
        return false;
    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency
    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public boolean logOut() {
        if (activeUser != null) {
            String id = activeUser.getId();
            activeUser.logout();
            eventLogger.logMsg(Level.INFO, String.format("logOut for user: %s.", id));
        } else errorLogger.logMsg(Level.WARNING, "attempt of logOut for unlog user.");

        return !activeUser.isLoggedIn();
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
    public void enterMarket(){
        User temp = new User();
        temp.enterMarket();
        this.activeUser = temp;
        eventLogger.logMsg(Level.INFO, "User entered Market.");
    }

    public User getUser(String id) {
        User getUser = null;
        for (Map.Entry<String, User> entry : memberList.entrySet()){
            if(entry.getKey().equals(id))
                getUser = entry.getValue();
        }
        return getUser;
    }

    public List<ShopInfo> getInfoOfShops(String id){
        throw new UnsupportedOperationException();
    }

    public List<ProductInfo> getInfoOfProductInShop(String userId, int shopID){
        throw new UnsupportedOperationException();
    }

    public List<ProductInfo>  searchProductByName(String userId, String name, SearchProductFilter f){
        throw new UnsupportedOperationException();
    }

    public List<ProductInfo>  searchProductByCategory(String userId, String category, SearchProductFilter f){
        throw new UnsupportedOperationException();
    }

    public List<ProductInfo>  searchProductByKeyword(String userId, String keyword, SearchProductFilter f){
        throw new UnsupportedOperationException();
    }

    public void deleteUserTest(String[] userId) {
        for (int i = 0; i < userId.length; i++) {
            deleteUser(userId[i]);
        }
    }

    private void deleteUser(String useID) {
        for (Map.Entry<String, User> entry : memberList.entrySet()) {
            if (entry.getKey().equals(useID)) {

                memberList.remove(entry.getKey());
            }
        }
    }
}
