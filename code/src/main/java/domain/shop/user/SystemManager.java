package domain.shop.user;

import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.ShopNotFoundException;
import domain.shop.Order;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.shop.user.filter.Filter;

import java.util.List;
import java.util.Map;

public class SystemManager extends User {

    public Map<Shop,List<Order>> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) throws ShopNotFoundException {
        ShopController sc = ShopController.getInstance();
        return sc.getOrderHistoryForShops(f);
    }

    public Map<User, List<Order>> getOrderHistoryForUser(Filter<Order> f, List<String>  userID) throws InvalidAuthorizationException {
        UserController uc = UserController.getInstance();
        return uc.getOrderHistoryForUsers(f);

    }
}
