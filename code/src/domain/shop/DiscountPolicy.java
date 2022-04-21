package domain.shop;

import domain.Tuple;
import domain.shop.discount.Discount;

import java.util.HashMap;
import java.util.Map;

public class DiscountPolicy {

    private Map<Integer ,Tuple<Integer, Discount>> product_discounts;
    private Map<Integer, Discount> productGroup_discounts;
    private int singleProdDiscounts_Counter;

    public DiscountPolicy(){
        product_discounts = new HashMap<>();
        singleProdDiscounts_Counter = 0;
    }

    public void addDiscount(int prodID, Discount discount){
        Tuple<Integer, Discount> prod_disc = new Tuple<>(prodID, discount);
        product_discounts.putIfAbsent(singleProdDiscounts_Counter++, prod_disc);
    }

    public Map<Integer, Discount> getProductGroup_discounts() {
        return productGroup_discounts;
    }


}
