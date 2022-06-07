package domain.shop;

import main.java.domain.shop.discount.Basket;
import main.java.domain.shop.predicate.PredicateManager;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PredicateManagerTest {
    PredicateManager pm;



    @Test
    void createPricePredicate() {
        Basket testSubject1 = mock(Basket.class);
        when(testSubject1.calculateTotal()).thenReturn(9.0);
        Basket testSubject2 = mock(Basket.class);
        when(testSubject2.calculateTotal()).thenReturn(25.0);
        Basket testSubject3 = mock(Basket.class);
        when(testSubject3.calculateTotal()).thenReturn(10.0);

        Predicate<Basket> pred1 = PredicateManager.createPricePredicate(10);
        assertFalse(pred1.test(testSubject1), "price not enough, should have failed");
        assertTrue(pred1.test(testSubject2), "price is enough, should have succeeded");
        assertTrue(pred1.test(testSubject3), "price is enough, should have succeeded");
    }


    @Test
    void createProductsPredicate() {
        Predicate<Basket> pred1 = PredicateManager.createMinimumProductsPredicate(1, 5);

        Basket testSubject1 = mock(Basket.class);
        when(testSubject1.findAmount(1)).thenReturn(4);
        assertFalse(pred1.test(testSubject1), "not enough products of predicate bought");
        when(testSubject1.findAmount(1)).thenReturn(5);
        assertTrue(pred1.test(testSubject1), "enough products of predicate bought");

        Predicate<Basket> pred2 = PredicateManager.createMinimumProductsPredicate(2, 3);
        when(testSubject1.findAmount(2)).thenReturn(0);
        assertFalse(pred2.test(testSubject1), "not enough products of predicate bought");
        when(testSubject1.findAmount(2)).thenReturn(5);
        assertTrue(pred2.test(testSubject1), "enough products of predicate bought");

        when(testSubject1.findAmount(3)).thenReturn(5);
        assertTrue(pred2.test(testSubject1), "not enough products of predicate bought");

    }

    @Test
    void orPredicate() {

        Predicate<Basket> pred1 = PredicateManager.createMinimumProductsPredicate(1, 5);
        Basket testSubject1 = mock(Basket.class);
        when(testSubject1.findAmount(1)).thenReturn(4);
        when(testSubject1.calculateTotal()).thenReturn(10.0);

        Predicate<Basket> pred2 = PredicateManager.createMinimumProductsPredicate(2, 3);
        Predicate<Basket> predOr1 = PredicateManager.orPredicate(pred1, pred2);
        Predicate<Basket> pred3 = PredicateManager.createPricePredicate(8);

        assertFalse(predOr1.test(testSubject1), "no predicates met, should have failed");
        when(testSubject1.findAmount(2)).thenReturn(5);
        assertTrue(predOr1.test(testSubject1), "predicate 2 met, should have succeeded");

        when(testSubject1.findAmount(2)).thenReturn(0);
        assertFalse(predOr1.test(testSubject1), "no predicates met, should have failed");

        Predicate<Basket> predOr2 = PredicateManager.orPredicate(predOr1, pred3);
        assertTrue(predOr2.test(testSubject1), "predicate 3 met, should have succeeded");

    }


    @Test
    void andPredicate() {

        Predicate<Basket> pred1 = PredicateManager.createMinimumProductsPredicate(1, 5);

        Basket testSubject1 = mock(Basket.class);
        when(testSubject1.findAmount(1)).thenReturn(6);
        when(testSubject1.calculateTotal()).thenReturn(7.0);
        Predicate<Basket> pred2 = PredicateManager.createMinimumProductsPredicate(2, 3);

        Predicate<Basket> predAnd1 = PredicateManager.andPredicate(pred1, pred2);
        Predicate<Basket> pred3 = PredicateManager.createPricePredicate(8);

        assertFalse(predAnd1.test(testSubject1), "one predicate not met, should have failed");
        when(testSubject1.findAmount(2)).thenReturn(6);
        assertTrue(predAnd1.test(testSubject1), "both predicate met, should have succeeded");


        Predicate<Basket> predAnd2 = PredicateManager.andPredicate(predAnd1, pred3);
        assertFalse(predAnd2.test(testSubject1), "predicate 3 not met, should have failed");
        when(testSubject1.calculateTotal()).thenReturn(10.0);
        assertTrue(predAnd2.test(testSubject1), "all predicates met, should have suceeded");

    }


    @Test
    void xorPredicate() {

        Predicate<Basket> pred1 = PredicateManager.createMinimumProductsPredicate(1, 5);
        Basket testSubject1 = mock(Basket.class);
        when(testSubject1.findAmount(1)).thenReturn(4);
        when(testSubject1.calculateTotal()).thenReturn(10.0);
        Predicate<Basket> pred2 = PredicateManager.createMinimumProductsPredicate(2, 3);
        Predicate<Basket> predXor1 = PredicateManager.xorPredicate(pred1, pred2);
        Predicate<Basket> pred3 = PredicateManager.createPricePredicate(8);

        assertFalse(predXor1.test(testSubject1), "no predicate met, should have failed");
        when(testSubject1.findAmount(1)).thenReturn(6);
        assertTrue(predXor1.test(testSubject1), "only a single predicate met, should have succeeded");
        when(testSubject1.findAmount(2)).thenReturn(5);
        assertFalse(predXor1.test(testSubject1), "more than a single predicate met, should have failed");

        Predicate<Basket> predXor2 = PredicateManager.xorPredicate(predXor1, pred3);
        assertTrue(predXor2.test(testSubject1), "only 1 sub-predicate true, should have suceeded");
        when(testSubject1.findAmount(1)).thenReturn(0);
        assertFalse(predXor2.test(testSubject1), "both sub-predicate met, should have failed");
        when(testSubject1.findAmount(2)).thenReturn(0);
        assertTrue(predXor2.test(testSubject1), "only one sub-predicate met, should have suceeded");
    }


}
