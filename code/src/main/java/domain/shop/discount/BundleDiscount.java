package domain.shop.discount;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;

import java.util.logging.Level;

public class BundleDiscount implements Discount {
    private int discountID;
    private int amountToBuyNeeded;
    private int amountToGetFree;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();



    public BundleDiscount(int amountToBuy, int amountToGet, int discountID) throws IllegalArgumentException {
        if(amountToBuy > 0)
            this.amountToBuyNeeded = amountToBuy;
        else {
            errorLogger.logMsg(Level.WARNING, String.format("inviable amount of products needed to buy: %d ", amountToBuy));
            throw new IllegalArgumentException("inviable amount of products needed to buy");
        }
        if(amountToGet > 0)
            this.amountToGetFree = amountToGet;
        else {
            errorLogger.logMsg(Level.WARNING, String.format("inviable amount of products to get free: %d ", amountToGet));
            throw new IllegalArgumentException("inviable amount of items to get when discount applied");
        }
        this.discountID = discountID;
    }

    public double applyDiscount(double price, int amount) {
        int numberOfApplies = (int)(amount/ (amountToBuyNeeded + amountToGetFree));
        double toPayPerProduct = ((amount - (numberOfApplies * amountToGetFree)) * price)/amount;
        return toPayPerProduct;
    }

    public int getID(){
        return discountID;
    }
}
