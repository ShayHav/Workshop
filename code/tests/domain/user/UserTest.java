package domain.user;

import Testing_System.UserGenerator;
import domain.Exceptions.*;
import domain.market.ExternalConnector;
import domain.shop.Product;
import domain.shop.ProductImp;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.shop.discount.DiscountPolicy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {
    private UserController userController;
    private ShopController shopController;
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private Shop shop;
    private int appleID;
    private int orangeID;

    @BeforeEach
    void setUp() {
        userController = UserController.getInstance();
        shopController = ShopController.getInstance();
        discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        for(int i=0;i<userName.length;i++) {
            try {
                userController.register(userName[i], userPass[i]);
                userController.getUser(userName[i]).enterMarket();
            }
            catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
                fail();
            }
            catch (IncorrectIdentification incorrectIdentification){
                fail();
            }
        }
        try {
            shopController.createShop("David's", discountPolicy, purchasePolicy,  userController.getUser(userName[0]));
        }
        catch (IncorrectIdentification incorrectIdentification){
            System.out.println(incorrectIdentification.getMessage());
        }
    }

    @Test
    void appointOwner() {
        User u1 = null;
        User u2 = null;
        try {
            u1 = userController.getUser(userName[0]);
            u1.login();
            u2 = userController.getUser(userName[1]);
            u2.login();

        }
        catch (IncorrectIdentification incorrectIdentification){
            System.out.println(incorrectIdentification.getMessage());
        }
        try {
            if (u1 != null) {
                u1.appointOwner(userName[1], 1);
            }
        }
        catch (BlankDataExc blankDataExc){
            fail();
        }
        catch (IncorrectIdentification incorrectIdentification){
            fail();
        }
        assertFalse(u1.getOwnerAppointmentList().contains(userName[1]));
        u1.addRole(1,Role.ShopOwner);
        try {
            if (u1 != null) {
                u1.appointOwner(userName[1], 1);
            }
        }
        catch (BlankDataExc blankDataExc){
            fail();
        }
        catch (IncorrectIdentification incorrectIdentification){
            fail();
        }
        assertTrue(u1.getOwnerAppointmentList().contains(userName[1]));;
    }

    @Test
    void appointManager() {
    }

    @Test
    void addRole() {
    }
}