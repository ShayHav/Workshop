package domain.shop.user;

import domain.shop.Shop;

import javax.persistence.Id;

public class OwnerAppointment {
    private Shop shop;
    private String appointeeId;
    private User appointed;


    public OwnerAppointment(Shop shop,String appointee , User appointed){
        this.appointed=appointed;
        this.shop = shop;
        this.appointeeId =appointee;
    }

    public User getAppointed() {
        return appointed;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getAppointeeId() {
        return appointeeId;
    }

    public void setAppointeeId(String appointeeId) {
        this.appointeeId = appointeeId;
    }

    public void setAppointed(User appointed) {
        this.appointed = appointed;
    }
}
