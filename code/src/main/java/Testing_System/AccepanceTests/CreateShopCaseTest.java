package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateShopCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user;
    private String pw;
    private int shopID;

    @BeforeAll
    public void SetUp()
    {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user = validUsers[0];
        pw = pws[0];
    }

    @BeforeEach
    public void SetUser()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++) {
            Register(validUsers[i], pws[i]);
            Login(validUsers[i], pws[i],null);
        }
    }

    @AfterEach
    public void DeleteUser()
    {
        DeleteUserTest(validUsers);
    }
    @AfterAll
    public void CleanUp()
    {
        ug.DeleteAdmin();
    }


    @Test
    public void GoodCreationTest()
    {
        assertTrue(!CreateShop("Test_1",validUsers[0], "TestShop").isErrorOccurred());
        assertTrue(!CreateShop("Test_2",validUsers[1], "TestShop_2").isErrorOccurred());
        assertTrue(!CreateShop("Test_3",validUsers[2], "TestShop_3").isErrorOccurred());
    }

    @Test
    public void SameUserMultipleShops()
    {
        assertTrue(!CreateShop("Test_1",validUsers[0], "TestShop").isErrorOccurred());
        assertTrue(!CreateShop("Test2",validUsers[0], "TestShop_2").isErrorOccurred());
        assertTrue(!CreateShop("Test_3",validUsers[0], "TestShop_3").isErrorOccurred());
    }


    @Test
    public void BadNameShopTest()
    {
        assertFalse(!CreateShop("Test_1",validUsers[0], "T#$hop").isErrorOccurred());
        assertFalse(!CreateShop("Test_2",validUsers[1], "Tes%&%p_2").isErrorOccurred());
        assertFalse(!CreateShop("Test_3",validUsers[2], "A").isErrorOccurred());
        assertFalse(!CreateShop("Test_4",validUsers[3], null).isErrorOccurred());

    }

    @Test
    public void DuplicateNames()
    {
        assertTrue(!CreateShop("Test_1",validUsers[0], "TestShop").isErrorOccurred());
        assertFalse(!CreateShop("Test_2",validUsers[1], "TestShop").isErrorOccurred());
    }

    @Test
    public void NotRegisteredUser()
    {
        assertFalse(!CreateShop("Test","notregis", "ValidName").isErrorOccurred());
    }

    @Test
    public void NotLoggedUser()
    {
        Logout(validUsers[0]);
        assertFalse(!CreateShop("Test_1",validUsers[0], "TestShop").isErrorOccurred());

    }

}
