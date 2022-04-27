package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.shop.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    List<ShopManagersPermissions> ls;


    @BeforeAll
    public void SetUp()
    {
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
        Register(user_1, pw_1);
        Login(user_1, pw_1);
        Register(owner,owner_pw);
        Register(manager, manager_pw);
        AppointNewShopManager(shopID_1,manager,user_1);
        AppointNewShopOwner(shopID_1,owner,user_1);
        ls = new ArrayList<ShopManagersPermissions>();
        ls.add(ShopManagersPermissions.ChangeProductsDetail);
        shopID_1 = CreateShop(user_1, "TestShop").GetSecondElement();
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
        pID_1 = AddProductToShopInventory(pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1).GetSecondElement();
        pID_2 = AddProductToShopInventory(pName_2,pDis_2,pCat_2,price_2,amountToAdd_2,user_1,shopID_1).GetSecondElement();
    }

    @BeforeEach
    public void LogUsers()
    {
        Login(owner, owner_pw);
        Login(manager, manager_pw);
    }

    @Test
    public void ChangeProductAmountGoodTest()
    {
        assertTrue(ChangeProduct(user_1,pID_1,shopID_1).GetFirstElement());
        assertTrue(ChangeProduct(owner,pID_1,shopID_1).GetFirstElement());
        assertTrue(ChangeProduct(manager,pID_1,shopID_1).GetFirstElement());
        RemoveShopManagerPermissions(shopID_1,ls,manager,user_1);
        assertFalse(ChangeProduct(manager,pID_1,shopID_1).GetFirstElement());
    }

    @Test
    public void NotLoggedInTest()
    {
        Logout(owner);
        assertFalse(ChangeProduct(owner,pID_1,shopID_1).GetFirstElement());

    }

    @Test
    public void NotRegisteredTest()
    {
        assertFalse(ChangeProduct(validUsers[3],pID_1,shopID_1).GetFirstElement());
    }
}
