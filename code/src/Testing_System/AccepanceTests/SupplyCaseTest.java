package Testing_System.AccepanceTests;

import Service.User.UserServices;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.shop.Order;
import domain.user.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void SetUp() {
        StartMarket();
        Close
        order_1 = new Order(null, 0, null); //should find supply even if order is dead
        order_2 = new Order(null, 0, null);
        order_3 = new Order(null, 0, null);
        order_4 = new Order(null, 0, null);
        order_5 = new Order(null, 0, null);
        order_6 = new Order(null, 0, null);
        for (int i = 0; i < ug.getNumOfUser(); i++)
        {
            Register(validUserNames[i], PW[i]);
            Login(validUserNames[i], PW[i]);
        }
        String guestID = UserServices.guestLogin();

    }

    @After
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }


    @Test
    public void ConfirmedSupply()
    {
        Assert.assertTrue(Order);
    }



}
