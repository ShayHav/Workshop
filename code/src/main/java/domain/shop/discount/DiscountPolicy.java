package domain.shop.discount;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Tuple;
import domain.shop.Product;
import domain.shop.discount.Discount;

import java.util.*;
import java.util.logging.Level;

public class DiscountPolicy {

    /**
     *
     */
    private Map<Integer, List<Discount>> product_discounts;
    private Map<Integer, Discount> productGroup_discounts; ///check if needed
    private List<Integer> hasBundleDeal;
    private int discountIDCounter;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    public DiscountPolicy(){
        product_discounts = new HashMap<>();
        hasBundleDeal = new ArrayList<>();
        discountIDCounter = 1;
    }

    public void addDiscount(int prodID, Discount discount){
        List<Discount> prod_disc = product_discounts.get(prodID);
        if(prod_disc == null) {
            prod_disc = new ArrayList<>();
            product_discounts.put(prodID, prod_disc);
        }
        eventLogger.logMsg(Level.INFO, String.format("added the discount to product: %d ", prodID));
        prod_disc.add(discount);
    }

    public Map<Integer, List<Discount>> getProduct_Discounts() {
        return product_discounts;
    }

    public List<Discount> getAllDiscountsForProd(int prodID){
        for(Map.Entry<Integer, List<Discount>> set : product_discounts.entrySet()){
            if(set.getKey() == prodID){
                eventLogger.logMsg(Level.INFO, String.format("returned a list of discounts applicable to product: %d ", prodID));
                return set.getValue();
            }
        }
        eventLogger.logMsg(Level.INFO, String.format("product: %d has no discounts", prodID));
        return new ArrayList<>();
    }

    public double calcPricePerProduct(int prodID, Double basePrice, int amount){
        List<Discount> prodDiscounts = getAllDiscountsForProd(prodID);
        double pricePerProd = basePrice;
        for (Discount discount: prodDiscounts){
            discount.applyDiscount(pricePerProd, amount);
        }
        eventLogger.logMsg(Level.INFO, String.format("applied all applicable discounts on product: %d and returned the price per product", prodID));
        return pricePerProd;
    }

    public double calcPriceTotalForProd(int prodID, Double basePrice, int amount){
        double totalPrice = amount * calcPricePerProduct(prodID, basePrice, amount);
        eventLogger.logMsg(Level.INFO, String.format("returned the price of total price paid for product: %d after discounts ", prodID));
        return totalPrice;
    }

    public int addPercentageDiscount(int prodID, double percentage){
        try {
            Discount discount = new PercentageDiscount(percentage, discountIDCounter++);
            List<Discount> prod_disc;
            if(product_discounts.containsKey(prodID))
                prod_disc = getAllDiscountsForProd(prodID);
            else
                prod_disc = new ArrayList<>();
            eventLogger.logMsg(Level.INFO, String.format("added percentage discount to product: %d ", prodID));
            prod_disc.add(discount);
            return discount.getID();
        }
        catch (IllegalArgumentException iae){
            errorLogger.logMsg(Level.WARNING, String.format("percentage: %d is an illegal value ", percentage));
            return -1;
        }
    }




    public int addBundleDiscount(int prodID, int amountNeededToBuy, int amountGetFree){
        try {
            if (hasBundleDeal.contains(prodID)){
                eventLogger.logMsg(Level.INFO, String.format("product: %d already has a Bundle discount", prodID));
                return -1;
            }

            Discount discount = new BundleDiscount(amountGetFree, amountNeededToBuy, discountIDCounter++);
            List<Discount> prod_disc;

            if(product_discounts.containsKey(prodID))
                prod_disc = getAllDiscountsForProd(prodID);
            else {
                prod_disc = new ArrayList<>();
                product_discounts.put(prodID, prod_disc);
            }
            hasBundleDeal.add(prodID);
            eventLogger.logMsg(Level.INFO, String.format("added Bundle discount to product: %d ", prodID));
            prod_disc.add(discount);
            return discount.getID();
        }
        catch (IllegalArgumentException iae){
            return -1;
        }
    }

    public boolean removeDiscount(int discountID){
        for(Map.Entry<Integer, List<Discount>> set : product_discounts.entrySet()){
            List<Discount> prod_discounts = set.getValue();
            for(Discount dis: prod_discounts) {
                if (dis.getID() == discountID) {
                    prod_discounts.remove(dis);
                    eventLogger.logMsg(Level.INFO, String.format("removed discount: %d ", discountID));
                    if(dis instanceof BundleDiscount){
                        hasBundleDeal.remove(set.getKey());
                    }
                    return true;
                }
            }
        }
        eventLogger.logMsg(Level.INFO, String.format("no such discount in the shop: %d", discountID));
        return false;
    }
}
