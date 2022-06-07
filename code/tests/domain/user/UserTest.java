package domain.user;

import domain.Exceptions.*;
import domain.UserGeneratorTemp;
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
    private UserGeneratorTemp userGenerator = new UserGeneratorTemp();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.getPw();
    private String[] badPass = userGenerator.getBadPW();
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