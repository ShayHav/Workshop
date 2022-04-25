package domain.shop.PurchasePolicys;

import java.util.ArrayList;
import java.util.List;

public class BlackListPolicy implements PurchaseRule {
    private final List<String> userBlackList;

    public BlackListPolicy(){
        userBlackList = new ArrayList<>();
    }

    @Override
    public boolean purchaseAllowed(String userID, int amount) {
        return !(userBlackList.contains(userID));
    }

    public void blackListUser(String userID) {
        for(String ID: userBlackList) {
            if (ID.equals(userID))
                return;
        }
        userBlackList.add(userID);
    }

    public void noLongerBlackListed(int userID) {
        userBlackList.removeIf(ID -> ID.equals(userID));
    }

}
