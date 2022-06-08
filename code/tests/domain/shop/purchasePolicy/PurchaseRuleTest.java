package domain.shop.purchasePolicy;

import domain.Exceptions.AccessDeniedException;
import domain.shop.Product;
import domain.shop.ProductImp;
import domain.shop.PurchasePolicys.AndPR;
import domain.shop.PurchasePolicys.OrPR;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.PurchasePolicys.PurchaseRuleIMPL;
import domain.shop.discount.Basket;
import domain.shop.predicate.PRPredType;
import domain.shop.predicate.PredicateManager;
import domain.shop.predicate.ToBuildPRPredicateFrom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.function.Predicate;

import static org.mockito.Mockito.when;

public class PurchaseRuleTest {



    @Test
    //to be ran between 17-19 to pass all tests
    void timePurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(20, 23, PRPredType.TimeConstraint);
        ToBuildPRPredicateFrom tb2 = new ToBuildPRPredicateFrom(17, 23, PRPredType.TimeConstraint);
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(22, 19, PRPredType.TimeConstraint);

        Predicate<Basket> elig1;
        Predicate<Basket> elig2;
        Predicate<Basket> elig3;
        try {
            elig1 = PredicateManager.createTimePredicate(tb1.getHoursFrom(), tb1.getHoursTo());
            elig2 = PredicateManager.createTimePredicate(tb2.getHoursFrom(), tb2.getHoursTo());
            elig3 = PredicateManager.createTimePredicate(tb3.getHoursFrom(), tb3.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        PurchaseRule pr2 = new PurchaseRuleIMPL(elig2, relevantTo, 2, "hihihihi");
        PurchaseRule pr3 = new PurchaseRuleIMPL(elig3, relevantTo, 3, "hihihihi");
        ProductImp mockProd1 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        Basket basket = new Basket();
        basket.put(mockProd1, 5);
        assertTrue(pr1.purchaseAllowed(basket));
        assertFalse(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));
    }


    @Test
    void minimumPurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(5, 2, "pizza", PRPredType.MinimumAmount);
        ToBuildPRPredicateFrom tb2 = new ToBuildPRPredicateFrom(5, 1, "hala", PRPredType.MinimumAmount);
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(1, 2, "pizza", PRPredType.MinimumAmount);
        Predicate<Basket> elig1;
        Predicate<Basket> elig2;
        Predicate<Basket> elig3;
        try {
            elig1 = PredicateManager.createMinimumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
            elig2 = PredicateManager.createMinimumProductsPredicate(tb2.getProdID(), tb2.getProductName(), tb2.getAmount());
            elig3 = PredicateManager.createMinimumProductsPredicate(tb3.getProdID(), tb3.getProductName(), tb3.getAmount());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        PurchaseRule pr2 = new PurchaseRuleIMPL(elig2, relevantTo, 2, "hihihihi");
        PurchaseRule pr3 = new PurchaseRuleIMPL(elig3, relevantTo, 3, "hihihihi");
        ProductImp mockProd1 = mock(ProductImp.class);
        ProductImp mockProd2 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        when(mockProd2.getId()).thenReturn(2);
        Basket basket = new Basket();
        basket.put(mockProd1, 4);

        assertFalse(pr1.purchaseAllowed(basket));
        assertFalse(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));

