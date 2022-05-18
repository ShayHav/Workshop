package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.Shop;
import domain.user.filter.SearchUserFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DismissalOwnerByOwnerCaseTest extends Tester {
    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames;
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;

    @BeforeAll
    public void SetUp()
    {
        shopID_1 = -1;
        ug = new UserGenerator();
        validUserNames = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUserNames[0];
        pw_1 = pws[0];
        Register(user_1, pw_1);
        Login(user_1, pw_1,null);
        ResponseT<Shop> shopResponseT = CreateShop("Test",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();

        for (int i = 1; i < ug.getNumOfUser(); i++) {
            Register(validUserNames[i], pws[i]);
            Login(validUserNames[i], pws[i],null);
            assertTrue(!AppointNewShopOwner(shopID_1, validUserNames[i], user_1).isErrorOccurred());
        }
    }

    @AfterEach
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }

    @Test
    public void DismissalAllOwners()
    {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            assertTrue(!DismissalOwnerByOwner(user_1,validUserNames[i],shopID_1).isErrorOccurred());
        }
    }

    @Test
    public void NotExistUserTest()
    {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            assertFalse(!DismissalOwnerByOwner(user_1,badUserName[i],shopID_1).isErrorOccurred());
        }
    }

    @Test
    public void NoPermissionDismissal()
    {
        for (int i = 2; i < ug.getNumOfUser(); i++) {
            assertFalse(!DismissalOwnerByOwner(validUserNames[1],validUserNames[i],shopID_1).isErrorOccurred());
        }
    }
}
