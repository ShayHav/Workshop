package domain.user;

import domain.ResponseT;
import domain.Tuple;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;

public class Guest implements UserState {

    private static MarketSystem market = MarketSystem.getInstance();

    @Override
    public List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfShops(f);
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfProductInShop(shopID);
    }



    @Override
    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByName(name, f);
    }

    @Override
    public List<ProductInfo>  searchProductByCategory(String category, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByCategory(category, f);
    }

    @Override
    public List<ProductInfo>  searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByKeyword(keyword, f);
    }

    @Override
    public Cart.CartInfo showCart(Cart c) {
        return c.showCart();
    }

    @Override
    public void addProductToCart(Cart c, Shop shop, int productID, int amount) {
        c.addProductToCart(shop, productID, amount);
    }

    @Override
    public boolean updateAmountOfProduct(Cart c, int shopID, int productID, int amount) {
        return c.updateAmountOfProduct(shopID, productID, amount);
    }

    @Override
    public boolean removeProductFromCart(Cart c, int shopID, int productID) {
        return c.removeProductFromCart(shopID, productID);
    }

    @Override
    public List<ResponseT<Order>> checkout(String id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return c.checkout(id, fullName, address, phoneNumber, cardNumber, expirationDate);
    }




    @Override
    public void leaveMarket(Cart cart) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void appointOwner(User user, Shop shop, String id, List<OwnerAppointment> ownerAppointmentList) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void appointManager(User user, Shop shop, String id, List<ManagerAppointment> managerAppointmentList) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void closeShop(Shop shop, String id) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }


}
