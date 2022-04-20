package domain.user;

import domain.shop.Product;

import java.util.Map;

public class Guest implements UserState{
    private Map<Integer, User> userList;
    @Override
    public void getInfo(Filter f) {

    }

    @Override
    public void searchProduct(Filter f) {

    }

    @Override
    public void checkCart() {

    }

    @Override
    public void checkout() {

    }

    @Override
    public void leaveMarket() {

    }
}
