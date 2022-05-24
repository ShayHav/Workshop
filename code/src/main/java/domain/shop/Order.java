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
    private int shopID;
    private String shopName;


    public Order(List<Product> products,double totalAmount, String userID, int shopID, String shopName){
        buyingTime = LocalDateTime.now();
        broughtItem = Collections.unmodifiableList(products);
        this.totalAmount = totalAmount;
        this.userID = userID;
        this.shopID = shopID;
        this.shopName = shopName;
    }

    public int getShopID() {
        return shopID;
    }
    public String getShopName() {
        return shopName;
    }

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

    public double getTotalAmount(){
        return totalAmount;
    }
}
