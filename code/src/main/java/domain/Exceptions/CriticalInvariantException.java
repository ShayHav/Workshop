package domain.Exceptions;

public class CriticalInvariantException extends Exception{
    public CriticalInvariantException(String message){
        super(message);
    }

    public CriticalInvariantException() {
        super();
    }
}
