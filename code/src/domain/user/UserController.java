package domain.user;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserController {
    private Map<Integer, User> memberList;
    private User activeUser;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

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
        if(SecurePasswordStorage.getSecurePasswordStorage_singleton().passwordCheck(id,pass)){
            activeUser = memberList.get(id);
            activeUser.login();
        }

    }

    //TODO: add logger and validate user is registered and logged in- when transferring to concurrency
    /***
     * logout from system
     * pre-condition - user is registered and logged-in
     */
    public void logOut() {
        if (activeUser != null) {
            activeUser.logout();
        }

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
        }
    }

    /***
     * enter to the market - active user is now a guest
     */
    public void enterMarket(){
        User temp = new User();
        temp.enterMarket();
        this.activeUser = temp;
    }
}
