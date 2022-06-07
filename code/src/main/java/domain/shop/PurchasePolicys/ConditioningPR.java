package domain.shop.PurchasePolicys;


import domain.shop.ProductImp;
import domain.shop.discount.Basket;

public class ConditioningPR implements PurchaseRule{
    PurchaseRule ifPR;
    PurchaseRule thenPR;
    int purchaseRuleID;


    public ConditioningPR(PurchaseRule ifPR, PurchaseRule thenPR, int purchaseRuleID){
        this.ifPR = ifPR;
        this.thenPR = thenPR;
        this.purchaseRuleID = purchaseRuleID;
    }

    @Override
    public boolean purchaseAllowed(Basket productAmountList) {
        if(ifPR.purchaseAllowed(productAmountList))
            return thenPR.purchaseAllowed(productAmountList);
        return false;
    }


    public int getID(){
        return purchaseRuleID;
    }

    public boolean relevant(ProductImp product){
        return ifPR.relevant(product) || thenPR.relevant(product);
    }

    public String toString(){
        return String.format("(if %s then if %s)",ifPR.toString(), thenPR.toString());
    }
}
