package domain.Exceptions;

public class DiscountNotFoundException extends Exception{
    public DiscountNotFoundException(String message){
        super(message);
    }

    public DiscountNotFoundException() {
        super();
    }

}
