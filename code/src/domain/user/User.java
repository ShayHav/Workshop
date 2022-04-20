package domain.user;

import domain.shop;

import java.util.logging.Logger;


public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private static final String ca = "command approve";
    private int id;
    private String username;
    //dont save password here, impl with bcrypt
    private String password;
    private UserState us;
    private Cart userCart;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User()
    {
        us = null;
    }

    public User(int id,String password){

    }

    /***
     *
     */
    public void enterMarket() {
        us = new Guest();
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
        if(true) {
            us = new Member();
            //logIn = true;
        }
    }

    /***
     *
     */
    public void logout() {

    }



    public boolean isPass(String pass) {
        return this.password.equals(pass);
    }


    public int getId() {
        return id;
    }
}
