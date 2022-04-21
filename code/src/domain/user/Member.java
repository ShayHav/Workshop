package domain.user;

import domain.shop.Product;
import domain.shop.Shop;

public class Member implements UserState{

    @Override
    public void getInfo(Filter f) {

    }

    @Override
    public void searchProduct(Filter f) {

    }

    @Override
    public void checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {

    }


    @Override
    public void leaveMarket() {
        //TODO: how to accesses to the user cart?
    }


    /***
     *
     * @param shop - shop's name or ID
     */
    public void createShop(String shop,int id)
    {
        //check if shop id exist

    } //TODO: should be at upper level

    /***
     *
     * @param userID - The appointed user's unique id
     */
    public void appointOwner(String userID)
    {
        throw new UnsupportedOperationException();
    } //TODO: should be at upper level

    /***
     *
     * @param userID - The appointed user's unique id
     *               TODO: decide if need to add param Permissions or to receive permission in the
     */
    public void appointManager(String userID)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param userID - The Manager's unique id
     */
    public void changeManagerPermissions(String userID)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param shop
     */
    public void closeShop(Shop shop)
    {
        throw new UnsupportedOperationException();
    }


    /***
     * The function will display information on the shop, and shop's officials.
     * The function can be called only by Shop Owner
     * @param f
     */
    public void requestShopInfo(Filter f)
    {
        throw new UnsupportedOperationException();
    }


    /***
     * The function will display past orders that were completed in the shop
     * @param f
     */
    public void getOrderHistory(Filter f)
    {

    }




}
