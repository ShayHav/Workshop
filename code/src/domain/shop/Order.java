package domain.shop;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Order {
    private long orderId;
    private LocalDateTime buyingTime;
    private String userID;
    private List<Product> broughtItem;
    private double totalAmount;
    private OrderInfo oi;


    public Order(List<Product> products,double totalAmount, String userID){
        buyingTime = LocalDateTime.now();
        broughtItem = Collections.unmodifiableList(products);
        this.totalAmount = totalAmount;
        this.userID = userID;

    }

    public Order(OrderInfo oi) { this.oi = oi;}

    public LocalDateTime getBuyingTime() {
        return buyingTime;
    }

    public String getUserID() {
        return userID;
    }

    public List<Product> getBroughtItem() {
        return broughtItem;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long id){
        orderId = id;
    }

    public OrderInfo getOrderInfo(){ return oi;}
}
