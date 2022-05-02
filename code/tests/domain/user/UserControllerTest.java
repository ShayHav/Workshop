package domain.user;

import Testing_System.UserGenerator;
import domain.shop.Inventory;
import domain.shop.ProductImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    private UserController userController;
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();


    @BeforeEach
    void setUp() {
        userController = UserController.getInstance();
        for(int i=0;i<userName.length;i++) {
            userController.register(userName[i], userPass[i]);
        }
    }

    @Test
    void logIn() {
        for(int i = 0; i < userName.length; i++){
            assertTrue(userController.logIn(userName[i], userPass[i]));
            userController.logOut(userName[i]);
        }
        for(int i = 0; i < userName.length; i++){
            assertFalse(userController.logIn(userName[i], badPass[i]));
        }
    }

    @Test
    void logOut() {
        for(int i = 0; i < userName.length; i++){
            userController.logIn(userName[i], userPass[i]);
            userController.logOut(userName[i]);
        }
    }

    @Test
    void register() {
        userController.deleteUserTest(userName);
        for (int i = 0; i < userName.length; i++) {
            assertTrue(userController.register(userName[i], userPass[i]));
            assertTrue(userController.getUser(userName[i]).getId().equals(userName[i]));
            assertFalse(userController.register(userName[i], userPass[i]));
        }
    }

    @Test
    void getUser() {
        for (int i = 0; i < userName.length; i++){
            assertTrue(userController.getUser(userName[i]).getId().equals(userName[i]));
        }
    }
}
