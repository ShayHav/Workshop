package domain.shop.PurchasePolicys;


import domain.shop.ProductImp;
import domain.shop.discount.Basket;

import java.util.function.Predicate;

public class PurchaseRuleIMPL implements PurchaseRule {
    private int purchaseRuleID;
    private Predicate<Basket> eligibility;
    private Predicate<ProductImp> relevantTo;
    private String stringed;

    public PurchaseRuleIMPL(Predicate<Basket> eligibility, Predicate<ProductImp> relevantTo, int purchaseRuleID, String stringed) throws IllegalArgumentException{
        this.eligibility = eligibility;
        this.relevantTo = relevantTo;
        this.purchaseRuleID = purchaseRuleID;
        this.stringed = stringed;
    }

    @Override
    public boolean purchaseAllowed(Basket productToAmounts) {
        return eligibility.test(productToAmounts);
    }

    public int getID(){
        return purchaseRuleID;
    }

    public boolean relevant(ProductImp product){
        return relevantTo.test(product);
    }

    public String toString(){
        return stringed;
    }
}
