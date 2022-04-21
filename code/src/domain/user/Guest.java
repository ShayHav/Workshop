package domain.user;

import domain.Tuple;
import domain.shop.ManagerAppointment;
import domain.shop.OwnerAppointment;
import domain.shop.Product;
import domain.shop.Shop;

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

    @Override
    public void searchProduct(Filter f) {

    }

    //TODO: remind shay to add rank to shop and product
    @Override
    public void searchProductByName(String name, Filter f) {

    }
    @Override
    public void searchProductByCategory(String category, Filter f) {

    }
    @Override
    public void searchProductByKeyword(String keyword, Filter f) {

    }


    @Override
    public void checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate){
        c.checkout(id,fullName,address,phoneNumber,cardNumber,expirationDate);
    }

    @Override
    public void leaveMarket(Cart cart) {

    }

    @Override
    public void createShop(Shop shop, int id) {

    }

    @Override
    public void appointOwner(User user, Shop shop, int id, List<OwnerAppointment> ownerAppointmentList) {

    }

    @Override
    public void appointManager(User user, Shop shop, int id, List<ManagerAppointment> managerAppointmentList) {

    }

    @Override
    public void closeShop(Shop shop, int id) {

    }


}
