package domain.user;

import domain.ResponseT;
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
    List<ResponseT<Order>> checkout(String id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);
	
	void leaveMarket(Cart cart);

    void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id);

    void appointOwner(User user, Shop shop, String id, List<OwnerAppointment> ownerAppointmentList);

    void appointManager(User user, Shop shop, String id, List<ManagerAppointment> managerAppointmentList);

    void closeShop(Shop shop,String id);

    List<ProductInfo>  searchProductByName(String name, SearchProductFilter f);

    List<ProductInfo>  searchProductByCategory(String category, SearchProductFilter f);

    List<ProductInfo>  searchProductByKeyword(String keyword, SearchProductFilter f);
}