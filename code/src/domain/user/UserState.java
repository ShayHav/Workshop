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
     * @param shop
     * @param p
     * @param amount
     */
    void addProductToCart(String shop, Product p, int amount);

    /***
     *
     */
    void checkCart();

    /***
     *
     */
    void checkout();

   }
