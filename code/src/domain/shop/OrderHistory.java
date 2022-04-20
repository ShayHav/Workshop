package domain.shop;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderHistory {

    private final List<Order> orders;

    public OrderHistory(){
        orders = new ArrayList<>();
    }

    public boolean addOrder(Order o){
        orders.add(o);
        return true;
    }

    public List<Order> searchByDate(LocalDate from, LocalDate to){
        return orders.stream().filter((Order o) -> o.getBuyingTime().isBefore(ChronoLocalDateTime.from(to)) &&
                o.getBuyingTime().isAfter(ChronoLocalDateTime.from(from))).collect(Collectors.toList());
    }

    public List<Order> searchByUser(int userID){
        return orders.stream().filter((Order o) -> o.getUserID() == userID).collect(Collectors.toList());
    }
}
