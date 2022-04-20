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

    public UserController(User employee) {
    }

    public boolean logIn(int id, String pass){
        User temp = memberList.get(new Integer(id));
        if(temp.isPass(pass))
            activeUser=temp;
        return activeUser !=null;
    }

    public boolean logOut(){
        if(activeUser!=null) {
            activeUser.logout();
        }
        return false;
    }
    /***
     *
     * @param id
     * @param pw
     */
    public void register(int id, String pw)
    {
        if(!memberList.containsKey(id)) {
            User user = new User(id, pw);
            memberList.put(id,user);
        }
    }
    //public boolean Login(String n,String p) {return true;}
}
