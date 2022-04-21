package domain.user;

import domain.Tuple;
import domain.shop.Product;

import java.util.List;

public interface UserState {

    /***
     *
     * @param f
     */
    List<String> getInfoOfShops();

    /***
     *
     */
    List<Tuple<String,String>> getInfoOfProductInShop(int shopID);

    /***
     *
     * @param f
     */
    void searchProduct(Filter f);


    /***
     *
     */
    void checkout(int id, Cart c,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate));

}