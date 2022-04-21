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
    void checkout(int id, Cart c,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate));

    void leaveMarket();
}