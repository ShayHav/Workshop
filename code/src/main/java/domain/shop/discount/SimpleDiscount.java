package domain.shop.discount;

import domain.shop.ProductImp;

import java.util.Map;
import java.util.function.Predicate;

public class SimpleDiscount implements Discount{
    private DiscountCalculatorType discountCalc;
    private int discountID;
    private Predicate<ProductImp> relevantTo;
    private String stringed;

    public SimpleDiscount(DiscountCalculatorType discountCalc, int discountID, Predicate<ProductImp> relevantTo, String stringed){
        this.discountCalc = discountCalc;
        this.discountID = discountID;
        this.relevantTo = relevantTo;
        this.stringed = stringed;
    }

    @Override
    public int getID() {
        return discountID;
    }

    public boolean



    eligible(Basket productAmountList){
        return true;
    }

    @Override
    public Basket applyDiscountCalculator(Basket productToAmounts) {
        ProductImp product;
        for(Map.Entry<ProductImp, Integer> productInCart: productToAmounts.entrySet()){
            product = productInCart.getKey();
            if (relevant(product))
                product.setBasePrice(discountCalc.applyDiscount(product.getBasePrice()));
        }
        return productToAmounts;
    }

    public boolean relevant(ProductImp product){
        return relevantTo.test(product);
    }

    public String toString(){
        return stringed;
    }
}
