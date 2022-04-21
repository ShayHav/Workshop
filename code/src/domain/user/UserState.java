package domain.user;

import domain.Tuple;
import domain.shop.ManagerAppointment;
import domain.shop.OwnerAppointment;
import domain.shop.Product;
import domain.shop.Shop;

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
    void checkout(int id, Cart c,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);
	
	void leaveMarket(Cart cart);

    void createShop(Shop shop, int id);

    void appointOwner(User user, Shop shop, int id, List<OwnerAppointment> ownerAppointmentList);

    void appointManager(User user, Shop shop, int id, List<ManagerAppointment> managerAppointmentList);

    void closeShop(Shop shop,int id);

    void searchProductByName(String name, Filter f);

    void searchProductByCategory(String category, Filter f);

    void searchProductByKeyword(String keyword, Filter f);
}