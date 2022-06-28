package domain.Exceptions;

public class NotFoundInDBException  extends Exception{
    public NotFoundInDBException(String message){
        super(message);
    }

    public NotFoundInDBException() {
        super();
    }

}
