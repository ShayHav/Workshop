package domain.shop.discount;

import domain.shop.ProductImp;

import java.util.Map;

public class XorDiscount implements Discount {
    Discount discount1;
    Discount discount2;
    int discountID;




    public XorDiscount(Discount discount1, Discount discount2, int discountID){
        this.discount1 = discount1;
        this.discount2 = discount2;
        this.discountID = discountID;
    }

    @Override
    public boolean eligible(Basket productAmountList) {
        return discount1.eligible(productAmountList) && discount2.eligible(productAmountList);
    }

    @Override
    public Basket applyDiscountCalculator(Basket productToAmounts) {
        if(eligible(productToAmounts)){
            Basket productToAmounts1 = new Basket();
            Basket productToAmounts2 = new Basket();

            for (Map.Entry<ProductImp, Integer> productInCart : productToAmounts.entrySet()) {
                productToAmounts1.put(new ProductImp(productInCart.getKey()), productInCart.getValue());
                    productToAmounts2.put(new ProductImp(productInCart.getKey()), productInCart.getValue());
            }
            productToAmounts1 = discount1.applyDiscountCalculator(productToAmounts1);
            productToAmounts2 = discount2.applyDiscountCalculator(productToAmounts2);
            if (productToAmounts1.calculateTotal() < productToAmounts2.calculateTotal())
                return productToAmounts1;
            return productToAmounts2;
        }
        return productToAmounts;
    }

    public int getID(){
        return discountID;
    }

    public boolean relevant(ProductImp product){
        return discount1.relevant(product) || discount2.relevant(product);
    }


}
