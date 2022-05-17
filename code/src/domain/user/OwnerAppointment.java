package domain.user;

import domain.shop.Shop;
import domain.user.User;

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
}