        basket.put(mockProd1, 5);
        assertFalse(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));


        basket.put(mockProd2, 1);
        assertFalse(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertTrue(pr3.purchaseAllowed(basket));

        basket.put(mockProd2, 5);
        assertTrue(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertTrue(pr3.purchaseAllowed(basket));

    }

    @Test
    void maximumPurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(5, 2, "pizza", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb2 = new ToBuildPRPredicateFrom(5, 1, "hala", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(1, 2, "pizza", PRPredType.MaximumAmount);
        Predicate<Basket> elig1;
        Predicate<Basket> elig2;
        Predicate<Basket> elig3;
        try {
            elig1 = PredicateManager.createMaximumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
            elig2 = PredicateManager.createMaximumProductsPredicate(tb2.getProdID(), tb2.getProductName(), tb2.getAmount());
            elig3 = PredicateManager.createMaximumProductsPredicate(tb3.getProdID(), tb3.getProductName(), tb3.getAmount());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        PurchaseRule pr2 = new PurchaseRuleIMPL(elig2, relevantTo, 2, "hihihihi");
        PurchaseRule pr3 = new PurchaseRuleIMPL(elig3, relevantTo, 3, "hihihihi");
        ProductImp mockProd1 = mock(ProductImp.class);
        ProductImp mockProd2 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        when(mockProd2.getId()).thenReturn(2);
        Basket basket = new Basket();
        basket.put(mockProd1, 4);

        assertTrue(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertTrue(pr3.purchaseAllowed(basket));

        basket.put(mockProd1, 6);
        assertTrue(pr1.purchaseAllowed(basket));
        assertFalse(pr2.purchaseAllowed(basket));
        assertTrue(pr3.purchaseAllowed(basket));


        basket.put(mockProd2, 6);
        assertFalse(pr1.purchaseAllowed(basket));
        assertFalse(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));


        basket.remove(mockProd1);
        assertFalse(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));

        basket.put(mockProd2, 5);
        assertTrue(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertFalse(pr3.purchaseAllowed(basket));

        basket.remove(mockProd2);
        assertTrue(pr1.purchaseAllowed(basket));
        assertTrue(pr2.purchaseAllowed(basket));
        assertTrue(pr3.purchaseAllowed(basket));
    }


    @Test
    void andPurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(5, 2, "pizza", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb2 = new ToBuildPRPredicateFrom(1, 2, "pizza", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(1, 24, PRPredType.TimeConstraint);
        ToBuildPRPredicateFrom tb4 = new ToBuildPRPredicateFrom(17, 19, PRPredType.TimeConstraint);
        Predicate<Basket> elig1;
        Predicate<Basket> elig2;
        Predicate<Basket> elig3;
        Predicate<Basket> elig4;
        try {
            elig1 = PredicateManager.createMinimumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
            elig2 = PredicateManager.createMinimumProductsPredicate(tb2.getProdID(), tb2.getProductName(), tb2.getAmount());
            elig3 = PredicateManager.createTimePredicate(tb3.getHoursFrom(), tb3.getHoursTo());
            elig4 = PredicateManager.createTimePredicate(tb4.getHoursFrom(), tb4.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        PurchaseRule pr2 = new PurchaseRuleIMPL(elig2, relevantTo, 2, "hihihihi");
        PurchaseRule pr3 = new PurchaseRuleIMPL(elig3, relevantTo, 3, "hihihihi");
        PurchaseRule pr4 = new PurchaseRuleIMPL(elig4, relevantTo, 4, "hihihihi");

        PurchaseRule and1 = new AndPR(pr1, pr2, 7);
        PurchaseRule and2 = new AndPR(pr3, pr2, 7);
        PurchaseRule and3 = new AndPR(pr4, pr2, 7);

        ProductImp mockProd1 = mock(ProductImp.class);
        ProductImp mockProd2 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        when(mockProd2.getId()).thenReturn(2);
        Basket basket = new Basket();


        assertFalse(and1.purchaseAllowed(basket));
        assertFalse(and2.purchaseAllowed(basket));
        assertFalse(and3.purchaseAllowed(basket));


        basket.put(mockProd2, 4);

        assertFalse(and1.purchaseAllowed(basket));
        assertFalse(and2.purchaseAllowed(basket));
        assertTrue(and3.purchaseAllowed(basket));

        basket.put(mockProd2, 6);
        assertTrue(and1.purchaseAllowed(basket));
        assertFalse(and2.purchaseAllowed(basket));
        assertTrue(and3.purchaseAllowed(basket));
    }


    @Test
    void orPurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(5, 2, "pizza", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb2 = new ToBuildPRPredicateFrom(1, 2, "pizza", PRPredType.MaximumAmount);
        ToBuildPRPredicateFrom tb3 = new ToBuildPRPredicateFrom(1, 23, PRPredType.TimeConstraint);
        ToBuildPRPredicateFrom tb4 = new ToBuildPRPredicateFrom(17, 19, PRPredType.TimeConstraint);
        Predicate<Basket> elig1;
        Predicate<Basket> elig2;
        Predicate<Basket> elig3;
        Predicate<Basket> elig4;
        try {
            elig1 = PredicateManager.createMinimumProductsPredicate(tb1.getProdID(), tb1.getProductName(), tb1.getAmount());
            elig2 = PredicateManager.createMinimumProductsPredicate(tb2.getProdID(), tb2.getProductName(), tb2.getAmount());
            elig3 = PredicateManager.createTimePredicate(tb3.getHoursFrom(), tb3.getHoursTo());
            elig4 = PredicateManager.createTimePredicate(tb4.getHoursFrom(), tb4.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        PurchaseRule pr2 = new PurchaseRuleIMPL(elig2, relevantTo, 2, "hihihihi");
        PurchaseRule pr3 = new PurchaseRuleIMPL(elig3, relevantTo, 3, "hihihihi");
        PurchaseRule pr4 = new PurchaseRuleIMPL(elig4, relevantTo, 4, "hihihihi");

        PurchaseRule or1 = new OrPR(pr1, pr3, 5);
        PurchaseRule or2 = new OrPR(pr2, pr3, 6);
        PurchaseRule or3 = new OrPR(pr2, pr4, 7);

        ProductImp mockProd1 = mock(ProductImp.class);
        ProductImp mockProd2 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);
        when(mockProd2.getId()).thenReturn(2);
        Basket basket = new Basket();


        assertFalse(or1.purchaseAllowed(basket));
        assertFalse(or2.purchaseAllowed(basket));
        assertTrue(or3.purchaseAllowed(basket));


        basket.put(mockProd2, 4);

        assertFalse(or1.purchaseAllowed(basket));
        assertTrue(or2.purchaseAllowed(basket));
        assertTrue(or3.purchaseAllowed(basket));

        basket.put(mockProd2, 6);
        assertTrue(or1.purchaseAllowed(basket));
        assertTrue(or2.purchaseAllowed(basket));
        assertTrue(or3.purchaseAllowed(basket));
    }



}
