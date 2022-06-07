package domain.shop.PurchasePolicys;


import domain.shop.ProductImp;
import domain.shop.discount.Basket;

import java.util.function.Predicate;

public class PurchaseRuleIMPL implements PurchaseRule {
    private int purchaseRuleID;
    private Predicate<Basket> eligibility;
    private Predicate<ProductImp> relevantTo;


    public PurchaseRuleIMPL(Predicate<Basket> eligibility, Predicate<ProductImp> relevantTo, int purchaseRuleID) throws IllegalArgumentException{
        this.eligibility = eligibility;
        this.relevantTo = relevantTo;
        this.purchaseRuleID = purchaseRuleID;
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
}
