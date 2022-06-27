package domain.shop.PurchasePolicys;


import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.CriticalInvariantException;
import domain.Exceptions.InvalidParamException;
import domain.Exceptions.PurchaseRuleNotFoundException;
import domain.shop.ProductImp;
import domain.shop.discount.Basket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PurchasePolicy {

    private final Map<Integer, List<PurchaseRule>> product_purchaseRules;
    private final Map<String, List<PurchaseRule>> category_purchaseRules;
    private final List<PurchaseRule> general_PurchaseRules;
    private int purchaseRuleIDCounter;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    public PurchasePolicy(){
        product_purchaseRules = new HashMap<>();
        category_purchaseRules = new HashMap<>();
        general_PurchaseRules = new ArrayList<>();
        purchaseRuleIDCounter = 1;
    }


    public Map<Integer, List<PurchaseRule>> getProductGroup_prs() {
        return product_purchaseRules;
    }

    public List<PurchaseRule> getAllPurchaseRulesForProd(int prodID){
        List<PurchaseRule> product_PR =  product_purchaseRules.get(prodID);
        if(product_PR != null)
            return product_PR;
        return new ArrayList<>();
    }

    public List<PurchaseRule> getAllPurchaseRulesForCategory(String category){
        List<PurchaseRule> product_PR =  category_purchaseRules.get(category);
        if(product_PR != null)
            return product_PR;
        return new ArrayList<>();
    }

    public List<PurchaseRule> getAllGeneralPurchaseRules(){
        return general_PurchaseRules;
    }


    public boolean checkCart_RulesAreMet(Basket productsToAmounts){

        Basket basket = new Basket(productsToAmounts);

        List<PurchaseRule> prodPR = new ArrayList<>();
        for(Map.Entry<ProductImp, Integer> set : productsToAmounts.entrySet())
            prodPR.addAll(getAllPurchaseRulesForProd(set.getKey().getId()));

        for(String category: basket.findAllDistinctCategories())
            prodPR.addAll(getAllPurchaseRulesForCategory(category));

        prodPR.addAll(general_PurchaseRules);

        prodPR = prodPR.stream().distinct().collect(Collectors.toList());

        boolean allowed;
        for(PurchaseRule pr: prodPR){
            allowed  = pr.purchaseAllowed(basket);
            if(!allowed)
                return false;
        }
        return true;
    }



    public int addProductPurchaseRule(int prodID, Predicate<Basket> eligible, String productName) {
        List<PurchaseRule> prod_pr;
        if(product_purchaseRules.containsKey(prodID))
            prod_pr = getAllPurchaseRulesForProd(prodID);
        else {
            prod_pr = new ArrayList<>();
            synchronized (product_purchaseRules) {
                product_purchaseRules.put(prodID, prod_pr);
            }
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == prodID;
        eventLogger.logMsg(Level.INFO, String.format("added percentage pr to product: %d ", prodID));
        String purchaseRuleStringed = String.format("product %s can only be purchased if %s", productName, eligible.toString());
        PurchaseRule newPurchaseRule = new PurchaseRuleIMPL(eligible, relevantTo, purchaseRuleIDCounter++, purchaseRuleStringed);
        prod_pr.add(newPurchaseRule);

        return newPurchaseRule.getID();
    }




    public int addCategoryPurchaseRule(String category, Predicate<Basket> eligible) {
        List<PurchaseRule> category_pr;
        if(category_purchaseRules.containsKey(category))
            category_pr = getAllPurchaseRulesForCategory(category);
        else {
            category_pr = new ArrayList<>();
            synchronized (category_purchaseRules) {
                category_purchaseRules.put(category, category_pr);
            }
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getCategory().equals(category);
        eventLogger.logMsg(Level.INFO, String.format("added percentage pr to product: %s ", category));
        String purchaseRuleStringed = String.format("product from category %s can only be purchased if %s", category, eligible.toString());
        PurchaseRule newPurchaseRule = new PurchaseRuleIMPL(eligible, relevantTo, purchaseRuleIDCounter++, purchaseRuleStringed);
        category_pr.add(newPurchaseRule);

        return newPurchaseRule.getID();
    }

    public int addGeneralShopPurchaseRule(Predicate<Basket> eligible) {
        Predicate<ProductImp> relevantTo = (productImp)-> true;
        String purchaseRuleStringed = String.format("Any of the shop's products can only be purchased if %s", eligible.toString());
        PurchaseRule newPurchaseRule = new PurchaseRuleIMPL(eligible, relevantTo, purchaseRuleIDCounter++, purchaseRuleStringed);
        synchronized (general_PurchaseRules) {
            general_PurchaseRules.add(newPurchaseRule);
        }
        return newPurchaseRule.getID();
    }


    public int addAndPR(int prID1, int prID2) throws CriticalInvariantException, PurchaseRuleNotFoundException {
        int prID;
        try {
            prID = addComplexPR(prID1, prID2, "And");
        }catch (InvalidParamException invalidParamException){
            throw new CriticalInvariantException("fundamental error, the complex type sent here is Invalid");
        }
        return prID;
    }

    public int addOrPR(int prID1, int prID2) throws CriticalInvariantException, PurchaseRuleNotFoundException {
        int prID;
        try {
            prID = addComplexPR(prID1, prID2, "Or");
        }catch (InvalidParamException invalidParamException){
            throw new CriticalInvariantException("fundamental error, the complex type sent here is Invalid");
        }
        return prID;
    }




    public int addComplexPR(int prID1, int prID2, String complexType) throws InvalidParamException, PurchaseRuleNotFoundException {
        PurchaseRule pr1 = null;
        PurchaseRule pr2 = null;
        List<PurchaseRule> listOfPurchaseRule1 = new ArrayList<>();
        List<PurchaseRule> listOfPurchaseRule2 = new ArrayList<>();

        for (List<PurchaseRule> purchaseRules : product_purchaseRules.values()) {
            for (PurchaseRule purchaseRule : purchaseRules) {
                if (purchaseRule.getID() == prID1) {
                    pr1 = purchaseRule;
                    listOfPurchaseRule1 = purchaseRules;
                }
                if (purchaseRule.getID() == prID2) {
                    pr2 = purchaseRule;
                    listOfPurchaseRule2 = purchaseRules;
                }
            }
        }

        if (pr1 == null || pr2 == null) {
            for (List<PurchaseRule> purchaseRules : category_purchaseRules.values()) {
                for (PurchaseRule purchaseRule : purchaseRules) {
                    if (purchaseRule.getID() == prID1) {
                        pr1 = purchaseRule;
                        listOfPurchaseRule1 = purchaseRules;
                    }
                    if (purchaseRule.getID() == prID2) {
                        pr2 = purchaseRule;
                        listOfPurchaseRule2 = purchaseRules;
                    }
                }
            }
        }

        if (pr1 == null || pr2 == null) {
            for (PurchaseRule purchaseRule: general_PurchaseRules){
                if (purchaseRule.getID() == prID1) {
                    pr1 = purchaseRule;
                    listOfPurchaseRule1 = general_PurchaseRules;
                }
                if (purchaseRule.getID() == prID2) {
                    pr2 = purchaseRule;
                    listOfPurchaseRule2 = general_PurchaseRules;
                }
            }
        }

        if(pr1 == null || pr2 == null){
            throw new PurchaseRuleNotFoundException("one of the purchase rules given was not found.");
        }

        PurchaseRule newPurchaseRule = switch (complexType) {
            case "And" -> new AndPR(pr1, pr2, purchaseRuleIDCounter++);
            case "Or" -> new OrPR(pr1, pr2, purchaseRuleIDCounter++);
            default -> throw new InvalidParamException("Complex type given is illegal.");
        };


        listOfPurchaseRule1.remove(pr1);
        listOfPurchaseRule2.remove(pr2);
        listOfPurchaseRule1.add(newPurchaseRule);
        if(listOfPurchaseRule1 != listOfPurchaseRule2)
            listOfPurchaseRule2.add(newPurchaseRule);

        return newPurchaseRule.getID();
    }


    public void removePurchaseRule(int purchaseRuleID){
        for(Map.Entry<Integer, List<PurchaseRule>> set : product_purchaseRules.entrySet()){
            List<PurchaseRule> prod_pr = set.getValue();
            for(PurchaseRule pr: prod_pr) {
                if (pr.getID() == purchaseRuleID) {
                    prod_pr.remove(pr);
                    eventLogger.logMsg(Level.INFO, String.format("removed discount: %d ", purchaseRuleID));
                    break;
                }
            }
        }

        for(Map.Entry<String, List<PurchaseRule>> set : category_purchaseRules.entrySet()){
            List<PurchaseRule> prod_pr = set.getValue();
            for(PurchaseRule pr: prod_pr) {
                if (pr.getID() == purchaseRuleID) {
                    prod_pr.remove(pr);
                    eventLogger.logMsg(Level.INFO, String.format("removed discount: %d ", purchaseRuleID));
                    break;
                }
            }
        }

        for (PurchaseRule pr : general_PurchaseRules){
            if (pr.getID() == purchaseRuleID) {
                general_PurchaseRules.remove(pr);
                eventLogger.logMsg(Level.INFO, String.format("removed discount: %d ", purchaseRuleID));
                break;
            }
        }
        eventLogger.logMsg(Level.INFO, String.format("no such discount in the shop: %d", purchaseRuleID));
    }

    public List<PurchaseRule> getAllDistinctPurchaseRules(){
        List<PurchaseRule> prodPR = new ArrayList<>();
        for(List<PurchaseRule> valueSet : product_purchaseRules.values())
            prodPR.addAll(valueSet);


        for(List<PurchaseRule> valueSet : category_purchaseRules.values())
            prodPR.addAll(valueSet);
        prodPR.addAll(general_PurchaseRules);

        return prodPR.stream().distinct().collect(Collectors.toList());
    }

}
