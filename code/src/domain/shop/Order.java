package domain.shop;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Order {
    private long orderId;
    private LocalDateTime buyingTime;
    private int userID;
    private List<Product> broughtItem;
    double totalAmount;

    public Order(List<Product> products,double totalAmount, int userID){
        buyingTime = LocalDateTime.now();
        broughtItem = Collections.unmodifiableList(products);
        this.totalAmount = totalAmount;
        this.userID = userID;

    }

    public LocalDateTime getBuyingTime() {
        return buyingTime;
    }

    public int getUserID() {
        return userID;
    }

    public List<Product> getBroughtItem() {
        return broughtItem;
    }
}
