package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
public class AddShopManagerPermissionsCaseTest extends Tester{

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private String owner;
    private String owner_pw;
    private int shopID_1;
    private List<ShopManagersPermissions> ls;


    @BeforeAll
    public void SetUp() {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        Register(user_1, pw_1);
        Login(user_1, pw_1,null);
        ResponseT<Shop> shopResponseT = CreateShop("Hello Darkness My Old friend", user_1, "TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        owner = validUsers[1];
        owner_pw = pws[1];
        Register(owner,owner_pw);
        AppointNewShopOwner(shopID_1,owner,user_1);


    }


    @AfterEach
    public void Demote() {
        Logout(owner);
        String[] arr = new String[ug.getNumOfUser() - 2];
        for (int i = 2; i < ug.getNumOfUser(); i++)
            arr[i - 2] = validUsers[i];
        DeleteUserTest(arr);
    }

    @AfterAll
    public void CleanUp() {
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @BeforeEach
    public void setList()
    {
        ls = new ArrayList<ShopManagersPermissions>();
    }

    @Test
    public void AddPermissionsSuccessTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);

        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            Login(validUsers[i],pws[i],null);
            AppointNewShopManager(shopID_1,validUsers[i],user_1);
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
        }
    }

    @Test
    public void AddMultiplePermissionsSuccessTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);
        ls.add(ShopManagersPermissions.RemoveProductFromInventory);

        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            Login(validUsers[i],pws[i],null);
            AppointNewShopManager(shopID_1,validUsers[i],user_1);
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
        }
    }

    @Test
    public void TwoStepAddSuccessTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);

        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            Login(validUsers[i],pws[i],null);
            AppointNewShopManager(shopID_1,validUsers[i],user_1);
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
        }

        ls = new ArrayList<ShopManagersPermissions>();
        ls.add(ShopManagersPermissions.RemoveProductFromInventory);

        for(int i = 2; i<ug.getNumOfUser(); i++)
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
    }

    @Test
    public void AppointedOwnerGivesPermissionTest()
    {
        Login(owner,owner_pw,null);
        ls.add(ShopManagersPermissions.AddProductToInventory);

        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            Login(validUsers[i],pws[i],null);
            AppointNewShopManager(shopID_1,validUsers[i],user_1);
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],owner).GetFirstElement());
        }
    }

    @Test
    public void ManagerNotLoggedGoodTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);
        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            AppointNewShopManager(shopID_1,validUsers[i],user_1);
            assertTrue(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
        }
    }

    @Test
    public void NotLoggedInCaseFailTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);
        for(int i = 2; i<ug.getNumOfUser(); i++) {
            Register(validUsers[i], pws[i]);
            AppointNewShopManager(shopID_1, validUsers[i], user_1);
            assertFalse(AddShopMangerPermissions(shopID_1, ls, validUsers[i], owner).GetFirstElement());
        }
    }

    @Test
    public void AddToNoManagerTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);
        for(int i = 2; i<ug.getNumOfUser(); i++)
        {
            Register(validUsers[i],pws[i]);
            Login(validUsers[i],pws[i],null);
            assertFalse(AddShopMangerPermissions(shopID_1,ls, validUsers[i],user_1).GetFirstElement());
        }
        assertFalse(AddShopMangerPermissions(shopID_1,ls, owner,user_1).GetFirstElement());

    }

    @Test
    public void BadInputsTest()
    {
        ls.add(ShopManagersPermissions.AddProductToInventory);
        List<ShopManagersPermissions> ls_2 = new ArrayList<ShopManagersPermissions>();
        Register(validUsers[2],pws[2]);
        AppointNewShopManager(shopID_1,validUsers[2],user_1);
        assertFalse(AddShopMangerPermissions(shopID_1+1,ls, validUsers[2],user_1).GetFirstElement());
        assertFalse(AddShopMangerPermissions(-1,ls, validUsers[2],user_1).GetFirstElement());
        assertFalse(AddShopMangerPermissions(shopID_1,ls_2, validUsers[2],user_1).GetFirstElement());
        assertFalse(AddShopMangerPermissions(shopID_1,null, validUsers[2],user_1).GetFirstElement());
        assertFalse(AddShopMangerPermissions(shopID_1,ls, null,user_1).GetFirstElement());
        assertFalse(AddShopMangerPermissions(shopID_1,ls_2, validUsers[2],null).GetFirstElement());

    }


}
