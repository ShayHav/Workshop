package domain.shop.PurchasePolicys;
import java.util.ArrayList;
import java.util.List;

public class BlackListPolicy {
    private final List<String> userBlackList;
    private int purchaseRuleID;

    public BlackListPolicy(int purchaseRuleID){
        userBlackList = new ArrayList<>();
        this.purchaseRuleID = purchaseRuleID;
    }

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


    public int getID(){
        return purchaseRuleID;
    }
}
