package domain.shop.discount;

public class PercentageDiscount implements Discount {
    double percentage;
    private int discountID;


    public PercentageDiscount(double percentageDiscount) throws IllegalArgumentException {
        if(percentageDiscount > 0 && percentageDiscount < 100){
            percentage =percentageDiscount;
        }
        else {
            throw new IllegalArgumentException("percentage inviable");
        }
    }

    public double applyDiscount(double price, int amount) {
        return (percentage * price) / 100 ;
    }

    public int getID(){
        return discountID;
    }
}
