package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private static final SecurePasswordStorage securePasswordStorage = SecurePasswordStorage.getSecurePasswordStorage_singleton();
    private Map<Integer, User> memberList; //TODO: At a later stage there will be a list of Thread by users
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
    public void logIn(int id, String pass) {
        if(memberList.get(new Integer(id))!=null) {
            if (securePasswordStorage.passwordCheck(id, pass)) {
                activeUser = memberList.get(id);
                activeUser.login();
            }
        }
        else errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for unregistered user with id: %d.", id));
        if(!activeUser.islog())
            errorLogger.logMsg(Level.WARNING, String.format("attempt of logIn for %d failed.", id));
        else eventLogger.logMsg(Level.INFO, String.format(" logIn for user: %d.", id));
    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency
    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public void logOut() {
        if (activeUser != null) {
            int id = activeUser.getId();
            activeUser.logout();
            eventLogger.logMsg(Level.INFO, String.format("logOut for user: %d.", id));
        }
        else errorLogger.logMsg(Level.WARNING, "attempt of logOut for unlog user.");
    }

    /***
     * register to the system
     * pre-condition - user is not registered
     * @param id the unique identifier of the user
     * @param pass password given by the user
     */
    public void register(int id, String pass) {
        if (!memberList.containsKey(id)) {
            User user = new User(id);
            memberList.put(id, user);
            SecurePasswordStorage.getSecurePasswordStorage_singleton().inRole(id,pass);
            eventLogger.logMsg(Level.INFO, String.format("Registered for user: %d.", id));
        }
        else errorLogger.logMsg(Level.WARNING, String.format("attempt of registered for exist id %d failed.",id));
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

    public User getUser(int id) {
        User getUser = null;
        for (Map.Entry<Integer, User> entry : memberList.entrySet()){
            if(entry.getKey()==id)
                getUser = entry.getValue();
        }
        return getUser;
    }
}
