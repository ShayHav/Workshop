package domain.user;

import domain.ResponseT;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.Cart.CartInfo;

import java.util.List;

public interface UserState {

    void leaveMarket(Cart cart);

    void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id);

    void appointOwner(User user, Shop shop, String id, List<OwnerAppointment> ownerAppointmentList);

    void appointManager(User user, Shop shop, String id, List<ManagerAppointment> managerAppointmentList);

    void closeShop(Shop shop, String id);




}