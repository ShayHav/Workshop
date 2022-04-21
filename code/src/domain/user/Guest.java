package domain.user;

import domain.shop.Product;

import java.util.Map;

public class Guest implements UserState {

    @Override
    public void getInfo(Filter f) {

    }

    @Override
    public void searchProduct(Filter f) {

    }


    @Override
    public void checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate){
        c.checkout(id,fullName,address,phoneNumber,cardNumber,expirationDate);
    }


}
