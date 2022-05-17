package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.ShopInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchShopFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
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


    @BeforeAll
    public void SetUp()
    {
        user = validUsers[0];
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        Register(validUsers[0],pws[0]);
        Login(validUsers[0],pws[0]);
        ResponseT<Shop> shopResponseT = CreateShop(validUsers[0],"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID = shopResponseT.getValue().getShopID();
        f_1 = new SearchShopFilter("TestShop",null);
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
        assertTrue(GetShopsInfo(user,f_1).GetFirstElement() && GetShopsInfo(user,f_1).GetSecondElement().size() == 1);
        assertTrue(GetShopsInfo(user,f_2).GetFirstElement() && GetShopsInfo(user,f_2).GetSecondElement().size() == 0);
        assertTrue(GetShopsInfo(user,f_3).GetFirstElement() && GetShopsInfo(user,f_1).GetSecondElement().size() == 0);

    }

    @Test
    public void NoFilterCaseTest()
    {
        assertFalse(GetShopsInfo(user,null).GetFirstElement());
    }

    @Test
    public void ClosedShop()
    {
        assertTrue(GetShopsInfo(user,f_1).GetFirstElement() && GetShopsInfo(user,f_1).GetSecondElement().size() == 1);
        CloseShop(shopID,user);
        assertFalse(GetShopsInfo(user,f_1).GetFirstElement() && GetShopsInfo(user,f_1).GetSecondElement().size() == 1);
    }



}
