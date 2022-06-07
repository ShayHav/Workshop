package domain.Exceptions;

public class InvalidParamException extends Exception{
    public InvalidParamException(String message){
        super(message);
    }

    public InvalidParamException() {
        super();
    }
}
