package domain.user;

import domain.Tuple;
import domain.shop.Product;

import java.util.List;
import java.util.Map;

public class Guest implements UserState {


    @Override
    public List<String> getInfoOfShops() {
        return null;
    }

    @Override
    public List<Tuple<String, String>> getInfoOfProductInShop(int shopID) {
        return null;
    }

    //TODO: remind shay to add rank to shop and product
    @Override
    public void searchProductByName(String name, Filter f) {

    }

    public void searchProductByCategory(String category, Filter f) {

    }

    public void searchProductByKeyword(String keyword, Filter f) {

    }


    @Override
    public void checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate){
        c.checkout(id,fullName,address,phoneNumber,cardNumber,expirationDate);
    }


}
