package domain.shop.predicate;

import domain.Exceptions.AccessDeniedException;

import java.time.LocalDateTime;

public class ToBuildPRPredicateFrom {
    private int prodID;
    private double hoursFrom;
    private double hoursTo;
    private int amount;
    private LocalDateTime fromDate;
    private LocalDateTime ToDate;
    private PRPredType predType;
    private String productName;

    public ToBuildPRPredicateFrom(double hoursFrom, double hoursTo, PRPredType type){
        int intPartFrom = (int)hoursFrom;
        int intPartTo = (int)hoursTo;
        double floatPartFrom = hoursFrom - intPartFrom;
        double floatPartTo = hoursTo - intPartTo;

        this.hoursFrom =  (intPartFrom % 24) + floatPartFrom;
        this.hoursTo = (intPartTo % 24) + floatPartTo;
        predType = type;
    }

    public ToBuildPRPredicateFrom(int amount, int prodID, String productName, PRPredType type){
        this.amount = amount;
        this.prodID = prodID;
        predType = type;
        this.productName = productName;
    }


    public int getAmount() throws AccessDeniedException {
        if(predType.equals(PRPredType.TimeConstraint))
            throw new AccessDeniedException("this is a Time predicate, should never access product amount");
        return amount;
    }

    public double getHoursFrom() throws AccessDeniedException {
        if(predType.equals(PRPredType.TimeConstraint))
            return hoursFrom;
        throw new AccessDeniedException("this is not a Time predicate, should never access \"from time\"");
    }

    public double getHoursTo() throws AccessDeniedException {
        if(predType.equals(PRPredType.TimeConstraint))
            return hoursTo;
        throw new AccessDeniedException("this is not a Time predicate, should never access \"to time\"");
    }

    public int getProdID() throws AccessDeniedException {
        if(predType.equals(PRPredType.MaximumAmount) || predType.equals(PRPredType.MinimumAmount))
            return prodID;
        throw new AccessDeniedException("this is not an amount predicate, should never access product ID");

    }


    public String getProductName() throws AccessDeniedException {
        if(predType.equals(PRPredType.MaximumAmount) || predType.equals(PRPredType.MinimumAmount))
            return productName;
        throw new AccessDeniedException("this is not an amount predicate, should never access product Name");
    }



    public PRPredType getPredType() {
        return predType;
    }
}
