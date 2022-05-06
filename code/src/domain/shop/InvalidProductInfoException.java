package domain.shop;

public class InvalidProductInfoException extends Exception {
    public InvalidProductInfoException(String message){
        super(message);
    }

    public InvalidProductInfoException() {
        super();
    }
}
