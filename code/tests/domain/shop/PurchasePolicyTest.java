package domain.shop;

import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.PurchasePolicys.PurchaseRule;
import org.junit.jupiter.api.BeforeEach;


public class PurchasePolicyTest {
    PurchasePolicy pp;

    @BeforeEach
    void setUp() {
        pp = new PurchasePolicy();
    }

    /*@Test
    void getAllPurchaseRulesForProd() {
        assertEquals(pp.getAllPurchaseRulesForProd(1).size(), 1, "amount of purchase rules for product id 1 currently should be 0");
        int pr1 = pp.addQuantityRule(1, 10);
        assertEquals(pp.getAllPurchaseRulesForProd(1).size(), 1, "amount of purchase rules currently should be 1");
        assertTrue(hasPR(pr1), "amount of purchase rules currently should be 1");
        int pr2 = pp.addQuantityRule(1, 10);
        assertEquals(pp.getAllPurchaseRulesForProd(1).size(), 1, "quantity rule already exists, shouldn't have added new quantity rule");
        assertTrue(hasPR(pr1), "amount of purchase rules currently should be 1");
        assertFalse(hasPR(pr2), "amount of purchase rules currently should be 1");
    }*/


    public boolean hasPR(int prID){
        for(PurchaseRule pr: pp.getAllPurchaseRulesForProd(1)){
            if (pr.getID() == prID)
                return true;
        }
        return false;
    }


   /* @Test
    void checkIfProductRulesAreMet() {
        assertTrue(pp.checkIfProductRulesAreMet("Davidos", 1, 10.0, 5), "amount of purchase rules for product id 1 currently should be 0");
        pp.addQuantityRule(1, 10);
        assertFalse(pp.checkIfProductRulesAreMet("Davidos", 1, 10.0, 5), "minimum amount to purchase from this item should be 10 but managed to buy only 5");
        assertTrue(pp.checkIfProductRulesAreMet("Davidos", 1, 10.0, 11), "minimum amount to purchase from this item should be 10 but couldn't buy 11");
    }*/
}

