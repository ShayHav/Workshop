package domain.user;

import Testing_System.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController;
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();


    @BeforeEach
    void setUp() throws InvalidSequenceOperationsExc {
        userController = UserController.getInstance();
        for(int i=0;i<userName.length;i++) {
            try {
                userController.register(userName[i], userPass[i]);
            }
            catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
                System.out.println(invalidSequenceOperationsExc.getMessage());
            }
        }
    }

    @Test
    void logIn() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException {
        for(int i = 0; i < userName.length; i++){
            assertTrue(userController.logIn(userName[i], userPass[i])!=null);
            userController.logOut(userName[i]);
        }
        for(int i = 0; i < userName.length; i++){
            int finalI = i;
            try {
                userController.logIn(userName[finalI], badPass[finalI]);
                assertTrue(false);
            }
            catch (InvalidAuthorizationException invalidAuthorizationException){
                assertTrue(true);
            }
        }
    }

    @Test
    void logOut() throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        for(int i = 0; i < userName.length; i++){
            userController.logIn(userName[i], userPass[i]);
            userController.logOut(userName[i]);
            assertFalse(userController.getUser(userName[i]).isLoggedIn());
        }
    }

    @Test
    void register() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        userController.deleteUserTest(userName);
        for (int i = 0; i < userName.length; i++) {
            assertTrue(userController.getUser(userName[i]).getId().equals(userName[i]));
        }
    }

    @Test
    void getUser() throws IncorrectIdentification {
        for (int i = 0; i < userName.length; i++){
            assertTrue(userController.getUser(userName[i]).getId().equals(userName[i]));
        }
    }
}
