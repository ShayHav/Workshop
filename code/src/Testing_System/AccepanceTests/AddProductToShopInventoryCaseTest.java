package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.Shop;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */

public class AddProductToShopInventoryCaseTest extends Tester {

    private final String pName = "Ainal";
    private final String pDis = "Haolam";
    private final String pCat = " Mard";
    private final double price = 222;
    private final int amount = 2222;
    private UserGenerator ug = new UserGenerator();
    private final String[] validUsers = ug.GetValidUsers();
    private final String[] PW = ug.GetPW();
    private int shopID;

    @BeforeAll
    public void SetUp()
    {
        Register(validUsers[0],PW[0]);
        Login(validUsers[0],PW[0]);

        ResponseT<Shop> shopResponseT = CreateShop(validUsers[0],"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID = shopResponseT.getValue().getShopID();
    }
    @AfterAll
    public void CleanUp()
    {
        DeleteUserTest(validUsers);
    }

    @Test
    public void AddNewProductTest()
    {
        //AddProductToShopInventory();
    }




}
