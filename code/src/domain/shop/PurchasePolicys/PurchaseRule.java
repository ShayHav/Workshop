package domain.shop.PurchasePolicys;

public interface PurchaseRule {
    boolean purchaseAllowed(int userID, int amount);
}
