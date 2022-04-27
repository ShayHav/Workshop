package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.Tuple;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;
import java.util.logging.Level;

public class Guest implements UserState {

    private static MarketSystem market = MarketSystem.getInstance();
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    @Override
    public boolean saveCart(Cart cart) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void appointOwner(String user, int shop, String id, List<OwnerAppointment> ownerAppointmentList) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void appointManager(String user, int shop, String id, List<ManagerAppointment> managerAppointmentList) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public void closeShop(int shop, String id) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String> userID) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }

    @Override
    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }
}
