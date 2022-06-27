package domain.shop.purchasePolicy;

import domain.Exceptions.AccessDeniedException;
import domain.Exceptions.CriticalInvariantException;
import domain.Exceptions.InvalidParamException;
import domain.Exceptions.PurchaseRuleNotFoundException;
import domain.shop.ProductImp;
import domain.shop.PurchasePolicys.*;
import domain.shop.discount.Basket;
import domain.shop.predicate.PRPredType;
import domain.shop.predicate.PredicateManager;
import domain.shop.predicate.ToBuildPRPredicateFrom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PurchasePolicyTest {
    PurchasePolicy pp;
    ProductImp mockProd1;
    ProductImp mockProd2;

    @BeforeEach
    void setUp() {
        pp = new PurchasePolicy();
    }





    private void setupProducts(){
        mockProd1 = mock(ProductImp.class);
        mockProd2 = mock(ProductImp.class);

        when(mockProd1.getId()).thenReturn(1);
        when(mockProd1.getCategory()).thenReturn("dairy");
        when(mockProd1.getName()).thenReturn("pizza");

        when(mockProd2.getId()).thenReturn(2);
        when(mockProd2.getCategory()).thenReturn("alcohol");
        when(mockProd2.getName()).thenReturn("tequila");
    }


    /**has atleast 5 of productID 2 in cart if buying productID 1;*/
    void setupPred1(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(5, 2, "pizza", PRPredType.MaximumAmount);
        Predicate<Basket> elig;

        try {
            elig = PredicateManager.createMinimumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }
        pp.addProductPurchaseRule(1, elig, "apple");
    }


    /**has at least 1 of productID 2 if buying dairy*/
    void setupPred2(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(1, 2, "pizza", PRPredType.MaximumAmount);
        Predicate<Basket> elig;

        try {
            elig = PredicateManager.createMinimumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }
        pp.addCategoryPurchaseRule("dairy" ,elig);
    }

    /**cannot buy alcohol between 23-6*/
    void setupPred3(){
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(16, 24, PRPredType.TimeConstraint);
        Predicate<Basket> elig;

        try {
            elig = PredicateManager.createTimePredicate(tb3.getHoursFrom(), tb3.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }
        pp.addCategoryPurchaseRule("alcohol" ,elig);
    }


    /**cannot buy anything between 14-16*/
    void setupPred4(){
        ToBuildPRPredicateFrom tb4 = new ToBuildPRPredicateFrom(14, 16, PRPredType.TimeConstraint);
        Predicate<Basket> elig;

        try {
            elig = PredicateManager.createTimePredicate(tb4.getHoursFrom(), tb4.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }
        pp.addGeneralShopPurchaseRule(elig);
    }


    @Test
    void checkCartRulesMet(){
        Basket basket = new Basket();
        assertTrue(pp.checkCart_RulesAreMet(basket));
        setupPred2();
        assertTrue(pp.checkCart_RulesAreMet(basket));
        setupProducts();
        basket.put(mockProd1, 1);
        assertFalse(pp.checkCart_RulesAreMet(basket));
        basket.put(mockProd2, 1);
        assertTrue(pp.checkCart_RulesAreMet(basket));
        setupPred1();
        assertFalse(pp.checkCart_RulesAreMet(basket));
        basket.put(mockProd2, 5);
        assertTrue(pp.checkCart_RulesAreMet(basket));
        setupPred4();
        assertTrue(pp.checkCart_RulesAreMet(basket));
        setupPred3();
        assertFalse(pp.checkCart_RulesAreMet(basket));
        setupAndRule();
        assertFalse(pp.checkCart_RulesAreMet(basket));
        basket.remove(mockProd2);
        assertFalse(pp.checkCart_RulesAreMet(basket));
        pp.removePurchaseRule(2);
        pp.removePurchaseRule(1);
        assertFalse(pp.checkCart_RulesAreMet(basket));
    }

    @Test
    void setupAndRule(){
        try {
            pp.addAndPR(4, 3);
        } catch (CriticalInvariantException criticalInvariantException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        } catch (PurchaseRuleNotFoundException purchaseRuleNotFoundException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }
    }




    void checkIfCartRulesAreMet(){
        ProductImp mockProd1 = mock(ProductImp.class);
        ProductImp mockProd2 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        when(mockProd1.getCategory()).thenReturn("dairy");

        when(mockProd2.getId()).thenReturn(2);
        when(mockProd2.getCategory()).thenReturn("fruits");


    }



   /* public int addProductPurchaseRule(int prodID, Predicate<Basket> eligible, String productName) {
        List<PurchaseRule> prod_pr;
        if(product_purchaseRules.containsKey(prodID))
            prod_pr = getAllPurchaseRulesForProd(prodID);
        else {
            prod_pr = new ArrayList<>();
            product_purchaseRules.put(prodID, prod_pr);
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
            category_purchaseRules.put(category, category_pr);
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
        general_PurchaseRules.add(newPurchaseRule);
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


    public boolean removePurchaseRule(int purchaseRuleID){
        for(Map.Entry<Integer, List<PurchaseRule>> set : product_purchaseRules.entrySet()){
            List<PurchaseRule> prod_pr = set.getValue();
            for(PurchaseRule pr: prod_pr) {
                if (pr.getID() == purchaseRuleID) {
                    prod_pr.remove(pr);
                    eventLogger.logMsg(Level.INFO, String.format("removed discount: %d ", purchaseRuleID));
                    return true;
                }
            }
        }
        eventLogger.logMsg(Level.INFO, String.format("no such discount in the shop: %d", purchaseRuleID));
        return false;
    }

    public List<PurchaseRule> getAllDistinctPurchaseRules(){
        List<PurchaseRule> prodPR = new ArrayList<>();
        for(List<PurchaseRule> valueSet : product_purchaseRules.values())
            prodPR.addAll(valueSet);


        for(List<PurchaseRule> valueSet : category_purchaseRules.values())
            prodPR.addAll(valueSet);
        prodPR.addAll(general_PurchaseRules);

        return prodPR.stream().distinct().collect(Collectors.toList());
    }*/
}

