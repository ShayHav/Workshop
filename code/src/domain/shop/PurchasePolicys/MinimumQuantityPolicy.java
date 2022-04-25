package domain.shop.PurchasePolicys;

public class MinimumQuantityPolicy implements PurchaseRule {
    private int minimumQuantity;

    public MinimumQuantityPolicy(int minimumQuantity) throws IllegalArgumentException{
        if(minimumQuantity <= 0)
            throw new IllegalArgumentException("invalid minimum quantity needed");
        this.minimumQuantity = minimumQuantity;
    }

    @Override
    public boolean purchaseAllowed(String userID, int amount) {
        return amount >= minimumQuantity;
    }

    public void editMinAmount(int newMin) throws IllegalArgumentException{
        if(newMin <= 0)
            throw new IllegalArgumentException("invalid minimum quantity needed");
        minimumQuantity = newMin;
    }

}
