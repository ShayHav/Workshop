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
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return -1;
    }

    @Override
    public boolean appointOwner(String user, int shop, String id, List<OwnerAppointment> ownerAppointmentList) {
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return false;
    }

    @Override
    public boolean appointManager(String user, int shop, String id, List<ManagerAppointment> managerAppointmentList) {
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return false;
    }

    @Override
    public boolean closeShop(int shop, String id) {
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return false;
    }

    @Override
    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String> userID) {
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return null;
    }

    @Override
    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) {
        errorLogger.logMsg(Level.WARNING,"guest is not allowed to perform this action");
        return null;
    }
}
