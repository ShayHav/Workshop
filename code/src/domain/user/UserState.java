package domain.user;

import domain.shop.Product;

public interface UserState {

    /***
     *
     * @param f
     */
    void getInfo(Filter f);

    /***
     *
     * @param f
     */
    void searchProduct(Filter f);


    /***
     *
     */
    void checkCart();

    /***
     *
     */
    void checkout();

    void leaveMarket();
   }