package domain.shop.PurchasePolicys;

import domain.shop.discount.BundleDiscount;
import domain.shop.discount.Discount;
import domain.shop.discount.PercentageDiscount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchasePolicy {

    private Map<Integer, List<PurchaseRule>> product_purchasePolicies;
    private Map<Integer, Discount> productGroup_discounts; ///check if needed


    public PurchasePolicy(){
        product_purchasePolicies = new HashMap<>();
    }


    public Map<Integer, List<PurchaseRule>> getProductGroup_discounts() {
        return product_purchasePolicies;
    }

    public List<PurchaseRule> getAllPurchaseRulesForProd(int prodID){
        for(Map.Entry<Integer, List<PurchaseRule>> set : product_purchasePolicies.entrySet()){
            if(set.getKey() == prodID){
                return set.getValue();
            }
        }
        return new ArrayList<>();
    }

    public boolean checkIfProductRulesAreMet(String userID ,int prodID, Double basePrice, int amount){
        List<PurchaseRule> purchaseRuleList = getAllPurchaseRulesForProd(prodID);
        boolean allowed = true;
        for (PurchaseRule pr: purchaseRuleList){
            allowed  = pr.purchaseAllowed(userID, amount);
            if(!allowed)
                return false;
        }
        return allowed;
    }

    public boolean addQuantityRule(int prodID, int minQuantity){
        try {
            PurchaseRule pr = new MinimumQuantityPolicy(minQuantity);
            List<PurchaseRule> prod_pr;
            if(product_purchasePolicies.containsKey(prodID))
                return false;
            else
                prod_pr = new ArrayList<>();
            prod_pr.add(pr);
            return true;
        }
        catch (IllegalArgumentException iae){
            return false;
        }
    }

}
