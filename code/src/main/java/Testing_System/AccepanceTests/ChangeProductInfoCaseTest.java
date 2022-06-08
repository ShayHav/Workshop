package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeProductInfoCaseTest extends Tester{
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
    private Product pID_1;
    private Product pID_2;
    private Product p_3;
    private Product p_4;
    private String guest1;
    private String guest2;
    private String guest3;
    List<ShopManagersPermissions> ls;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        owner = validUsers[1];
        owner_pw = pws[1];
        manager = validUsers[2];
        manager_pw = pws[2];
        guest1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";

        Register(guest1,user_1, pw_1);
        Login(guest1,user_1, pw_1);
        Register(guest2,owner,owner_pw);
        Register(guest3,manager, manager_pw);
        AppointNewShopManager(shopID_1,manager,user_1);
        AppointNewShopOwner(shopID_1,owner,user_1);
        ls = new ArrayList<ShopManagersPermissions>();
        ls.add(ShopManagersPermissions.ChangeProductsDetail);
        ResponseT<Shop> shopResponseT = CreateShop("Test",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
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
        ResponseT<Product> productResponseT = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1);
        if (!productResponseT.isErrorOccurred())
            pID_1 = productResponseT.getValue();
        productResponseT = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2,amountToAdd_2,user_1,shopID_1);
        if(!productResponseT.isErrorOccurred())
            pID_2 = productResponseT.getValue();
    }

    @BeforeEach
    public void LogUsers()
    {
        Login(guest2,owner, owner_pw);
        Login(guest3,manager, manager_pw);
    }

    @Test
    public void ChangeProductAmountGoodTest()
    {
        assertTrue(!ChangeProduct(user_1,pID_1,shopID_1).isErrorOccurred());
        assertTrue(!ChangeProduct(owner,pID_1,shopID_1).isErrorOccurred());
        assertTrue(!ChangeProduct(manager,pID_1,shopID_1).isErrorOccurred());
        RemoveShopManagerPermissions(shopID_1,ls,manager,user_1);
        assertFalse(!ChangeProduct(manager,pID_1,shopID_1).isErrorOccurred());
    }

    @Test
    public void NotLoggedInTest()
    {
        Logout(owner);
        assertFalse(!ChangeProduct(owner,pID_1,shopID_1).isErrorOccurred());

    }

    @Test
    public void NotRegisteredTest()
    {
        assertFalse(!ChangeProduct(validUsers[3],pID_1,shopID_1).isErrorOccurred());
    }
}
