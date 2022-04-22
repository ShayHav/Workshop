package domain.shop.PurchasePolicys;

import java.util.ArrayList;
import java.util.List;

public class BlackListPolicy implements PurchaseRule {
    private final List<Integer> userBlackList;

    public BlackListPolicy(){
        userBlackList = new ArrayList<>();
    }

    @Override
    public boolean purchaseAllowed(int userID, int amount) {
        return !(userBlackList.contains(userID));
    }

    public void blackListUser(int userID) {
        for(Integer ID: userBlackList) {
            if (ID == userID)
                return;
        }
        userBlackList.add(userID);
    }

    public void noLongerBlackListed(int userID) {
        userBlackList.removeIf(ID -> ID == userID);
    }

}
