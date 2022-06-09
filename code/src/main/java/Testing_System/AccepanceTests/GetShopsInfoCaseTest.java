package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import domain.shop.ShopInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchShopFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetShopsInfoCaseTest extends Tester {

    private UserGenerator ug;
    private String[] validUsers;
    private String[] pws;
    private Filter<Shop> f_1;
    private Filter<Shop> f_2;
    private Filter<Shop> f_3;
    private Filter<Shop> f_4;
    private String user;
    private int shopID;
    private String guest1;
    private String guest2;
    private String guest3;
    private String guest4;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        guest1 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest2 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest3 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        guest4 = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        user = validUsers[0];
        pws = ug.GetPW();
        ug.InitTest();
        Register(guest1, validUsers[0],pws[0]);
        Login(guest1,validUsers[0],pws[0]);
        ResponseT<Shop> shopResponseT = CreateShop("Test_1",validUsers[0],"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID = shopResponseT.getValue().getShopID();
        f_1 = new SearchShopFilter("s1",null);
        f_2= new SearchShopFilter(null,null);
        f_3 = new SearchShopFilter("Tepop",null);

    }

    @AfterAll
    public void CleanUp()
    {
        ug.DeleteAdmin();
        DeleteUserTest(validUsers);
    }

    @Test
    public void GoodCaseTest()
    {
        assertTrue(!GetShopsInfo(user,f_1).isErrorOccurred() && GetShopsInfo(user,f_1).getValue().size() == 1);
        assertTrue(!GetShopsInfo(user,f_2).isErrorOccurred() && GetShopsInfo(user,f_2).getValue().size() == 0);
        assertTrue(!GetShopsInfo(user,f_3).isErrorOccurred() && GetShopsInfo(user,f_1).getValue().size() == 0);

    }

    @Test
    public void NoFilterCaseTest()
    {
        assertFalse(!GetShopsInfo(user,null).isErrorOccurred());
    }

    @Test
    public void ClosedShop()
    {
        assertTrue(!GetShopsInfo(user,f_1).isErrorOccurred() && GetShopsInfo(user,f_1).getValue().size() == 1);
        CloseShop(shopID,user);
        assertFalse(!GetShopsInfo(user,f_1).isErrorOccurred() && GetShopsInfo(user,f_1).getValue().size() == 1);
    }



}
