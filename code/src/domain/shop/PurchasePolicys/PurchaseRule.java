package domain.shop.PurchasePolicys;

public interface PurchaseRule {
    boolean purchaseAllowed(String userID, int amount);
}
