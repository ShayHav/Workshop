package domain.shop;

import domain.DAL.ControllerDAL;
import domain.ErrorLoggerSingleton;
import domain.user.filter.SearchOrderFilter;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class OrderHistory {

    private final List<Order> orders;
    private long orderIdGen = 0;
    @Transient
    private ControllerDAL controllerDAL = ControllerDAL.getInstance();
    public OrderHistory(){
        orders = new ArrayList<>();
    }

    /**
     * adding a finished order to the store's History
     * @param o an order the ended which mean that the user paid to and successfully sent to supply
     */
    public synchronized void addOrder(Order o){
        orders.add(o);
        o.setOrderId(orderIdGen);
        controllerDAL.saveOrder(o);
        controllerDAL.upDateOrderHistory(this);
        orderIdGen++;
    }

    /**
     * search all the order filtered by dates
     * @param from the beginning  of the filter
     * @param to the end of the filter
     * @return a List of all the Orders filtered
     */
    public List<Order> searchByDate(LocalDateTime from, LocalDateTime to){
        return orders.stream().filter((Order o) -> o.getBuyingTime().compareTo(from) >= 0 &&
                o.getBuyingTime().compareTo(to) <= 0).collect(Collectors.toList());
    }

    /**
     * search all the orders of a specific user
     * @param userID the id of the user to search by
     * @return List of all the Order that were paid by and supplied to the user
     */
    public List<Order> searchByUser(String userID){
        return orders.stream().filter((Order o) -> o.getUserID().equals(userID)).collect(Collectors.toList());
    }

    /**
     * return a Order with given order id or null if not found
     * @param orderID the order id to search by
     * @return Instance of Order or Null if failed to found one
     */
    public Order getOrder(long orderID){
        for(Order o: orders){
            if(o.getOrderId() == orderID)
                return o;
        }
        ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
        errorLogger.logMsg(Level.WARNING, String.format("No order with order I.D.: %d", orderID));
        return null;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public List<Order> getOrders(SearchOrderFilter f){
        //todo::
        return new ArrayList<>();
    }
}
