package domain.user;

public class InvalidAuthorizationException extends Exception {
    public String expectedAuthorization;
    public String actualAuthorization;

    public InvalidAuthorizationException(String expected, String actual){
        super();
        expectedAuthorization = expected;
        actualAuthorization = actual;
    }

    public InvalidAuthorizationException(){
        super();
        expectedAuthorization = "unknown";
        actualAuthorization = "unknown";
    }

    public InvalidAuthorizationException(String message){
        super(message);
        expectedAuthorization = "unknown";
        actualAuthorization = "unknown";
    }

}
