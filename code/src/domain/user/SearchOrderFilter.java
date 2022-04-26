package domain.user;

import domain.shop.Order;
import domain.shop.ProductInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchOrderFilter implements Filter<Order> {
    Double minPrice;
    Double maxPrice;
    LocalDate minDate;
    LocalDate maxDate;


    public SearchOrderFilter(Double minPrice, Double maxPrice, LocalDate minDate, LocalDate maxDate) {
        this.minPrice = Objects.requireNonNullElse(minPrice, 0d);
        this.maxPrice = Objects.requireNonNullElse(maxPrice, Double.MAX_VALUE);
        if (minDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("min date cannot be later than today");
        this.minDate = minDate;
        if (maxDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("max date cannot be later than today");
        this.maxDate = maxDate;
    }

    public List<Order> applyFilter(List<Order> orders) {
        List<Order> result = new ArrayList<>();
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
