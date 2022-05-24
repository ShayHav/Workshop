package domain.user;

import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.ShopNotFoundException;
import domain.shop.Order;
import domain.shop.ShopController;
import domain.user.filter.Filter;

import java.util.List;

public class SystemManager extends User {

    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) throws ShopNotFoundException {
        ShopController sc = ShopController.getInstance();
        List<Order> result = sc.getOrderHistoryForShops();
        return f.applyFilter(result);
    }

    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String>  userID) throws InvalidAuthorizationException {
        UserController uc = UserController.getInstance();
        List<Order> result = uc.getOrderHistoryForUsers();
        return f.applyFilter(result);
    }
}
