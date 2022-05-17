package Presentation.Model;

public class AddToCartMessage {
    private String username;
    private int shopID;
    private int serialNumber;

    public AddToCartMessage(){

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
}
