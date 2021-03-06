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
public class AddProductToShopCaseTest extends Tester {

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
//    private String GuestId_2;
//    private String GuestId_3;


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
        Guest_Id = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
//        GuestId_2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
//        GuestId_3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
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
        String GuestId_2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        String GuestId_3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";

        Register(GuestId_2 ,user_1, pw_1);
        Login(GuestId_2,user_1,pw_1);
        Register(GuestId_3, user_2, pw_2);
        Login(GuestId_3, user_2,pw_2);
        ResponseT<Shop> shopResponseT;
        shopResponseT= CreateShop("Test_1", user_1, "TestShop_1");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        shopResponseT= CreateShop("Test_2",user_2, "TestShop_2");
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        shopResponseT= CreateShop("Test_3",user_2, "TestShop_3");
        if(!shopResponseT.isErrorOccurred())
            shopID_3 = shopResponseT.getValue().getShopID();

    }

    @AfterEach
    public void Reset()
    {
        DeleteUserTest(validUsers);
    }

    @AfterAll
    public void CleanUp()
    {
        ug.DeleteAdmin();
    }

    @Test
    public void GoodInsertTest()
    {
        assertTrue(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertTrue(!(AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2, amountToAdd_2, user_2, shopID_2).isErrorOccurred()));
        assertTrue(!(AddProductToShopInventory(3,pName_1,pDis_1,pCat_1,price_2, amountToAdd_2, user_2, shopID_3).isErrorOccurred()));
    }

    @Test
    public void SameProductDifferentShopInsertTest()
    {
        assertTrue(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertTrue(!(AddProductToShopInventory(2,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_2, shopID_2).isErrorOccurred()));
//        assertTrue(!(AddProductToShopInventory(3,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_2, shopID_3).isErrorOccurred()));
    }

    @Test
    public void AppointOwnerInsertTest()
    {
        String guestId_4 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guestId_4,validUsers[2],pws[2]);
        Login(guestId_4, validUsers[2],pws[2]);
        AppointNewShopOwner(shopID_1,validUsers[2],user_1);
        assertTrue(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[2], shopID_1).isErrorOccurred()));
    }

    @Test
    public void AppointManagerInsertTest()
    {
        String guestId_4 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guestId_4,validUsers[2],pws[2]);
        Login(guestId_4, validUsers[2],pws[2]);
        AppointNewShopManager(shopID_1,validUsers[2],user_1);
        ShopManagersPermissions sp = ShopManagersPermissions.AddProductToInventory;
        List<ShopManagersPermissions> ls = new ArrayList<ShopManagersPermissions>();
        ls.add(sp);
        AddShopMangerPermissions(shopID_1,ls,validUsers[2],user_1);
        assertTrue((!AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[2], shopID_1).isErrorOccurred()));
    }

    @Test
    public void NotRegisteredUserTest()
    {
        assertFalse(!(AddProductToShopInventory(10,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[2], shopID_1).isErrorOccurred()));
    }

    @Test
    public void NotLoggedInUserTest()
    {
        Logout(user_1);
        assertFalse(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
    }

//    @Test
//    public void NoPermissionTest()
//    {
//        String guestId_4 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
//        String guestId_5 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
//        String guestId_6 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
//        Register(guestId_4, validUsers[2],pws[2]);
//        Login(guestId_4, validUsers[2],pws[2]);
//        Register(guestId_5,validUsers[3],pws[3]);
//        Login(guestId_5,validUsers[3],pws[3]);
//        Register(guestId_6,validUsers[ug.getNumOfUser()-1],pws[ug.getNumOfUser()-1]);
//        Login(guestId_6,validUsers[ug.getNumOfUser()-1],pws[ug.getNumOfUser()-1]);
//        AppointNewShopManager(shopID_2,validUsers[ug.getNumOfUser()-1],user_2); //appointed, no permissions were given yet
//        AppointNewShopManager(shopID_1,validUsers[2],user_1);
//        ShopManagersPermissions sp = ShopManagersPermissions.AddProductToInventory;
//        List<ShopManagersPermissions> ls = new ArrayList<ShopManagersPermissions>();
//        ls.add(sp);
//        AddShopMangerPermissions(shopID_1,ls,validUsers[2],user_1);
//        AppointNewShopOwner(shopID_1, validUsers[3],user_1);
//        assertFalse(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[2], shopID_2).isErrorOccurred()));
//        assertFalse(!(AddProductToShopInventory(2,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[3], shopID_2).isErrorOccurred()));
//        assertFalse(!(AddProductToShopInventory(3,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_2).isErrorOccurred()));
//        assertFalse(!(AddProductToShopInventory(4,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, validUsers[ug.getNumOfUser()-1], shopID_2).isErrorOccurred()));
//
//    }

    @Test
    public void ProductAlreadyInShopTest()
    {
        assertTrue(!(AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertFalse(!(AddProductToShopInventory(1,pName_1,pDis_2,pCat_2,price_2, amountToAdd_2, user_1, shopID_1).isErrorOccurred()));
    }

    @Test
    public void BadProductInputsTest() {
        assertFalse(!(AddProductToShopInventory(1,pName_1, "", pCat_1, price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertFalse(!(AddProductToShopInventory(1,pName_1, pDis_1, "", price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertFalse(!(AddProductToShopInventory(1,pName_1, pDis_1, pCat_1, -1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));
        assertFalse(!(AddProductToShopInventory(1,pName_1, pDis_1, pCat_1, price_1, 0, user_1, shopID_1).isErrorOccurred()));
        assertFalse(!(AddProductToShopInventory(1,null, pDis_1, pCat_1, price_1, amountToAdd_1, user_1, shopID_1).isErrorOccurred()));

    }

}
