package domain.shop.PurchasePolicys;


import domain.shop.ProductImp;
import domain.shop.discount.Basket;

public class OrPR implements PurchaseRule{
    PurchaseRule pr1;
    PurchaseRule pr2;
    int purchaseRuleID;


    public OrPR(PurchaseRule pr1, PurchaseRule pr2, int purchaseRuleID){
        this.pr1 = pr1;
        this.pr2 = pr2;
        this.purchaseRuleID = purchaseRuleID;
    }

    @Override
    public boolean purchaseAllowed(Basket productAmountList) {
        return pr1.purchaseAllowed(productAmountList) || pr2.purchaseAllowed(productAmountList);
    }


    public int getID(){
        return purchaseRuleID;
    }

    public boolean relevant(ProductImp product){
        return pr1.relevant(product) || pr2.relevant(product);
    }

    public String toString(){
        return String.format("(%s and\\or %s)", pr1.toString(), pr2.toString());
    }
}
