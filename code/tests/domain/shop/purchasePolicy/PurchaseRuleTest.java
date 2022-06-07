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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.fail;
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
    void timePurchaseRule(){
        ToBuildPRPredicateFrom tb1 = new ToBuildPRPredicateFrom(17.5, 23, PRPredType.TimeConstraint);
        Predicate<Basket> elig1;
        try {
            elig1 = PredicateManager.createTimePredicate(tb1.getHoursFrom(), tb1.getHoursTo());
        } catch (AccessDeniedException accessDeniedException) {
            fail("should have been able to access, faulty ToBuildPRPredicateFrom code");
            return;
        }

        Predicate<ProductImp> relevantTo = (productImp)-> productImp.getId() == 1;
        PurchaseRule pr1 = new PurchaseRuleIMPL(elig1, relevantTo, 1, "hihihihi");
        Product mockProd1 = mock(ProductImp.class);
        when(mockProd1.getId()).thenReturn(1);


    }

    public int getID(){
        return purchaseRuleID;
    }

    public boolean relevant(ProductImp product){
        return relevantTo.test(product);
    }


}
