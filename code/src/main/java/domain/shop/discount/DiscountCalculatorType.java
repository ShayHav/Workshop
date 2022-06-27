package domain.shop.discount;
public interface DiscountCalculatorType {
    double applyDiscount(double price);

    double getPercentage();
}
