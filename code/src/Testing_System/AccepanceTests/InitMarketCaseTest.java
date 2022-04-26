package Testing_System.AccepanceTests;

import Testing_System.Tester;
import domain.market.MarketSystem;
import org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitMarketCaseTest extends Tester {

    @AfterEach
    public void CleanAdminUser()
    {
        String[] admin = {"MAdminM"};
        DeleteUserTest(admin);
    }

    @Test
    public void GoodStartTest()
    {
        MarketSystem.getInstance().setPaymentConnection(true);
        MarketSystem.getInstance().setSupplierConnection(true);
        MarketSystem.getInstance().createSystemManger("MAdminM", "!@#09Pp");
        assertTrue(StartMarket().GetFirstElement());

    }

    @Test
    public void NoPaymentConnectionTest()
    {
        MarketSystem.getInstance().setPaymentConnection(false);
        MarketSystem.getInstance().setSupplierConnection(true);
        MarketSystem.getInstance().createSystemManger("MAdminM", "!@#09Pp");
        assertFalse(StartMarket().GetFirstElement());
    }

    @Test
    public void NoSupplyConnectionTest()
    {
        MarketSystem.getInstance().setPaymentConnection(true);
        MarketSystem.getInstance().setPaymentConnection(false);
        MarketSystem.getInstance().createSystemManger("MAdminM", "!@#09Pp");
        assertFalse(StartMarket().GetFirstElement());
    }

    @Test
    public void NoSystemManagerTest()
    {
        MarketSystem.getInstance().setPaymentConnection(true);
        MarketSystem.getInstance().setSupplierConnection(true);
        assertFalse(StartMarket().GetFirstElement());
    }

}
