package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Responses.ResponseT;
import domain.market.MarketSystem;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private String user_2;
    private String pw_2;
    private int shopID_1;
    private int pID_1;
    private int pID_2;
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
    private String guest1;
    private String guest2;




    @BeforeAll
    public void setUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        ug.InitTest();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        user_1 = validUsers[0];
        user_2 = validUsers[1];
        pw_1 = pws[0];
        pw_2 = pws[1];
        Guest_Id = EnterMarket().getValue().getUserName();
        guest1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";

        Register(guest1, user_1,pw_1);
        Register(guest2, user_2,pw_2);
        Login(guest1,user_1,pw_1);
        Login(guest2,user_2,pw_2);

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

        ResponseT<Shop> shopResponseT;
        shopResponseT= CreateShop("Test",user_1, "TestShop_1");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        pID_1 = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1).getValue().getId();
        pID_2 = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2,amountToAdd_2,user_1,shopID_1).getValue().getId();
    }

    @AfterAll
    public void CleanUp(){
        ug.DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @Test
    public void GoodCheckOut()
    {
        AddToShoppingCart(user_2,shopID_1,pID_1,1);
        AddToShoppingCart(user_1,shopID_1,pID_1, 1);
        AddToShoppingCart(Guest_Id,shopID_1,pID_2, 1);
        assertTrue(Checkout(user_1,"Ariel Ronen","Bazal 13","Tel Aviv", "Israel", "1", "0546840084","4580000000000000", "123", "12/25").GetFirstElement());
        assertTrue(Checkout(user_2,"Nitay Vitkin","Bazal 15", "Tel Aviv", "Israel", "1","0546844084","4580000000010000", "123", "12/25").GetFirstElement());
        assertTrue(Checkout(Guest_Id,"Omry Vitkin","Bazal 25","Tel Aviv", "Israel", "1", "0546842084","4580000000020000", "123", "12/25").GetFirstElement());

    }

    @Test
    public void AfterLogoutTest()
    {
        AddToShoppingCart(user_2,shopID_1,pID_2,2);
        Logout(user_2);
        String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Login(g,user_2,pw_2);
        assertTrue(Checkout(user_2,"Nitay Vitkin","Bazal 15","Tel Aviv", "Israel", "1", "0546840084","4580000000010000","123", "12/25").GetFirstElement());
    }

    @Test
    public void EmptyCartCheckoutTest()
    {
        assertFalse(Checkout(user_1,"Ariel Ronen","Bazal 13","Tel Aviv", "Israel", "1", "0546840084","4580000000000000","123","12/25").GetFirstElement());
    }

    @Test
    public void NoConnectionPaymentTest()
    {
        AddToShoppingCart(user_1,shopID_1,pID_1, 1);
        MarketSystem.getInstance().setPaymentConnection(false);
        assertFalse(Checkout(user_1,"Ariel Ronen","Bazal 13","Tel Aviv", "Israel", "1", "0546840084","4580000000000000","123", "12/25").GetFirstElement());

    }

    @Test
    public void NoConnectionSupplierTest()
    {
        AddToShoppingCart(user_1,shopID_1,pID_1, 1);
        MarketSystem.getInstance().setSupplierConnection(false);
        assertFalse(Checkout(user_1,"Ariel Ronen","Bazal 13", "Tel Aviv", "Israel", "1","0546840084","4580000000000000", "123", "12/25").GetFirstElement());
    }

//    @Test
//    public void EmptyCartTest()
//    {
//        AddToShoppingCart();
//    }

}
