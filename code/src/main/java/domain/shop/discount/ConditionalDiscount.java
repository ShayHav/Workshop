package domain.shop.discount;import main.java.domain.shop.ProductImp;

import java.util.Map;
import java.util.function.Predicate;


public class ConditionalDiscount implements Discount {
    private Predicate predicate;
    private DiscountCalculatorType discountCalc;
    private int discountID;
    private Predicate<ProductImp> relevantTo;


    public ConditionalDiscount(Predicate<Basket> predicate, DiscountCalculatorType discountCalc, int discountID, Predicate<ProductImp> relevantTo){
        this.predicate = predicate;
        this.discountCalc = discountCalc;
        this.discountID = discountID;
        this.relevantTo = relevantTo;
    }



    @Override
    public int getID() {
        return discountID;
    }

    @Override
    public boolean eligible(Basket productAmountList) {
        return predicate.test(productAmountList);
    }

    @Override
    public Basket applyDiscountCalculator(Basket productToAmounts) {
        ProductImp product;
        for(Map.Entry<ProductImp, Integer> productInCart: productToAmounts.entrySet()){
            product = productInCart.getKey();
            if (relevant(product)) {
                if(eligible(productToAmounts))
                    product.setBasePrice(discountCalc.applyDiscount(product.getBasePrice()));
            }
        }
        return productToAmounts;
    }

    public boolean relevant(ProductImp product){
        return relevantTo.test(product);
    }


}
