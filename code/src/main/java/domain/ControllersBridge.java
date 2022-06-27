package domain;

import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.shop.Order;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.Exceptions.ShopNotFoundException;
import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.shop.user.User;
import domain.shop.user.UserController;
import domain.shop.user.filter.Filter;

import java.util.List;
import java.util.Map;

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

    /**
     * get shop request is send to the userController
     * @param shopID
     * @return
     * @throws ShopNotFoundException
     */
    public Shop getShop(int shopID) throws ShopNotFoundException {
        return ShopController.getInstance().getShop(shopID);
    }
    //TODO: need to get inside

    /**
     * get user request is send to the userController
     * @param id - identifier
     * @return
     * @throws IncorrectIdentification
     * @throws BlankDataExc
     */
    public User getUser(String id) throws IncorrectIdentification, BlankDataExc {
        if(id == null || id.isEmpty())
            throw new BlankDataExc("id");
        return UserController.getInstance().getUser(id);
    }

    /**
     * a OrderHistory query is sent to shopController
     * @return
     */
    public Map<Shop, List<Order>> getOrderHistoryForShops(Filter<Order> f) throws ShopNotFoundException {
        return ShopController.getInstance().getOrderHistoryForShops(f);
    }

    /**
     * After checking that the user meets delete conditions, a request is sent to userController
     * @param targetUser
     * @throws InvalidSequenceOperationsExc
     */
    public void DismissalUser(String targetUser) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        if(ShopController.getInstance().canBeDismiss(targetUser))
            UserController.getInstance().deleteUserName(targetUser);
        throw new InvalidSequenceOperationsExc(String.format("user can't be dismiss: %s",targetUser));
    }

    /**
     * After checking that the Owner meets delete conditions, a request is sent to userController
     * @param userName
     * @param targetUser
     * @param shop
     * @throws InvalidSequenceOperationsExc
     * @throws ShopNotFoundException
     */
    public void DismissalOwner(String userName, String targetUser, int shop) throws InvalidSequenceOperationsExc, ShopNotFoundException, IncorrectIdentification, BlankDataExc {
        if(!ShopController.getInstance().DismissalOwner(userName,targetUser,shop))
            throw new InvalidSequenceOperationsExc(String.format("user can't be dismiss: %s", targetUser));

    }
}
