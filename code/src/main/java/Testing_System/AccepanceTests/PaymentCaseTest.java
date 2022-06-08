package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.market.*;
import domain.shop.*;
import domain.shop.Order;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchProductFilter;
import domain.user.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import Testing_System.Tester;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUserNames;
    private String[] pws;
    private final String shopname = "TestShop";
    private TransactionInfo ti_good_1;
    private TransactionInfo ti_good_2;
    private TransactionInfo ti_good_3;
    private TransactionInfo ti_good_4;
    private TransactionInfo ti_good_5;
    private TransactionInfo ti_good_same_credit_3;
    private TransactionInfo ti_good_same_user_different_location_1;
    private TransactionInfo ti_good_guest;
    private String guest_id;
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
    private int shopID;
    private PaymentService payment;
    private SupplyService supply;
    private final String userID = "MAdminM";
    private final String pw = "!@AQgb75";

    private TransactionInfo ti_bad_1;
    private TransactionInfo ti_bad_2;
    private TransactionInfo ti_bad_3;
    private TransactionInfo ti_bad_4;
    private int pID_1;
    private int pID_2;



    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug = new UserGenerator();
        validUserNames = ug.GetValidUsers();
        pws = ug.GetPW();
        LocalDate t_date = LocalDate.of(2022, 4, 26);
        ti_good_1 = new TransactionInfo(validUserNames[0],"Ariel Ronen","Israel&Baer-Sheva&Ragar&1&-1&-1","0546840084", "4580000000000001","12/22",t_date, 200.2 );
        ti_good_2 = new TransactionInfo(validUserNames[1],"Nitay Vitkin","Israel&Baer-Sheva&Ragar&2&1&-1","0546840080", "4580000000000002","12/22",t_date, 5 );
        ti_good_3 = new TransactionInfo(validUserNames[2],"Omry Arviv","Israel&Baer-Sheva&Ragar&3&-1&1","0546840081", "4580000000000003","12/22",t_date, 10 );
        ti_good_4 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","0546840082", "4580000000000004","12/22",t_date, 400 );
        ti_good_5 = new TransactionInfo(validUserNames[4],"Shahar Lenkry","Israel&Baer-Sheva&Ragar&50&11&-1","0546840083", "4580000000000005","12/22",t_date, 313.1 );
        ti_good_guest = new TransactionInfo(guest_id,"Oren Ronen","Israel&Tel-Aviv&Ragar&1&1&1","0546810080", "4580000000000006","12/22",t_date, 290 );

        ti_good_same_credit_3 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","0546840082", "4580000000000003","12/22",t_date, 700.0 );
        ti_good_same_user_different_location_1 = new TransactionInfo(validUserNames[0],"Ariel Ronen","Israel&TelAviv&Rotchild&100&2&-1","0546840084", "4580000000000000","12/22",t_date, 370.99 );

        ti_bad_1 = new TransactionInfo("","Ariel Ronen","Israel&Baer-Sheva&Ragar&1&-1&-1","0546840084", "4580000000000000","12/22",t_date, 200.2 );
        ti_bad_2 = new TransactionInfo(validUserNames[1],"","Israel&Baer-Sheva&Ragar&2&1&-1","0546840080", "4580000000000000","12/22",t_date, 5 );
        ti_bad_3 = new TransactionInfo(validUserNames[2],"Omry Arviv","Israel&Ragar&3&-1&1","0546840081", "4580000000000000","12/22",t_date, 10 );
        ti_bad_4 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","054684008k", "4580000000000000","12/22",t_date, 400 );
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
        payment = new PaymentServiceImp();
        supply = new SupplyServiceImp();
        ug.InitTest();
        for(int i =0; i< ug.getNumOfUser(); i++)
        {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(g,validUserNames[i],pws[i]);
            Login(g,validUserNames[i],pws[i]);
        }
        guest_id = EnterMarket().getValue().getUserName();
        shopID = CreateShop("Test",validUserNames[0],shopname).getValue().getShopID();
        pID_1 = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,validUserNames[0],shopID).getValue().getId();
        pID_2 = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2,price_2,amountToAdd_2,validUserNames[0],shopID).getValue().getId();

    }

    @AfterAll
    public void CleanUp()
    {
        LeaveMarket(guest_id);
        ug.DeleteUserTest(validUserNames);
        ug.DeleteAdmin();
    }

    @Test
    public void ConfirmedPaymentTest()
    {
        assertTrue(!Payment(ti_good_1).isErrorOccurred());
        assertTrue(!Payment(ti_good_2).isErrorOccurred());
        assertTrue(!Payment(ti_good_3).isErrorOccurred());
        assertTrue(!Payment(ti_good_4).isErrorOccurred());
        assertTrue(!Payment(ti_good_5).isErrorOccurred());
        assertTrue(!Payment(ti_good_guest).isErrorOccurred());

    }

    @Test
    public void NotSupplyConnectionTest()
    {
        MarketSystem.getInstance().setPaymentConnection(false);
        assertFalse(!Payment(ti_good_1).isErrorOccurred());
        assertFalse(!Payment(ti_good_2).isErrorOccurred());
        assertFalse(!Payment(ti_good_3).isErrorOccurred());
        assertFalse(!Payment(ti_good_4).isErrorOccurred());
        MarketSystem.getInstance().setPaymentConnection(true);

    }

    @Test
    public void SameCreditTest()
    {
        assertTrue(!Payment(ti_good_3).isErrorOccurred());
        assertTrue(!Payment(ti_good_same_credit_3).isErrorOccurred());

    }


    @Test
    public void BadTITest()
    {
        assertFalse(!Payment(ti_bad_1).isErrorOccurred());
        assertFalse(!Payment(ti_bad_2).isErrorOccurred());
        assertFalse(!Payment(ti_bad_3).isErrorOccurred());
        assertFalse(!Payment(ti_bad_4).isErrorOccurred());

    }
}
