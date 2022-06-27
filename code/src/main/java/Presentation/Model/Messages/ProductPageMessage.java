package Presentation.Model.Messages;

public class ProductPageMessage {
    private String type;
    private String username;
    private int shopID;
    private int serialNumber;
    private int quantity;
    private double price;

    public ProductPageMessage(){

    }

    public int getQuantity() {
        return quantity;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getUsername() {
        return username;
    }

    public int getShopID() {
        return shopID;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
