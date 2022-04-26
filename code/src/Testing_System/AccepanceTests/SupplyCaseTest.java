package Testing_System.AccepanceTests;

import Service.User.UserServices;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.market.MarketSystem;
import domain.shop.Order;
import domain.shop.Product;
import domain.shop.ProductImp;
import domain.user.TransactionInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupplyCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames =ug.GetValidUsers();
    private String[] PW = ug.GetPW();
    private final String shopname = "TestShop";
    private List<Order> orderList;
    private final int Test_1 = 6;
    private TransactionInfo ti_good_1;
    private TransactionInfo ti_good_2;
    private TransactionInfo ti_good_3;
    private TransactionInfo ti_good_4;
    private TransactionInfo ti_good_5;
    private TransactionInfo ti_good_same_address_3;
    private TransactionInfo ti_good_same_user_different_location_1;
    private TransactionInfo ti_good_guest;
    private ProductImp pi;
    private final String guest_id = "-Guest";
    private Map<Integer,Integer> products;


    private TransactionInfo ti_bad_1;
    private TransactionInfo ti_bad_2;
    private TransactionInfo ti_bad_3;
    private TransactionInfo ti_bad_4;
    private List<Product> pLs;




    public SupplyCaseTest()
    {

    }

    @BeforeAll
    public void SetUp() {

        LocalDate t_date = LocalDate.of(2022, 4, 26);
        ti_good_1 = new TransactionInfo(validUserNames[0],"Ariel Ronen","Israel&Baer-Sheva&Ragar&1&-1&-1","0546840084", "4580000000000000","12/22",t_date, 200.2 );
        ti_good_2 = new TransactionInfo(validUserNames[1],"Nitay Vitkin","Israel&Baer-Sheva&Ragar&2&1&-1","0546840080", "4580000000000000","12/22",t_date, 5 );
        ti_good_3 = new TransactionInfo(validUserNames[2],"Omry Arviv","Israel&Baer-Sheva&Ragar&3&-1&1","0546840081", "4580000000000000","12/22",t_date, 10 );
        ti_good_4 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","0546840082", "4580000000000000","12/22",t_date, 400 );
        ti_good_5 = new TransactionInfo(validUserNames[4],"Shahar Lenkry","Israel&Baer-Sheva&Ragar&50&11&-1","0546840083", "4580000000000000","12/22",t_date, 313.1 );
        ti_good_guest = new TransactionInfo(guest_id,"Oren Ronen","Israel&Tel-Aviv&Ragar&1&1&1","0546810080", "4580000000000000","12/22",t_date, 290 );

        ti_good_same_address_3 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","0546840082", "4580000000000000","12/22",t_date, 700.0 );
        ti_good_same_user_different_location_1 = new TransactionInfo(validUserNames[0],"Ariel Ronen","Israel&TelAviv&Rotchild&100&2&-1","0546840084", "4580000000000000","12/22",t_date, 370.99 );

        ti_bad_1 = new TransactionInfo("","Ariel Ronen","Israel&Baer-Sheva&Ragar&1&-1&-1","0546840084", "4580000000000000","12/22",t_date, 200.2 );
        ti_bad_2 = new TransactionInfo(validUserNames[1],"","Israel&Baer-Sheva&Ragar&2&1&-1","0546840080", "4580000000000000","12/22",t_date, 5 );
        ti_bad_3 = new TransactionInfo(validUserNames[2],"Omry Arviv","Israel&Ragar&3&-1&1","0546840081", "4580000000000000","12/22",t_date, 10 );
        ti_bad_4 = new TransactionInfo(validUserNames[3],"Shay Hav","Israel&Baer-Sheva&Ragar&3&2&1","054684008k", "4580000000000000","12/22",t_date, 400 );

        Register(validUserNames[0], PW[0]);
        Login(validUserNames[0], PW[0]);
        CreateShop(validUserNames[0],shopname );
        pi = new ProductImp("Ainal","Haolm","Haza",500,500);
        AddProductToShopInventory(pi,validUserNames[0],shopname);
        pLs = new ArrayList<Product>();
        pLs.add(pi);
        products = new HashMap<Integer,Integer>();
        MarketSystem.getInstance().setPaymentConnection(true);
        MarketSystem.getInstance().createSystemManger("MAdminM", "!@#09Pp");
//        Close

        for (int i = 0; i < ug.getNumOfUser(); i++)
        {
            Register(validUserNames[i], PW[i]);
            Login(validUserNames[i], PW[i]);
        }
        String guestID = UserServices.guestLogin();

    }

    @BeforeEach
    public void ConnectSupplier()
    {
        MarketSystem.getInstance().setSupplierConnection(true);
        StartMarket();
    }


    @AfterAll
    public void CleanUp()
    {
        RemoveProductFromShopInventory(pi.getId(),validUserNames[0],shopname);
        for (int i = 0; i < ug.getNumOfUser(); i++)
            Logout(validUserNames[i]);
        DeleteUserTest(validUserNames);
    }

    @Test
    public void ConfirmedSupplyTest()
    {
        products.putIfAbsent(pi.getId(), 10);
        assertTrue(PurchaseDelivery(ti_good_1,));
    }

    @Test
    public void NotSupplyConnectionTest()
    {
        MarketSystem.getInstance().setSupplierConnection(false);
        for(Order o : orderList)
            assertFalse(PurchaseDelivery(o));
    }

    @Test
    public void CantFindSupplyDateTest()
    {
        for(Order o : orderList)
            assertFalse(PurchaseDelivery(o));
    }




}
