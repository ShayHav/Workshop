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

    public ToBuildPRPredicateFrom(double hoursFrom, double hoursTo, PRPredType type){
        this.hoursFrom = hoursFrom % 24;
        this.hoursTo = hoursTo % 24;

        predType = type;
    }

    public ToBuildPRPredicateFrom(int amount, PRPredType type){
        this.amount = amount;
        predType = type;
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
            throw new AccessDeniedException("this is not an amount predicate, should never access product ID");
        return prodID;
    }



    public PRPredType getPredType() {
        return predType;
    }
}
