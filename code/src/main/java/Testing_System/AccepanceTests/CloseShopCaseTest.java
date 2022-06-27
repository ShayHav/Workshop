package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CloseShopCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;
    private String owner;
    private String owner_pw;
    private String manager;
    private String manager_pw;
    List<ShopManagersPermissions> ls;
    private String guest1;
    private String guest2;
    private String guest3;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        guest1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";

        Register(guest1, user_1, pw_1);
        Login(guest2, user_1, pw_1);
        ResponseT<Shop> shopResponseT = CreateShop("Test",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        owner = validUsers[1];
        owner_pw = pws[1];
        Register(guest2,owner,owner_pw);
        AppointNewShopOwner(shopID_1,owner,user_1);
        manager = validUsers[2];
        manager_pw = pws[2];
        Register(guest3,manager,manager_pw);
        AppointNewShopManager(shopID_1,manager,user_1);
        ls = new ArrayList<ShopManagersPermissions>();
        ls.add(ShopManagersPermissions.CloseShop);

    }

    @BeforeEach
    public void SetShop()
    {
        Login(guest2,owner,owner_pw);
        Login(guest3,manager,manager_pw);
        AddShopMangerPermissions(shopID_1,ls,manager,user_1);
        /**
         * Not supported offline --> Online shop
         */
    }

    @AfterAll
    public void CleanUp()
    {
        Logout(user_1);
        Logout(owner);
        Logout(manager);
        ug.DeleteAdmin();
        DeleteUserTest(validUsers);
    }

    @Test
    public void FounderCloseShopGoodTest()
    {
        assertTrue(!CloseShop(shopID_1,user_1).isErrorOccurred());
    }

    @Test
    public void OwnerCloseShopGoodTest()
    {
        assertTrue(!CloseShop(shopID_1,owner).isErrorOccurred());
    }

    @Test
    public void ManagerCloseShopBadTest()
    {
        assertTrue(!CloseShop(shopID_1,manager).isErrorOccurred());
    }

    @Test
    public void ManagerClosesShopNoPermission()
    {
        RemoveShopManagerPermissions(shopID_1,ls,manager,user_1);
        assertFalse(!CloseShop(shopID_1,manager).isErrorOccurred());

    }

    @Test
    public void AlreadyClosedShopTest()
    {
        assertTrue(!CloseShop(shopID_1,user_1).isErrorOccurred());
        assertFalse(!CloseShop(shopID_1,user_1).isErrorOccurred());
        assertFalse(!CloseShop(shopID_1,owner).isErrorOccurred());
        assertFalse(!CloseShop(shopID_1,manager).isErrorOccurred());

    }

    @Test
    public void NotLoggedInTest()
    {
        Logout(owner);
        assertFalse(!CloseShop(shopID_1,owner).isErrorOccurred());

    }

    @Test
    public void NotRegisteredTest()
    {
        assertFalse(!CloseShop(shopID_1,validUsers[ug.getNumOfUser()-1]).isErrorOccurred());
    }

    @Test
    public void BadInputsTest()
    {
        assertFalse(!CloseShop(shopID_1-1,user_1).isErrorOccurred());
        assertFalse(!CloseShop(-1,user_1).isErrorOccurred());
        assertFalse(!CloseShop(shopID_1,null).isErrorOccurred());

    }





}
