package domain.user;

import domain.ResponseT;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;

public interface UserState {

    void leaveMarket(Cart cart);

    int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id);

    void appointOwner(String user, int shop, String id, List<OwnerAppointment> ownerAppointmentList);

    void appointManager(String user, int shop, String id, List<ManagerAppointment> managerAppointmentList);

    void closeShop(int shop, String id);




}