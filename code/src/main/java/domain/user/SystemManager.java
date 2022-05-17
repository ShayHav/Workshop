package domain.user;

import domain.shop.Order;
import domain.user.filter.Filter;

import java.util.List;

public class SystemManager extends User {

    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) {
        ShopController sc = ShopController.getInstance();
        List<Order> result = sc.getOrderHistoryForShops(shopID);
        return f.applyFilter(result);
    }

    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String>  userID){
        UserController uc = UserController.getInstance();
        List<Order> result = uc.getOrderHistoryForUser(userID);
        return f.applyFilter(result);
    }
}
