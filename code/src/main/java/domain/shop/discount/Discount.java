package domain.shop.discount;

public interface Discount {

    int getID();

    double applyDiscount(double price, int amount);

}

