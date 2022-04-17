package domain;

import domain.shop.ShopController;
import domain.user.UserController;

import java.util.logging.Logger;

public class MarketSystem {
    private Market m;
    private UserController uc; // singleton?
    private ShopController sc;
    private static final Logger logger = Logger.getLogger(MarketSystem.class.getName()); // array of loggers?

    public MarketSystem() {
        logger.info("System start");
    }

    public boolean login(String n,String p) {
        return uc.Login(n,p);
    }

    public String gettingInformation(String s){}

    public String searchItems(String s){}
}
