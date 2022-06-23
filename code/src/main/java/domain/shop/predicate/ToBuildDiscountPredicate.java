package domain.shop.predicate;

import domain.Exceptions.AccessDeniedException;
import domain.Exceptions.InvalidParamException;


public class ToBuildDiscountPredicate {
    private int productID;
    private int amount;
    private double price;
    private DiscountPredType predType;
    private String productName;


    public ToBuildDiscountPredicate(int productID, String productName,int amount) throws InvalidParamException {
        if(productID < 0)
            throw new InvalidParamException("invalid product ID");
        if(amount < 0)
            throw new InvalidParamException("invalid amount");
        this.productID = productID;
        this.amount = amount;
        this.productName = productName;
        predType = DiscountPredType.product;
    }

    public ToBuildDiscountPredicate(double price) throws InvalidParamException {
        if(amount < 0)
            throw new InvalidParamException("invalid amount");
        this.price = price;
        predType = DiscountPredType.price;
    }

    public int getProductID() throws AccessDeniedException {
        if(predType.equals(DiscountPredType.price))
            throw new AccessDeniedException("this is a price predicate, should never access product ID");
        return productID;
    }

    public String getProductName() throws AccessDeniedException {
        if(predType.equals(DiscountPredType.product))
            throw new AccessDeniedException("this is a price predicate, should never access product Name");
        return productName;
    }

    public int getAmount() throws AccessDeniedException {
        if(predType.equals(DiscountPredType.price))
            throw new AccessDeniedException("this is a price predicate, should never access amount");
        return amount;
    }

    public double getPrice() throws AccessDeniedException {
        if(predType.equals(DiscountPredType.product))
            throw new AccessDeniedException("this is a product predicate, should never access price");
        return price;
    }

    public DiscountPredType getPredType() {
        return predType;
    }
}

