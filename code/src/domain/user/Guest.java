package domain.user;

import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;

public class Guest implements UserState {

    @Override
    public List<ShopInfo> getInfoOfShops() {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfShops();
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfProductInShop(shopID);
    }


    @Override
    public List<ProductInfo> searchProductByName(String name, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByName(name, f);
    }

    @Override
    public List<ProductInfo>  searchProductByCategory(String category, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByCategory(category, f);
    }

    @Override
    public List<ProductInfo>  searchProductByKeyword(String keyword, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByKeyword(keyword, f);
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
