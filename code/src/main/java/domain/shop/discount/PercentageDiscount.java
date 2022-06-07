package domain.shop.discount;

import domain.Exceptions.InvalidParamException;

public class PercentageDiscount implements DiscountCalculatorType {
    double percentage;



    public PercentageDiscount(double percentageDiscount) throws InvalidParamException {
        if(percentageDiscount > 0 && percentageDiscount < 100){
            percentage =percentageDiscount;
        }
        else {
            throw new InvalidParamException("percentage inviable");
        }
    }

    public double applyDiscount(double price) {
        return ((100 - percentage) * price) / 100 ;
    }


    public double getPercentage(){
        return percentage;
    }
}
