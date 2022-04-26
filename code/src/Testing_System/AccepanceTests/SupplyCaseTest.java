package Testing_System.AccepanceTests;

import Service.User.UserServices;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.market.MarketSystem;
import domain.shop.Order;
import domain.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;


public class SupplyCaseTest extends Tester {

    private Order order_1;
    private Order order_2;
    private Order order_3;
    private Order order_4;
    private Order order_5;
    private Order order_6;
    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames =ug.GetValidUsers();
    private String[] PW = ug.GetPW();
    private List<Order> orderList;
    private final int numOfOrders = 6;

    public SupplyCaseTest()
    {

    }

    @BeforeAll
    public void SetUp() {
        order_1 = new Order(null, 0, null); //should find supply even if order is dead
        order_2 = new Order(null, 0, null);
        order_3 = new Order(null, 0, null);
        order_4 = new Order(null, 0, null);
        order_5 = new Order(null, 0, null);
        order_6 = new Order(null, 0, null);
        orderList = new ArrayList<Order>();
        orderList.add(order_1);
        orderList.add(order_2);
        orderList.add(order_3);
        orderList.add(order_4);
        orderList.add(order_5);
        orderList.add(order_6);
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
        for (int i = 0; i < ug.getNumOfUser(); i++)
            Logout(validUserNames[i]);
        DeleteUserTest(validUserNames);
    }

    @Test
    public void ConfirmedSupplyTest()
    {
        for(Order o : orderList)
           assertTrue(PurchaseDelivery(o));
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
