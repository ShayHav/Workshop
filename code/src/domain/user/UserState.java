package domain.user;

import domain.ResponseT;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.Cart.CartInfo;

import java.util.List;

public interface UserState {

    /***
     *
     * @return
     */
    List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f);

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

    List<ProductInfo>  searchProductByName(String name, Filter<ProductInfo> f);

    List<ProductInfo>  searchProductByCategory(String category, Filter<ProductInfo> f);

    List<ProductInfo>  searchProductByKeyword(String keyword, Filter<ProductInfo> f);

    CartInfo showCart();

    void addProductToCart(Shop shop, int productID, int amount);

    boolean updateAmountOfProduct(int shopID, int productID, int amount);

    boolean removeProductFromCart(int shopID, int productID);



}