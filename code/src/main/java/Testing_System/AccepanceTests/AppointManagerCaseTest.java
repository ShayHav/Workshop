package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointManagerCaseTest extends Tester{
    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;
    private String g1;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,user_1, pw_1);
        Login(g,user_1, pw_1);
        ResponseT<Shop> shopResponseT = CreateShop("Test",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();

    }


    @AfterEach
    public void Demote() {
        String[] arr = new String[ug.getNumOfUser() - 1];
        for (int i = 1; i < ug.getNumOfUser(); i++)
            arr[i - 1] = validUsers[i];
        DeleteUserTest(arr);
    }

    @AfterAll
    public void CleanUp() {
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }


    @Test
    public void GoodAppoint() {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g,validUsers[i], pws[i]);
            Login(g,validUsers[i], pws[i]);
            assertTrue(!(AppointNewShopManager(shopID_1, validUsers[i], user_1).isErrorOccurred()));
        }
    }

    @Test
    public void AppointedNotLoggedInCaseTest() {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g,validUsers[i], pws[i]);
            assertTrue(!(AppointNewShopManager(shopID_1, validUsers[i], user_1).isErrorOccurred()));
        }
    }


    @Test
    public void NewOwnerAppointsManagerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        AppointNewShopOwner(shopID_1, validUsers[1], user_1);
        for (int i = 2; i < ug.getNumOfUser(); i++) {
            String gt = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(gt,validUsers[i], pws[i]);
            Login(gt,validUsers[i], pws[i]);
            assertFalse(!(AppointNewShopManager(shopID_1, validUsers[i], validUsers[1]).isErrorOccurred()));
        }
    }

    @Test
    public void FounderAppointFounderAsManagerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        ResponseT<Shop> shopResponseT = CreateShop("Test_2",validUsers[1], "TestShop_2");
        int shopID_2 = -1;
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        assertTrue(!(AppointNewShopManager(shopID_1, validUsers[1], user_1).isErrorOccurred()));
        assertTrue(!AppointNewShopManager(shopID_2, user_1, validUsers[1]).isErrorOccurred());
    }

    @Test
    public void MultipleManagersDifferentShop() {
        ResponseT<Shop> shopResponseT = CreateShop("Test_2",user_1, "TestShop_2");
        int shopID_2 = -1;
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        shopResponseT =CreateShop("Test_3",validUsers[1], "TestShop_3");
        int shopID_3 = -1;
        if(!shopResponseT.isErrorOccurred())
            shopID_3 = shopResponseT.getValue().getShopID();
        for (int i = 2; i < ug.getNumOfUser(); i++) {
            String g2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g2,validUsers[i],pws[i]);
            assertTrue(!(AppointNewShopManager(shopID_2, validUsers[i], user_1).isErrorOccurred()));
            assertTrue(!(AppointNewShopManager(shopID_3, validUsers[i], validUsers[1]).isErrorOccurred()));
            assertTrue(!(AppointNewShopManager(shopID_1, validUsers[i], user_1).isErrorOccurred()));
        }
    }

    @Test
    public void NotLoggedAppointerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        for (int i = 2; i < ug.getNumOfUser(); i++)
            assertFalse(!AppointNewShopManager(shopID_1, validUsers[i], validUsers[1]).isErrorOccurred());

    }

    @Test
    public void NotRegisteredUserTest() {
        for (int i = 1; i < ug.getNumOfUser(); i++)
            assertFalse(!AppointNewShopOwner(shopID_1, validUsers[i], user_1).isErrorOccurred());
    }

    @Test
    public void BadValuesTest() {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g,validUsers[i], pws[i]);
            assertFalse(!AppointNewShopManager(shopID_1, validUsers[i], null).isErrorOccurred());
            assertFalse(!AppointNewShopManager(shopID_1, null, user_1).isErrorOccurred());

        }
    }

    @Test
    public void MemberAppointManagerTest()
    {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        String gt = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        Register(gt,validUsers[2], pws[2]);
        assertFalse(!AppointNewShopManager(shopID_1,validUsers[2],validUsers[1]).isErrorOccurred());
    }

}
