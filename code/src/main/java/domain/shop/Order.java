package domain.shop;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
public class Order {
    @Id
    private long orderId;
    private LocalDateTime buyingTime;
    private String userID;
    @Transient
    private List<Product> broughtItem;

    @ElementCollection
    private List<ProductHistory> itemsLs;
    private double totalAmount;
    private int shopID;
    private String shopName;

    public void setBuyingTime(LocalDateTime buyingTime) {
        this.buyingTime = buyingTime;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setBroughtItem(List<Product> broughtItem) {
        this.broughtItem = broughtItem;
    }

    public List<ProductHistory> getItemsLs() {
        return itemsLs;
    }

    public void setItemsLs(List<ProductHistory> itemsLs) {
        this.itemsLs = itemsLs;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Order(List<Product> products, double totalAmount, String userID, int shopID, String shopName){
        buyingTime = LocalDateTime.now();
        broughtItem = Collections.unmodifiableList(products);
        this.totalAmount = totalAmount;
        this.userID = userID;
        this.shopID = shopID;
        this.shopName = shopName;
        itemsLs = new ArrayList<>();
    }

    public Order() {

    }

    public Order merge(Order o)
    {
        setOrderId(o.orderId);
        setBroughtItem(new ArrayList<>());
        setBuyingTime(o.getBuyingTime());
        setItemsLs(o.getItemsLs());
        setShopName(o.getShopName());
        setUserID(o.getUserID());
        setTotalAmount(o.getTotalAmount());
        setShopID(o.getShopID());
        return this;

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

    public void initLs()
    {
        for(Product p : broughtItem)
        {
            if(p instanceof ProductHistory)
                itemsLs.add((ProductHistory) p);
        }
    }

    public void cleanLs()
    {
        itemsLs = new ArrayList<>();
    }

    public void convertLs()
    {
        broughtItem = new ArrayList<>();
        broughtItem.addAll(itemsLs);
    }
}
