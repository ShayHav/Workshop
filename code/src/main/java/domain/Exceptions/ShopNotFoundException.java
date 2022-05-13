package domain.Exceptions;

public class ShopNotFoundException extends Exception{
    public ShopNotFoundException(String message){
        super(message);
    }

    public ShopNotFoundException() {
        super();
    }
}
