package domain.user;

import domain.shop.Shop;
import domain.user.User;

public class ManagerAppointment {
    private Shop shop;
    private int appointeeId;
    private User appointed;

    public ManagerAppointment(Shop shop,int appointee , User appointed){
        this.appointed=appointed;
        this.shop = shop;
        this.appointeeId =appointee;
    }

    public User getAppointed() {
        return appointed;
    }
}
