package domain.shop;


import domain.shop.discount.DiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class DiscountPolicyTest {
    DiscountPolicy dp;

    @BeforeEach
    void setUp() {
        dp = new DiscountPolicy();
    }

    @Test
    void calcPricePerProduct(){

       /* assertEquals(dp.calcPricePerProduct(1, 10.0, 5, ), 10.0, "there are no discounts, price should be 10");
        int discount1 = dp.addSimpleProductDiscount(1, 50);
        assertEquals(dp.calcPricePerProduct(1, 10.0, 5), 5.0, "price after discount should be 5");
        int discount2 = dp.addPercentageDiscount(1, 20);
        assertEquals(dp.calcPricePerProduct(1, 10.0, 5), 4.0, "price after discount should be 4");
        assertEquals(dp.calcPricePerProduct(2, 10.0, 5), 10.0, "price after discount should be 10");
        int discount3 = dp.addBundleDiscount(2, 3, 2);
        assertEquals(dp.calcPricePerProduct(2, 10.0, 5), 6.0, "price after discount should be 6");
        int discount4 = dp.addBundleDiscount(2, 3, 2);
        assertEquals(dp.calcPricePerProduct(2, 10.0, 5), 6.0, "price after discount should be 6");
        assertEquals(dp.calcPricePerProduct(2, 10.0, 8), 7.5, "price after discount should be 7.5");
        dp.removeDiscount(discount1);
        assertEquals(dp.calcPricePerProduct(1, 10.0, 5), 5.0, "price after discount should be 5");
        dp.removeDiscount(discount3);
        assertEquals(dp.calcPricePerProduct(2, 10.0, 5), 10.0, "there are no discounts, price should be 10");
        int discount5 = dp.addBundleDiscount(2, 3, 2);
        assertEquals(dp.calcPricePerProduct(2, 10.0, 5), 6.0, "price after discount should be 6");*/
    }
}
