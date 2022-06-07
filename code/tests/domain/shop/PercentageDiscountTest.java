package domain.shop;

import domain.Exceptions.InvalidParamException;
import domain.shop.discount.DiscountCalculatorType;

import domain.shop.discount.PercentageDiscount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountTest {
    private DiscountCalculatorType discountCalc;
    private int discountID;
    DiscountCalculatorType percentageDiscount;


    @Test
    public void PercentageDiscountConstructor() {
        assertThrows(InvalidParamException.class, ()-> new PercentageDiscount(0));
        assertThrows(InvalidParamException.class, ()-> new PercentageDiscount(100));
        assertThrows(InvalidParamException.class, ()-> new PercentageDiscount(500));
        assertThrows(InvalidParamException.class, ()-> new PercentageDiscount(-1));
        DiscountCalculatorType percentageDisc;
        try{
            percentageDisc = new PercentageDiscount(50);
        }catch (InvalidParamException invalidParamException){
            fail("percentage discount params are valid");
            return;
        }
        assertNotNull(percentageDisc);
    }

    @Test
    public void applyDiscount(){
        DiscountCalculatorType percentageDisc1;
        DiscountCalculatorType percentageDisc2;
        try{
            percentageDisc1 = new PercentageDiscount(50);
            percentageDisc2 = new PercentageDiscount(20);
        }catch (InvalidParamException invalidParamException){
            fail("percentage discount params are valid");
            return;
        }
        assertEquals(50 ,percentageDisc1.applyDiscount(100));
        assertEquals(80 ,percentageDisc2.applyDiscount(100));
    }
}
