package domain.user;

import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;

public interface UserState {

    /***
     *
     * @return
     */
    List<ShopInfo> getInfoOfShops();

    /***
     *
     */
    List<ProductInfo> getInfoOfProductInShop(int shopID);

    /***
     *
     */
    List<Order> checkout(String id, Cart c,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);
	
	void leaveMarket(Cart cart);

    void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id);

    void appointOwner(User user, Shop shop, String id, List<OwnerAppointment> ownerAppointmentList);

    void appointManager(User user, Shop shop, String id, List<ManagerAppointment> managerAppointmentList);

    void closeShop(Shop shop,String id);

    void searchProductByName(String name, Filter f);

    void searchProductByCategory(String category, Filter f);

    void searchProductByKeyword(String keyword, Filter f);
}