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
public class AppointNewOwnerCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;
    private String guest;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest,user_1, pw_1);
        Login(guest,user_1, pw_1);
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
            assertTrue(!AppointNewShopOwner(shopID_1, validUsers[i], user_1).isErrorOccurred());
        }
    }

    @Test
    public void AppointedNotLoggedInCaseTest() {
        for (int i = 1; i < ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g,validUsers[i], pws[i]);
            assertTrue(!AppointNewShopOwner(shopID_1, validUsers[i], user_1).isErrorOccurred());
        }
    }


    @Test
    public void NewOwnerAppointsOwnerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        assertTrue(!AppointNewShopOwner(shopID_1, validUsers[1], user_1).isErrorOccurred());
        for (int i = 2; i < ug.getNumOfUser(); i++) {
            String gt = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(gt,validUsers[i], pws[i]);
            Login(gt,validUsers[i], pws[i]);
            assertTrue(!AppointNewShopOwner(shopID_1, validUsers[i], validUsers[1]).isErrorOccurred());
        }
    }

    @Test
    public void FounderAppointFounderAsOwnerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        int shopID_2 = -1;
        ResponseT<Shop> shopResponseT = CreateShop("Test_2",validUsers[1], "TestShop_2");
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        assertTrue(!AppointNewShopOwner(shopID_1, validUsers[1], user_1).isErrorOccurred());
        assertTrue(!AppointNewShopOwner(shopID_2, user_1, validUsers[1]).isErrorOccurred());
    }

    @Test
    public void MultipleOwnersDifferentShop() {
        int shopID_2 = -1;
        int shopID_3 = -1;
        ResponseT<Shop> shopResponseT = CreateShop("Test_2",user_1, "TestShop_2");
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        shopResponseT = CreateShop("Test_3",validUsers[1], "TestShop_3");
        if(!shopResponseT.isErrorOccurred())
            shopID_3 = shopResponseT.getValue().getShopID();
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        for (int i = 2; i < ug.getNumOfUser(); i++) {
            assertTrue(!AppointNewShopOwner(shopID_3, validUsers[i], validUsers[1]).isErrorOccurred());
            assertTrue(!AppointNewShopOwner(shopID_2, validUsers[i], validUsers[1]).isErrorOccurred());
            assertTrue(!AppointNewShopOwner(shopID_1, validUsers[i], user_1).isErrorOccurred());
        }
    }

    @Test
    public void MemberAppointOwnerTest()
    {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        String gt = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        Login(g,validUsers[1], pws[1]);
        Register(gt,validUsers[2], pws[2]);
        assertFalse(!AppointNewShopOwner(shopID_1,validUsers[2],validUsers[1]).isErrorOccurred());
    }
    @Test
    public void NotLoggedAppointerTest() {
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g,validUsers[1], pws[1]);
        assertTrue(!AppointNewShopOwner(shopID_1, validUsers[1], user_1).isErrorOccurred());
        for (int i = 2; i < ug.getNumOfUser(); i++)
            assertFalse(!AppointNewShopOwner(shopID_1, validUsers[i], validUsers[1]).isErrorOccurred());

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
            assertFalse(!AppointNewShopOwner(shopID_1, validUsers[i], null).isErrorOccurred());
            assertFalse(!AppointNewShopOwner(shopID_1, null, user_1).isErrorOccurred());

        }
    }

}

