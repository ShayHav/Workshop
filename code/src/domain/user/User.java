package domain.user;


import domain.Tuple;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private static final String ca = "command approve";
    private int id;
    //TODO:dont save password here, impl with bcrypt
    private UserState us;
    private Cart userCart;
    private boolean loggedIn;

    //TODO: all methods in user, delegate to state. if only methods of member: impl in guest and throw exception/log as error.

    public User() {
        us = null;
    }

    public User(int id) {
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
    public void leaveMarket() {
        us = null;
    }


    /***
     * login to the system
     */
    public void login() {
        us = new Member();
        loggedIn = true;
    }

    /***
     *  logout from the system
     */
    public void logout() {
        //TODO: next session iml Cart DataBase
        loggedIn = false;
    }

    /***
     * show the contents of the cart
     * @return the contents of the cart
     */
    public Map<Integer, List<Tuple<Integer, Integer>>> showCart() {
        return userCart.showCart();
    }

    public int getId() {
        return id;
    }

    public void checkout(String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        us.checkout(id, userCart, fullName, address, phoneNumber, cardNumber, expirationDate);
    }


}
