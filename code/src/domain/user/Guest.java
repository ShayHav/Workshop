package domain.user;

import domain.shop.Product;

public class Guest implements UserState{
    @Override
    public void getInfo(Filter f) {

    }

    @Override
    public void searchProduct(Filter f) {

    }

    @Override
    public void addProductToCart(String shop, Product p, int amount) {

    }

    @Override
    public void checkCart() {

    }

    @Override
    public void checkout() {

    }
}
