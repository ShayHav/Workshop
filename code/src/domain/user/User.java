package domain.user;


import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Logger;


public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private static final String ca = "command approve";
    private int id;
    //TODO:dont save password here, impl with bcrypt
    //private String password;
    private UserState us;
    private Cart userCart;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {us = null;}

    public User(int id){
        this.id = id;
        //this.password = password;
    }

    /***
     *
     */
    public void enterMarket() {
        us = new Guest();
        userCart = new Cart();
    }

    /***
     *
     */
    public void leaveMarket(){
        us = null;
    }


    /***
     *
     */
    public void login() {
        us = new Member();
    }

    /***
     *
     */
    public boolean logout() {
        //TODO: next session iml Cart DataBase
        return true;
    }


    public int getId() {
        return id;
    }
}
