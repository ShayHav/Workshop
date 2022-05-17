package domain.user;

import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.ShopNotFoundException;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.filter.Filter;

import java.util.List;

public interface UserState {

    boolean saveCart(Cart cart);

    int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) throws BlankDataExc, IncorrectIdentification;

    boolean appointOwner(String user, int shop, String id, List<OwnerAppointment> ownerAppointmentList) throws IncorrectIdentification, BlankDataExc, ShopNotFoundException;

    boolean appointManager(String user, int shop, String id, List<ManagerAppointment> managerAppointmentList) throws ShopNotFoundException, IncorrectIdentification, BlankDataExc;

    boolean closeShop(int shop, String id) throws ShopNotFoundException;

    List<Order> getOrderHistoryForUser(Filter<Order> f, List<String>  userID);

    List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID);




}