package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            Login(validUsers[i], pws[i]);
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
        assertTrue(CreateShop(validUsers[0], "TestShop").GetFirstElement());
        assertTrue(CreateShop(validUsers[1], "TestShop_2").GetFirstElement());
        assertTrue(CreateShop(validUsers[2], "TestShop_3").GetFirstElement());
    }

    @Test
    public void SameUserMultipleShops()
    {
        assertTrue(CreateShop(validUsers[0], "TestShop").GetFirstElement());
        assertTrue(CreateShop(validUsers[0], "TestShop_2").GetFirstElement());
        assertTrue(CreateShop(validUsers[0], "TestShop_3").GetFirstElement());
    }


    @Test
    public void BadNameShopTest()
    {
        assertFalse(CreateShop(validUsers[0], "T#$hop").GetFirstElement());
        assertFalse(CreateShop(validUsers[1], "Tes%&%p_2").GetFirstElement());
        assertFalse(CreateShop(validUsers[2], "A").GetFirstElement());
        assertFalse(CreateShop(validUsers[3], null).GetFirstElement());

    }

    @Test
    public void DuplicateNames()
    {
        assertTrue(CreateShop(validUsers[0], "TestShop").GetFirstElement());
        assertFalse(CreateShop(validUsers[1], "TestShop").GetFirstElement());
    }

    @Test
    public void NotRegisteredUser()
    {
        assertFalse(CreateShop("notregis", "ValidName").GetFirstElement());
    }

    @Test
    public void NotLoggedUser()
    {
        Logout(validUsers[0]);
        assertFalse(CreateShop(validUsers[0], "TestShop").GetFirstElement());

    }

}
