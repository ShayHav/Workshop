package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchOrderFilter implements Filter<Order> {
    private Double minPrice;
    private Double maxPrice;
    private LocalDate minDate;
    private LocalDate maxDate;

    public SearchOrderFilter() {
        minPrice = 0d;
        maxPrice = Double.MAX_VALUE;
        minDate = null;
        maxDate = null;
    }

    public SearchOrderFilter(Double minPrice, Double maxPrice, LocalDate minDate, LocalDate maxDate) {
        this.minPrice = Objects.requireNonNullElse(minPrice, 0d);
        this.maxPrice = Objects.requireNonNullElse(maxPrice, 1000d);
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public List<Order> applyFilter(List<Order> orders) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO, "Operate filter for Orders");
        List<Order> result = new ArrayList<>();

        if (minPrice < -1 || maxPrice > Double.MAX_VALUE || minPrice > maxPrice
                || (minDate != null && minDate.isAfter(LocalDate.now()))
                || (maxDate!= null && maxDate.isAfter(LocalDate.now()))
                || (minDate!= null && maxDate!= null && maxDate.isBefore(minDate))) {
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

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public LocalDate getMinDate() {
        return minDate;
    }
}
