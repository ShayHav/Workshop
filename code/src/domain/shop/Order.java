package domain.shop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Order {
    private long orderId;
    private LocalDateTime buyingTime;
    private int userid;
    private List<Product> broughtItem;
    double totalAmount;

    public Order(List<Product> products,double totalAmount){
        buyingTime = LocalDateTime.now();
        broughtItem = products;
        this.totalAmount = totalAmount;

    }

    public LocalDateTime getBuyingTime() {
        return buyingTime;
    }

    public int getUserid() {
        return userid;
    }

    public List<Product> getBroughtItem() {
        return broughtItem;
    }
}
