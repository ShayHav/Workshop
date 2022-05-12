package domain.user;

import domain.EventLoggerSingleton;
import domain.shop.Order;
import domain.shop.ProductInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchOrderFilter implements Filter<Order> {
    Double minPrice;
    Double maxPrice;
    LocalDate minDate;
    LocalDate maxDate;


    public SearchOrderFilter(Double minPrice, Double maxPrice, LocalDate minDate, LocalDate maxDate) {
        this.minPrice = Objects.requireNonNullElse(minPrice, 0d);
        this.maxPrice = Objects.requireNonNullElse(maxPrice, Double.MAX_VALUE);
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public List<Order> applyFilter(List<Order> orders) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filer for Orders");
        List<Order> result = new ArrayList<>();

        if(minPrice < -1 || maxPrice > Double.MAX_VALUE || minPrice > maxPrice
            || minDate.isAfter(LocalDate.now()) || maxDate.isAfter(LocalDate.now())
            || maxDate.isBefore(minDate)) {
            return result;
        }

        result = orders.stream().filter(o -> (o.getTotalAmount() >= minPrice & o.getTotalAmount() <= maxPrice)).collect(Collectors.toList());

        if (minDate != null) {
            result = result.stream().filter(o -> o.getBuyingTime().isAfter(minDate.atStartOfDay())).collect(Collectors.toList());
        }
        if (maxDate != null) {
            result = result.stream().filter(o -> o.getBuyingTime().isBefore(maxDate.atStartOfDay())).collect(Collectors.toList());
        }
        return result;
    }
}
