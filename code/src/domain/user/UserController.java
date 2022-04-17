package domain.user;

import domain.MarketSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserController {
    private Map<Integer, User> activeUserList;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController() {
    }

    public UserController(User employee) {
    }

    public boolean Login(String n,String p) {
        return true;
    }
}
