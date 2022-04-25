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
    public List<ShopInfo> getInfoOfShops() {
        return market.getInfoOfShops();
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        return null;
    }



    @Override
    public void searchProductByName(String name, Filter f) {

    }

    @Override
    public void searchProductByCategory(String category, Filter f) {

    }

    @Override
    public void searchProductByKeyword(String keyword, Filter f) {

    }


    @Override
    public List<Order> checkout(String id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        List<ResponseT<Order>> result = c.checkout(id, fullName, address, phoneNumber, cardNumber, expirationDate);
        for(ResponseT<Order> o : result){
            o.isErrorOccurred()
        }
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
