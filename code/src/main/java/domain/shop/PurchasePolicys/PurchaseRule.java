package domain.shop.PurchasePolicys;


import domain.shop.ProductImp;
import domain.shop.discount.Basket;

public interface PurchaseRule {

    boolean purchaseAllowed(Basket productToAmounts);

    int getID();

    boolean relevant(ProductImp product);

}
