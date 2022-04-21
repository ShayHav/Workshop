package domain.shop.discount;

public class BundleDiscount {

    int amountToBuyNeeded;
    int amountToGetFree;

    public BundleDiscount(int amountToBuy, int amountToGet) throws IllegalArgumentException {
        if(amountToBuy > 0)
            this.amountToBuyNeeded = amountToBuy;
        else
            throw new IllegalArgumentException("inviable amount of products to buy needed");

        if(amountToGet > 0)
            this.amountToGetFree = amountToGet;
        else
            throw new IllegalArgumentException("inviable amount of items to get when discount applied");
    }

    public double applyDiscount(double price, int amount) {
        int numberOfApplies = (int)(amount/ amountToBuyNeeded);
        double toPay = (amount - (numberOfApplies * amountToGetFree)) * price;
        return toPay;
    }
}
