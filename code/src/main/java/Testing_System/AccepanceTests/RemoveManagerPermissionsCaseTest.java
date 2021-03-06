package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Responses.ResponseT;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RemoveManagerPermissionsCaseTest extends Tester{

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private String owner;
    private String owner_pw;
    private String manager;
    private String manager_pw;
    private int shopID_1;
    private List<ShopManagersPermissions> ls;
    private List<ShopManagersPermissions> pToRemove;
    private String guest_1;
    private String guest_2;
    private String guest_3;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        guest_1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest_2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest_3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest_1,user_1, pw_1);
        Login(guest_1,user_1, pw_1);
        ResponseT<Shop> shopResponseT = CreateShop("Test_1",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        owner = validUsers[1];
        owner_pw = pws[1];
        Register(guest_2,owner,owner_pw);
        AppointNewShopOwner(shopID_1,owner,user_1);
        manager = validUsers[2];
        manager_pw = pws[2];
        Register(guest_3,manager,manager_pw);
        AppointNewShopManager(shopID_1,manager,user_1);
        List<ShopManagersPermissions> ls = new ArrayList<ShopManagersPermissions>();
        ls.add(ShopManagersPermissions.RemoveProductFromInventory);
        ls.add(ShopManagersPermissions.AddProductToInventory);

    }


    @AfterEach
    public void Demote() {
        Logout(owner);
        Logout(manager);
        String[] arr = new String[ug.getNumOfUser() - 2];
        for (int i = 3; i < ug.getNumOfUser(); i++)
            arr[i - 2] = validUsers[i];
        DeleteUserTest(arr);
    }

    @AfterAll
    public void CleanUp() {
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @BeforeEach
    public void AddPermissions()
    {
        pToRemove = new ArrayList<ShopManagersPermissions>();
        AddShopMangerPermissions(shopID_1,ls,manager,user_1);
    }

    @Test
    public void RemovePermissionsSuccessTest()
    {
        Login(manager,manager_pw,null);
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,user_1).isErrorOccurred());
    }

    @Test
    public void RemoveMultiplePermissionsSuccessTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        pToRemove.add(ShopManagersPermissions.RemoveProductFromInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,user_1).isErrorOccurred());

    }

    @Test
    public void TwoStepRemoveSuccessTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,user_1).isErrorOccurred());
        pToRemove.add(ShopManagersPermissions.RemoveProductFromInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,user_1).isErrorOccurred());

    }

    @Test
    public void AppointedOwnerRemovesPermissionTest()
    {
        Login(owner,owner_pw,null);
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,ls, manager,owner).isErrorOccurred());
    }

    @Test
    public void ManagerNotLoggedGoodTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertTrue(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,user_1).isErrorOccurred());
    }

    @Test
    public void NotLoggedInCaseFailTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, manager,owner).isErrorOccurred());
    }

    @Test
    public void RemoveToNoManagerTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        String guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest, validUsers[3],pws[3]);
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, validUsers[3], user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, owner,user_1).isErrorOccurred());

    }

    @Test
    public void RemoveNotRegisteredTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, validUsers[3], user_1).isErrorOccurred());
    }

    @Test
    public void BadInputsTest()
    {
        pToRemove.add(ShopManagersPermissions.AddProductToInventory);
        List<ShopManagersPermissions> ls_2 = new ArrayList<ShopManagersPermissions>();
        String guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest, validUsers[3],pws[3]);
        AppointNewShopManager(shopID_1,validUsers[2],user_1);
        assertFalse(!RemoveShopManagerPermissions(shopID_1+1,pToRemove, validUsers[3], user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(-1,pToRemove, validUsers[3], user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(shopID_1,ls_2, validUsers[3], user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(shopID_1,null, validUsers[3], user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, null, user_1).isErrorOccurred());
        assertFalse(!RemoveShopManagerPermissions(shopID_1,pToRemove, validUsers[3], null).isErrorOccurred());


    }


}
