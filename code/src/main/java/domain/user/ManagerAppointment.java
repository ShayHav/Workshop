package domain.user;

import domain.shop.Shop;

public class ManagerAppointment {
    private Shop shop;
    private User appointee; // the user who decide to appoint the new manager
    private User appointed; // the user whe got appointed to be a new manager

    public ManagerAppointment(Shop shop,User appointee , User appointed){
        this.appointed=appointed;
        this.shop = shop;
        this.appointee =appointee;
    }

    public User getAppointed() {
        return appointed;
    }

    public Shop getShop() {
        return shop;
    }

    public User getAppointee(){return appointee;}
}
