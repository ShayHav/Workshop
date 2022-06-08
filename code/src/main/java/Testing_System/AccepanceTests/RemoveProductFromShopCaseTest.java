package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.shop.ShopManagersPermissions;
import domain.shop.ShopPermissions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RemoveProductFromShopCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private String user_2;
    private String pw_2;
    private int shopID_1;
    private int shopID_2;
    private int shopID_3;
    private String pName_1;
    private String pDis_1;
    private String pCat_1;
    private double price_1;
    private int amountToAdd_1;
    private String pName_2;
    private String pDis_2;
    private String pCat_2;
    private double price_2;
    private int amountToAdd_2;
    private String Guest_Id;
    private int pID_1;
    private int pID_2;
    private int pID_3;
    private int pID_4;
    private int pID_5;
    private int pID_6;
    private String guest_1;
    private String guest_2;




    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        user_2 = validUsers[1];
        pw_1 = pws[0];
        pw_2 = pws[1];
        Guest_Id = EnterMarket().getValue().getUserName();
        pName_1 = "Durex";
        pDis_1 = "Protection rubber item. Single item.";
        pCat_1 = "Sex";
        price_1 = 4.6;
        amountToAdd_1 = 1000;
        pName_2 = "Vibrator";
        pDis_2 = "Man genitalia sex toy.";
        pCat_2 = "Sex";
        price_2 = 99.9;
        amountToAdd_2 = 50;
    }

    @BeforeEach
    public void SetShops()
    {
        Register(guest_1,user_1, pw_1);
        Login(guest_1,user_1,pw_1);
        Register(guest_2,user_2, pw_2);
        Login(guest_2,user_2,pw_2);
        shopID_1 = CreateShop("Test_1",user_1, "TestShop_1").getValue().getShopID();
        shopID_2 = CreateShop("Test_2",user_2, "TestShop_2").getValue().getShopID();
        shopID_3 = CreateShop("Test_3",user_2, "TestShop_3").getValue().getShopID();
        pID_1 = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_1).getValue().getId();
        pID_2 = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2, amountToAdd_2, user_1, shopID_1).getValue().getId();

        pID_3 = AddProductToShopInventory(3,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_2, shopID_2).getValue().getId();
        pID_4 = AddProductToShopInventory(4,pName_2,pDis_2,pCat_2,price_2, amountToAdd_2, user_2, shopID_2).getValue().getId();

        pID_5 = AddProductToShopInventory(5,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_2, shopID_3).getValue().getId();
        pID_6 = AddProductToShopInventory(6,pName_2,pDis_2,pCat_2,price_2, amountToAdd_2, user_2, shopID_3).getValue().getId();

    }

    @AfterEach
    public void Reset()
    {
        DeleteUserTest(validUsers);
    }

    @AfterAll
    public void CleanUp()
    {
        LeaveMarket(Guest_Id);
        ug.DeleteAdmin();
    }

    @Test
    public void GoodRemovalTest() {
        assertTrue(!RemoveProductFromShopInventory(pID_1, user_1, shopID_1).isErrorOccurred());
        assertTrue(!RemoveProductFromShopInventory(pID_2, user_1, shopID_1).isErrorOccurred());

        assertTrue(!RemoveProductFromShopInventory(pID_3, user_2, shopID_2).isErrorOccurred());
        assertTrue(!RemoveProductFromShopInventory(pID_4, user_2, shopID_2).isErrorOccurred());

    }

    @Test
    public void SameProductsAndFounderDifferentShopRemovalTest()
    {
        assertTrue(!RemoveProductFromShopInventory(pID_3, user_2, shopID_2).isErrorOccurred());
        assertTrue(!RemoveProductFromShopInventory(pID_4, user_2, shopID_2).isErrorOccurred());

        assertTrue(!RemoveProductFromShopInventory(pID_5, user_2, shopID_3).isErrorOccurred());
        assertTrue(!RemoveProductFromShopInventory(pID_6, user_2, shopID_3).isErrorOccurred());
    }

    @Test
    public void AppointOwnerRemovalTest()
    {
        String guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest, validUsers[2],pws[2]);
        Login(guest, validUsers[2],pws[2]);
        AppointNewShopOwner(shopID_1,validUsers[2],user_1);
        assertTrue(!RemoveProductFromShopInventory(pID_2, validUsers[2], shopID_1).isErrorOccurred());
        assertTrue(!RemoveProductFromShopInventory(pID_1, validUsers[2], shopID_1).isErrorOccurred());
    }

    @Test
    public void AppointManagerInsertTest()
    {
        String guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guest, validUsers[2],pws[2]);
        Login(guest, validUsers[2],pws[2]);
        AppointNewShopManager(shopID_1,validUsers[2],user_1);
        ShopManagersPermissions sp = ShopManagersPermissions.RemoveProductFromInventory;
        List<ShopManagersPermissions> ls = new ArrayList<ShopManagersPermissions>();
        ls.add(sp);
        AddShopMangerPermissions(shopID_1,ls,validUsers[2],user_1);
        assertTrue(!RemoveProductFromShopInventory(pID_2, validUsers[2], shopID_1).isErrorOccurred());
    }

    @Test
    public void NotRegisteredUserTest()
    {
        assertFalse(!RemoveProductFromShopInventory(pID_2, validUsers[2], shopID_1).isErrorOccurred());
    }

    @Test
    public void NotLoggedInUserTest()
    {
        Logout(user_1);
        assertFalse(!RemoveProductFromShopInventory(pID_2, user_1, shopID_1).isErrorOccurred());
    }

    @Test
    public void NoPermissionRemovalTest() {
        String g1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        String g2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        String g3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g1, validUsers[2], pws[2]);
        Login(g1,validUsers[2], pws[2]);
        Register(g2,validUsers[3], pws[3]);
        Login(g2,validUsers[3], pws[3]);
        Register(g3,validUsers[ug.getNumOfUser() - 1], pws[ug.getNumOfUser() - 1]);
        Login(g3,validUsers[ug.getNumOfUser() - 1], pws[ug.getNumOfUser() - 1]);
        AppointNewShopManager(shopID_2, validUsers[ug.getNumOfUser() - 1], user_2); //appointed, no permissions were given yet
        AppointNewShopManager(shopID_1, validUsers[2], user_1);
        ShopManagersPermissions sp = ShopManagersPermissions.RemoveProductFromInventory;
        List<ShopManagersPermissions> ls = new ArrayList<ShopManagersPermissions>();
        ls.add(sp);
        AddShopMangerPermissions(shopID_1, ls, validUsers[2], user_1);
        AppointNewShopOwner(shopID_1, validUsers[3], user_1);

        assertFalse(!RemoveProductFromShopInventory(pID_2, validUsers[ug.getNumOfUser() - 1], shopID_2).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_1, validUsers[ug.getNumOfUser() - 1], shopID_2).isErrorOccurred());

        assertFalse(!RemoveProductFromShopInventory(pID_1, validUsers[2], shopID_2).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_2, validUsers[2], shopID_2).isErrorOccurred());

        assertFalse(!RemoveProductFromShopInventory(pID_2, validUsers[3], shopID_2).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_1, validUsers[3], shopID_2).isErrorOccurred());


    }

    @Test
    public void ProductAlreadyRemovedTest()
    {
        assertTrue(!RemoveProductFromShopInventory(pID_1, user_1, shopID_1).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_2, user_1, shopID_1).isErrorOccurred());
    }

    @Test
    public void BadProductInputsTest() {
        assertFalse(!RemoveProductFromShopInventory(-1, user_1, shopID_1).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_2, null, shopID_1).isErrorOccurred());
        assertFalse(!RemoveProductFromShopInventory(pID_2, user_1, -1).isErrorOccurred());

    }

}
