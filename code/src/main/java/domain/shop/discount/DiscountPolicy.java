package domain.shop.discount;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.CriticalInvariantException;
import domain.Exceptions.DiscountNotFoundException;
import domain.Exceptions.InvalidParamException;
import domain.shop.ProductImp;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DiscountPolicy {

    /**
     *
     */
    private Map<Integer, List<Discount>> product_discounts;
    private Map<String, List<Discount>> category_discounts; ///check if needed
    private List<Discount> shopAllProducts_discounts; ///check if needed
    private List<Integer> hasBundleDeal;
    private int discountIDCounter;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    public DiscountPolicy(){
        product_discounts = new HashMap<>();
        category_discounts = new HashMap<>();
        shopAllProducts_discounts = new ArrayList<>();
        hasBundleDeal = new ArrayList<>();
        discountIDCounter = 1;
    }


    /*public void addProductDiscount(int prodID, Discount discount){
        List<Discount> prod_disc = product_discounts.get(prodID);
        if(prod_disc == null) {
            prod_disc = new ArrayList<>();
            product_discounts.put(prodID, prod_disc);
        }
        eventLogger.logMsg(Level.INFO, String.format("added the discount to product: %d ", prodID));
        prod_disc.add(discount);
    }


    public void addCategoryDiscount(String category, Discount discount){
        List<Discount> category_discount = category_discounts.get(category);
        if(category_discount == null) {
            category_discount = new ArrayList<>();
            category_discounts.put(category, category_discount);
        }
        eventLogger.logMsg(Level.INFO, String.format("added the discount to category: %d ", category));
        category_discount.add(discount);
    }

    public void addShopAllProductsDiscount(Discount discount){
        eventLogger.logMsg(Level.INFO, String.format("added the discount to shop."));
        shopAllProducts_discounts.add(discount);
    }*/


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

    public List<Discount> getAllDiscountsForCategory(String category){
        for(Map.Entry<String, List<Discount>> set : category_discounts.entrySet()){
            if(set.getKey().equals(category)){
                eventLogger.logMsg(Level.INFO, String.format("returned a list of discounts applicable to product: %s ", category));
                return set.getValue();
            }
        }
        eventLogger.logMsg(Level.INFO, String.format("category: %s has no discounts", category));
        return new ArrayList<>();
    }

    /*public double calcPricePerProduct(int prodID, Double basePrice, Map<Integer, Integer> productAmountList, double cartBaseTotal, String category){
        List<Discount> prodDiscounts = getAllDiscountsForProd(prodID);
        prodDiscounts.addAll(getAllDiscountsForCategory(category));
        prodDiscounts.addAll(shopAllProducts_discounts);

        double pricePerProd = basePrice;
        for (Discount discount: prodDiscounts){
            pricePerProd = discount.applyDiscountCalculator(pricePerProd, productAmountList, cartBaseTotal);
        }

        eventLogger.logMsg(Level.INFO, String.format("applied all applicable discounts on product: %d and returned the price per product", prodID));
        return pricePerProd;
    }*/



    public Basket calcPricePerProductForCartTotal(Map<ProductImp, Integer> productsToAmounts){
        Basket basket = new Basket();

        for (Map.Entry<ProductImp, Integer> product_amount: productsToAmounts.entrySet()){
            basket.put(new ProductImp(product_amount.getKey()), product_amount.getValue());
        }

        List<Discount> prodDiscounts = new ArrayList<>();
        for(Map.Entry<ProductImp, Integer> set : productsToAmounts.entrySet())
            prodDiscounts.addAll(getAllDiscountsForProd(set.getKey().getId()));

        for(String category: basket.findAllDistinctCategories())
            prodDiscounts.addAll(getAllDiscountsForCategory(category));

        prodDiscounts.addAll(shopAllProducts_discounts);

        prodDiscounts = prodDiscounts.stream().distinct().collect(Collectors.toList());

        for(Discount discount: prodDiscounts){
            basket = discount.applyDiscountCalculator(basket);
        }

        return basket;

    }



    public int addSimpleProductDiscount(int prodID, double percentage) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        List<Discount> prod_disc;
        if(product_discounts.containsKey(prodID))
            prod_disc = getAllDiscountsForProd(prodID);
        else {
            prod_disc = new ArrayList<>();
            product_discounts.put(prodID, prod_disc);
        }
        eventLogger.logMsg(Level.INFO, String.format("added percentage discount to product: %d ", prodID));
        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == prodID;
        Discount newDiscount = new SimpleDiscount(discountCalc, discountIDCounter++, relevantTo);
        prod_disc.add(newDiscount);

        return newDiscount.getID();
    }


    public int addSimpleCategoryDiscount(String category, double percentage) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        List<Discount> category_discount;
        if(category_discounts.containsKey(category))
            category_discount = getAllDiscountsForCategory(category);
        else {
            category_discount = new ArrayList<>();
            category_discounts.put(category, category_discount);
        }
        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getCategory().equals(category);

        eventLogger.logMsg(Level.INFO, String.format("added percentage discount to product: %s ", category));
        Discount newDiscount = new SimpleDiscount(discountCalc, discountIDCounter++, relevantTo);
        category_discount.add(newDiscount);

        return newDiscount.getID();
    }

    public int addSimpleShopAllProductsDiscount(double percentage) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        eventLogger.logMsg(Level.INFO, "added the discount to shop.");
        Predicate<ProductImp> relevantTo = (productImp)-> true;
        Discount newDiscount = new SimpleDiscount(discountCalc, discountIDCounter++, relevantTo);
        shopAllProducts_discounts.add(newDiscount);
        return newDiscount.getID();
    }


    public int addConditionalProductDiscount(int prodID, double percentage, Predicate<Basket> pred) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        List<Discount> prod_disc;
        if(product_discounts.containsKey(prodID))
            prod_disc = getAllDiscountsForProd(prodID);
        else {
            prod_disc = new ArrayList<>();
            product_discounts.put(prodID, prod_disc);
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == prodID;
        eventLogger.logMsg(Level.INFO, String.format("added percentage discount to product: %d ", prodID));
        Discount newDiscount = new ConditionalDiscount(pred, discountCalc, discountIDCounter++, relevantTo);
        prod_disc.add(newDiscount);

        return newDiscount.getID();
    }





    public int addConditionalCategoryDiscount(String category, double percentage, Predicate<Basket> pred) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        List<Discount> category_discount;
        if(category_discounts.containsKey(category))
            category_discount = getAllDiscountsForCategory(category);
        else {
            category_discount = new ArrayList<>();
            category_discounts.put(category, category_discount);
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getCategory().equals(category);
        eventLogger.logMsg(Level.INFO, String.format("added percentage discount to product: %s ", category));
        Discount newDiscount = new ConditionalDiscount(pred, discountCalc, discountIDCounter++, relevantTo);
        category_discount.add(newDiscount);

        return newDiscount.getID();
    }

    public int addConditionalShopAllProductsDiscount(double percentage, Predicate<Basket> pred) throws InvalidParamException {
        DiscountCalculatorType discountCalc = new PercentageDiscount(percentage);
        eventLogger.logMsg(Level.INFO, "added the discount to shop.");
        Predicate<ProductImp> relevantTo = (productImp)-> true;
        Discount newDiscount = new ConditionalDiscount(pred, discountCalc, discountIDCounter++, relevantTo);
        shopAllProducts_discounts.add(newDiscount);

        return newDiscount.getID();
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


    public Discount getDiscount(int discountID) throws DiscountNotFoundException {
        Discount disc1 = getProdDiscount(discountID);
        if (disc1 != null) return disc1;

        disc1 = getCategoryDiscount(discountID);
        if (disc1 != null) return disc1;

        disc1 = getAllShopDiscount(discountID);
        if (disc1 != null) return disc1;
        throw new DiscountNotFoundException(String.format("discount:%d not found", discountID));
    }

    private Discount getAllShopDiscount(int discountID) {
        for (Discount disc: shopAllProducts_discounts){
            if(disc.getID() == discountID)
                return disc;
        }
        return null;
    }

    private Discount getCategoryDiscount(int discountID) {
        for(List<Discount> discounts: category_discounts.values()){
            for (Discount disc: discounts){
                if(disc.getID() == discountID)
                    return disc;
            }
        }
        return null;
    }

    private Discount getProdDiscount(int discountID) {
        for(List<Discount> discounts: product_discounts.values()){
            for (Discount disc: discounts){
                if(disc.getID() == discountID)
                    return disc;
            }
        }
        return null;
    }

    public int addAndDiscount(int discountID1, int discountID2) throws CriticalInvariantException, DiscountNotFoundException {
        int discID;
        try {
            discID = addComplexDiscount(discountID1, discountID2, "And");
        }catch (InvalidParamException invalidParamException){
            throw new CriticalInvariantException("fundamental error, the complex type sent here is Invalid");
        }
        return discID;
    }

    public int addOrDiscount(int discountID1, int discountID2) throws CriticalInvariantException, DiscountNotFoundException {
        int discID;
        try {
            discID = addComplexDiscount(discountID1, discountID2, "Or");
        }catch (InvalidParamException invalidParamException){
            throw new CriticalInvariantException("fundamental error, the complex type sent here is Invalid");
        }
        return discID;
    }

    public int addXorDiscount(int discountID1, int discountID2) throws CriticalInvariantException, DiscountNotFoundException {
        int discID;
        try {
            discID = addComplexDiscount(discountID1, discountID2, "Xor");
        }catch (InvalidParamException invalidParamException){
            throw new CriticalInvariantException("fundamental error, the complex type sent here is Invalid");
        }
        return discID;
    }



    public int addComplexDiscount(int discountID1, int discountID2, String complexType) throws InvalidParamException, DiscountNotFoundException {
        Discount discount1 = null;
        Discount discount2 = null;
        List<Discount> listOfDiscount1 = new ArrayList<>();
        List<Discount> listOfDiscount2 = new ArrayList<>();

        for (List<Discount> discounts : product_discounts.values()) {
            for (Discount disc : discounts) {
                if (disc.getID() == discountID1) {
                    discount1 = disc;
                    listOfDiscount1 = discounts;
                }
                if (disc.getID() == discountID2) {
                    discount2 = disc;
                    listOfDiscount2 = discounts;
                }
            }
        }

        if (discount1 == null || discount2 == null) {
            for (List<Discount> discounts : category_discounts.values()) {
                for (Discount disc : discounts) {
                    if (disc.getID() == discountID1) {
                        discount1 = disc;
                        listOfDiscount1 = discounts;
                    }
                    if (disc.getID() == discountID2) {
                        discount2 = disc;
                        listOfDiscount2 = discounts;
                    }
                }
            }
        }

        if (discount1 == null || discount2 == null) {
            for (Discount disc: shopAllProducts_discounts){
                if (disc.getID() == discountID1) {
                    discount1 = disc;
                    listOfDiscount1 = shopAllProducts_discounts;
                }
                if (disc.getID() == discountID2) {
                    discount2 = disc;
                    listOfDiscount2 = shopAllProducts_discounts;
                }
            }
        }

        if(discount1 == null || discount2 == null){
            throw new DiscountNotFoundException("one of the discounts given was not found.");
        }

        Discount newDiscount = switch (complexType) {
            case "And" -> new AndDiscount(discount1, discount2, discountIDCounter++);
            case "Or" -> new OrDiscount(discount1, discount2, discountIDCounter++);
            case "Xor" -> new XorDiscount(discount1, discount2, discountIDCounter++);
            default -> throw new InvalidParamException("Complex type given is illegal.");
        };


        listOfDiscount1.remove(discount1);
        listOfDiscount2.remove(discount2);
        listOfDiscount1.add(newDiscount);
        if(listOfDiscount1 != listOfDiscount2)
            listOfDiscount2.add(newDiscount);

        return newDiscount.getID();
    }
}
