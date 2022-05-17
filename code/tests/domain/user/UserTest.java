package domain.user;

import Testing_System.UserGenerator;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

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

    }

    @Test
    void appointOwner() {

    }

    @Test
    void appointManager() {
    }

    @Test
    void addRole() {
    }
}