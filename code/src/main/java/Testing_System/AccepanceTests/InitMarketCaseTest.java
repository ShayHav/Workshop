package Testing_System.AccepanceTests;

import Testing_System.Result;
import Testing_System.Tester;
import domain.market.*;
import org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
public class InitMarketCaseTest extends Tester {

    /*Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password)*/
    private PaymentService payment;
    private SupplyService supply;
    private final String userID = "MAdminM";
    private final String pw = "!@AQgb75";

    @AfterEach
    public void CleanAdminUser()
    {

        String[] admin = {"MAdminM"};
        DeleteUserTest(admin);
    }

    @Test
    public void GoodStartTest()
    {
        payment = new PaymentServiceImp();
        supply = new SupplyServiceImp();
        assertTrue(!StartMarket(payment,supply,userID,pw).isErrorOccurred());
    }

    @Test
    public void NoPaymentConnectionTest()
    {
        payment = null;
        supply = new SupplyServiceImp();
        assertFalse(!StartMarket(payment,supply,userID,pw).isErrorOccurred());
    }

    @Test
    public void NoSupplyConnectionTest()
    {
        payment = new PaymentServiceImp();
        supply = null;
        assertFalse(!StartMarket(payment,supply,userID,pw).isErrorOccurred());
    }

    @Test
    public void NoSystemManagerTest()
    {
        payment = new PaymentServiceImp();
        supply = new SupplyServiceImp();
        assertFalse(!StartMarket(payment,supply,userID,null).isErrorOccurred());
    }

}
