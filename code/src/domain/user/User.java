package domain.user;


import domain.ErrorLoggerSingleton;
import domain.shop.Shop;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;


public class User {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final String ca = "command approve";
    private int id;
    private UserState us;
    private List<Role> roleList;
    private Cart userCart;
    private boolean loggedIn;
    private List<ManagerAppointment> managerAppointeeList;
    private List<OwnerAppointment> ownerAppointmentList;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {us = null;}

    public User(int id){
        this.id = id;
        loggedIn = false;
    }

    /***
     * enter market - user state is now guest, with empty cart
     */
    public void enterMarket() {
        us = new Guest();
        userCart = new Cart();
    }

    /***
     * leave market - user has no state
     */
    public void leaveMarket(){
        us.leaveMarket(userCart);
        us = null;
    }

    public void closeShop(Shop shop){
        if(roleList.contains(Role.ShopFounder))
            us.closeShop(shop,this.id);
        else errorLogger.logMsg(Level.WARNING,String.format("Not Founder shop try to close shop. user: %d",this.id));
    }

    public void createShop(Shop shop){
        roleList.add(Role.ShopFounder);
        us.createShop(shop,this.id);
    }

    public void appointOwner(User user,Shop shop){
        if(roleList.contains(Role.ShopFounder) || roleList.contains(Role.ShopOwner))
            us.appointOwner(user,shop,this.id,ownerAppointmentList);
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner withOut appropriate role by user: %d",id));
    }

    /***
     * login to the system
     */
    public void login() {
        us = new Member();
        if (ownerAppointmentList == null)
            ownerAppointmentList = new LinkedList<>();
        if (managerAppointeeList == null)
            managerAppointeeList = new LinkedList<>();
        loggedIn = true;
    }

    /***
     *  logout from the system
     */
    public void logout() {
        //TODO: next session iml Cart DataBase
        loggedIn = false;
    }

    public int getId() {
        return this.id;
    }

    public boolean islog() {
        return this.loggedIn;
    }
}
