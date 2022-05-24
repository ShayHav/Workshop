package domain.user;

import Testing_System.UserGenerator;
import domain.Exceptions.*;
import domain.shop.Shop;
import domain.shop.ShopController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController = UserController.getInstance();;
    private ShopController shopController = ShopController.getInstance();
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();


    @BeforeEach
    void setUp() {
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
        //userController.deleteUserTest(userName);
        for (int i = 0; i < userName.length; i++) {
            assertTrue(userController.getUser(userName[i]).getUserName().equals(userName[i]));
        }
    }

    @Test
    void getUser() throws IncorrectIdentification {
        for (int i = 0; i < userName.length; i++){
            assertTrue(userController.getUser(userName[i]).getUserName().equals(userName[i]));
        }
    }

    @Test
    void createSystemManager(){
        try {
            userController.createSystemManager(admin, adminPass);
            assertTrue(userController.getUser(admin).isSystemManager());
        }
        catch (InvalidSequenceOperationsExc | IncorrectIdentification exception){
            assertTrue(false);
        }
    }
    @Test
    void deleteUserName() {
        try {
            userController.deleteUserName(userName[0]);
            assertTrue( userController.getUser(userName[0]) == null);
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc exception) {
            fail();
        }
    }
    @Test
    void DismissalOwner() throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, ShopNotFoundException {
        userController.logIn(userName[0],userPass[0]);
        User u = userController.getUser(userName[0]);
        Shop s = shopController.createShop("","",null,null, u);
        shopController.AppointNewShopOwner(s.getShopID(),userName[1], userName[0]);
        User u1 = userController.getUser(userName[1]);
        assertTrue(u1.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        userController.logIn(userName[1],userPass[1]);
        shopController.AppointNewShopOwner(s.getShopID(),userName[2], userName[1]);
        User u2 = userController.getUser(userName[2]);
        assertTrue(u2.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        userController.DismissalOwner(userName[0], userName[1],s.getShopID());
        assertFalse(u1.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        assertFalse(u2.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
    }
}
