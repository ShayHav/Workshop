package domain.user;

import domain.shop.Shop;

import java.util.logging.Logger;


public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private static final String ca = "command approve";
    private Integer id;
    private String name;
    private String password;
    private UserState us;
    private Cart userCart;

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
    public void leaveMarket(){

    }

    public boolean isPass(String pass) {
        return this.password.equals(pass);
    }
    public String addProductToCart(Shop shop, int product, int amount){
        try {
            userCart.addProductToCart(shop,product,amount);
            return ca;
        }
        catch (Exception e){
            logger.info(e.getMessage());
            return e.getMessage();
        }
    }

    public String editingCart(Shop shop, int product, int amount) {
        try {
            userCart.editingCart(shop, product, amount);
            return ca;
        }
        catch (Exception e){
            logger.info(e.getMessage());
            return e.getMessage();
        }
    }
}
