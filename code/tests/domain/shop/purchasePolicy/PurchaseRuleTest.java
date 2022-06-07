package domain.shop.purchasePolicy;

import domain.Exceptions.AccessDeniedException;
import domain.shop.Product;
import domain.shop.ProductImp;
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
    private int purchaseRuleID;
    private Predicate<Basket> eligibility;
    private Predicate<ProductImp> relevantTo;


    /*public PurchaseRuleIMPL(Predicate<Basket> eligibility, Predicate<ProductImp> relevantTo, int purchaseRuleID) throws IllegalArgumentException{
        this.eligibility = eligibility;
        this.relevantTo = relevantTo;
        this.purchaseRuleID = purchaseRuleID;
    }

    @Override
    public boolean purchaseAllowed(Basket productToAmounts) {
        return eligibility.test(productToAmounts);
    }*/


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



    public int getID(){
        return purchaseRuleID;
    }

    public boolean relevant(ProductImp product){
        return relevantTo.test(product);
    }

}
