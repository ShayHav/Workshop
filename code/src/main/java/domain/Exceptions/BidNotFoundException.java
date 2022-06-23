package domain.Exceptions;

public class BidNotFoundException extends Exception{
    public BidNotFoundException(String message){
        super(message);
    }

    public BidNotFoundException() {
        super();
    }
}
