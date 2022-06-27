package domain.shop.discount;

import domain.shop.ProductImp;

public interface Discount {

    int getID();

    boolean eligible(Basket productAmountList);

    Basket applyDiscountCalculator(Basket productToAmounts);

    boolean relevant(ProductImp product);


}

