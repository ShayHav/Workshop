package domain.shop.discount;

public interface Discount {

    int getID();

    boolean eligible(Basket productAmountList);

    Basket applyDiscountCalculator(Basket productToAmounts);

    boolean relevant(ProductImp product);


}

