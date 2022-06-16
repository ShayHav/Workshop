package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import domain.user.filter.SearchOrderFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetOrderHistoryInShopCaseTest extends Tester {

    private UserGenerator ug;
    private String admin;
    private String pw_admin;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private String user_2;
    private String pw_2;
    private int shopID_1;
    private int shopID_2;
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
    private String guest_1;
    private String guest_2;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        guest_1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest_2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        DeleteUserTest(validUsers);
        pws = ug.GetPW();
        admin = validUsers[0];
        pw_admin = pws[0];
        ug.InitTest();
        CreateSystemManager(ug.getAdmin(),admin,pw_admin);
        user_1 = validUsers[1];
        user_2 = validUsers[2];
        pw_1 = pws[1];
        pw_2 = pws[2];
        Guest_Id = EnterMarket().getValue().getUserName();
        Register(guest_1,user_1,pw_1);
        Register(guest_2,user_2,pw_2);
        Login(guest_1,user_1,pw_1);
        Login(guest_2,user_2,pw_2);

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
        shopResponseT= CreateShop("Test",user_2, "TestShop_1");
        if(!shopResponseT.isErrorOccurred())
            shopID_2 = shopResponseT.getValue().getShopID();
        pID_1 = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1).getValue().getId();
        pID_2 = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2,amountToAdd_2,user_1,shopID_1).getValue().getId();
        AddToShoppingCart(user_1,shopID_1,pID_1,10);
        Checkout(user_1,"Ariel Ronen", "TLV Bazal 13", "0546840084","4580000000000000","12/25");
        AddToShoppingCart(user_1,shopID_1,pID_2,11);
        Checkout(user_2,"Nitay Vitkin", "TLV Bazal 13", "0546840014","4580000000000001","12/25");
        AddToShoppingCart(user_1,shopID_1,pID_1,8);
        Checkout(admin,"Omery Arviv", "TLV Bazal 13", "0546840083","4580000000000002","12/25");

    }

    @AfterAll
    public void CleanUp()
    {
        LeaveMarket(Guest_Id);
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @Test
    public void GoodHistoryNoFilter()
    {
        SearchOrderFilter searchF = new SearchOrderFilter();
        assertTrue(!RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).isErrorOccurred() && RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).getValue().size() == 3);
        assertTrue(!RequestInformationOfShopsSalesHistory(shopID_2,searchF,admin).isErrorOccurred() && RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).getValue().size() == 0);
    }

    @Test
    public void NoSystemManagerTest()
    {
        SearchOrderFilter searchF = new SearchOrderFilter();
        assertFalse(!RequestInformationOfShopsSalesHistory(shopID_1,searchF,user_2).isErrorOccurred());
    }

    @Test
    public void FilterGoodCase()
    {
        LocalDate min =LocalDate.of(2021, 4, 26);
        LocalDate max = LocalDate.of(2023, 4, 26);;
        SearchOrderFilter searchF = new SearchOrderFilter(0.0,2000000.0,min,max);
        assertTrue(!RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).isErrorOccurred() && RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).getValue().size() == 3);
        assertTrue(!RequestInformationOfShopsSalesHistory(shopID_2,searchF,admin).isErrorOccurred() && RequestInformationOfShopsSalesHistory(shopID_1,searchF,admin).getValue().size() == 0);
    }
}
