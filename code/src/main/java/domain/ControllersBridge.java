package domain;

import domain.shop.Order;
import domain.Exceptions.ShopNotFoundException;
import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.user.User;
import domain.user.UserController;

import java.util.List;

public class ControllersBridge {
    private static ControllersBridge instance = null;

    private ControllersBridge(){

    }
    public static ControllersBridge getInstance() {
        if (instance == null) {
            instance = new ControllersBridge();
        }
        return instance;
    }

    //TODO: need to get inside
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }
    //TODO: need to get inside
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if(id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    public List<Order> getOrderHistoryForShops(List<Integer> shopId){
        return ShopController.getInstance().getOrderHistoryForShops(shopId);
    }
}
