package domain.Exceptions;

public class IllegalStateException extends Exception{
    public IllegalStateException(String message){
        super(message);
    }

    public IllegalStateException() {
        super();
    }
}
