package Testing_System.AccepanceTests;


import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import domain.user.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddToCartCaseTest extends Tester {

    private String[] validUsers;
    private String[] badUser;
    private String[] pws;
    private String user_1;
    private String user_2;
    private String user_3;

    private String pw_user_1;
    private String pw_user_2;
    private String pw_user_3;

    private String guest;

    private int shopID;

    private UserGenerator ug;

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
    private int pID_1;
    private  int pID_2;
    private String g1;
    private String g2;
    private String g3;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        pName_1 = "Durex";
        pDis_1 = "Protection rubber item. Single item.";
        pCat_1 = "Sex";
        price_1 = 4.6;
        amountToAdd_1 = 1000;
        amountToAdd_2 = 500;
        pName_2 = "Vibrator";
        pDis_2 = "Man genitalia sex toy.";
        pCat_2 = "Funny";
        price_2 = 99.9;
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        validUsers = ug.GetValidUsers();
        badUser = ug.GetBadUsers();
        ug.InitTest();
        user_1 = validUsers[0];
        user_2 = validUsers[1];
        user_3 = validUsers[2];

        pw_user_1 = pws[0];
        pw_user_2 = pws[1];
        pw_user_3 = pws[2];

        ResponseT<User> u = EnterMarket();
        if(!u.isErrorOccurred())
            guest = u.getValue().getUserName();

        g1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        g2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        g3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(g1, user_1,pw_user_1);
        Register(g2, user_2,pw_user_2);
        Register(g3,user_3,pw_user_3);

        Login(g1,user_1,pw_user_1);

        ResponseT<Shop> shopResponseT = CreateShop("Test_1",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID = shopResponseT.getValue().getShopID();
        pID_1 = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1, amountToAdd_1, user_1, shopID).getValue().getId();
        pID_2 = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2, amountToAdd_2, user_1, shopID).getValue().getId();
    }

    @AfterAll
    public void LogUsers()
    {
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @Test
    public void AddGoodTest()
    {
        assertTrue(!AddToShoppingCart(user_2,shopID,pID_1,3).isErrorOccurred());
        assertTrue(!AddToShoppingCart(user_2,shopID,pID_1,3).isErrorOccurred());
        assertTrue(!AddToShoppingCart(user_3,shopID,pID_2,3).isErrorOccurred());
    }

    @Test
    public void BadProductIDTest()
    {
        assertFalse(!AddToShoppingCart(user_2,shopID,400214,3).isErrorOccurred());
    }

    @Test
    public void NotInStockTest()
    {
        assertFalse(!AddToShoppingCart(user_2,shopID,pID_2,20000).isErrorOccurred());
        assertFalse(!AddToShoppingCart(user_1,shopID,pID_1,20000).isErrorOccurred());

    }

    @Test
    public void EditCartTest()
    {
        AddToShoppingCart(user_2,shopID,pID_1,3);
        assertTrue(!EditShoppingCart(user_2,shopID,pID_1,1).isErrorOccurred());
    }



}
