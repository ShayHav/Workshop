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

    public boolean logIn(int id, String pass) {
        if(SecurePasswordStorage.getSecurePasswordStorage_singleton().passwordCheck(id,pass))
            activeUser = memberList.get(new Integer(id));
        return activeUser != null;
    }

    public boolean logOut() {
        if (activeUser != null) {
            return activeUser.logout();
        }
        return false;
    }

    /***
     *
     * @param id
     * @param pw
     */
    public void register(int id, String pw) {
        if (!memberList.containsKey(id)) {
            User user = new User(id);
            memberList.put(id, user);
            SecurePasswordStorage.getSecurePasswordStorage_singleton().inRole(id,pw);
        }
    }
    public void enterMarket(){
        User temp = new User();
        temp.enterMarket();
        this.activeUser = temp;
    }
}
