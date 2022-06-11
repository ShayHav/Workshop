package domain.shop;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
public class Order {
    @Id
    private long orderId;
    private LocalDateTime buyingTime;
    @ManyToOne
    private String userID;
    @Embedded
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

    public String checkoutMessage() {
        StringBuilder message = new StringBuilder();
        message.append(String.format("%s bought:\n", userID));
        for(Product product: broughtItem){
            message.append(String.format("%d %s at price of %.2f$\n", product.getAmount(), product.getName(), product.getPrice()));
        }
        message.append(String.format("at total price: %.2f$\n", totalAmount));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        message.append(String.format("time of checkout: %s",buyingTime.format(formatter)));
        return message.toString();
    }
}
