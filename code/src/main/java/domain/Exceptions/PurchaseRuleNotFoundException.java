package domain.Exceptions;

public class PurchaseRuleNotFoundException extends Exception{
    public PurchaseRuleNotFoundException(String message){
        super(message);
    }

    public PurchaseRuleNotFoundException() {
        super();
    }
}
