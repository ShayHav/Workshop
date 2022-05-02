package domain.user;

import domain.ResponseT;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;

public interface UserState {

    boolean saveCart(Cart cart);

    int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id);

    boolean appointOwner(String user, int shop, String id, List<OwnerAppointment> ownerAppointmentList);

    boolean appointManager(String user, int shop, String id, List<ManagerAppointment> managerAppointmentList);

    boolean closeShop(int shop, String id);

    List<Order> getOrderHistoryForUser(Filter<Order> f, List<String>  userID);

    List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID);




}