package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.Shop;
import domain.user.Role;
import domain.user.filter.SearchOfficialsFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestShopOfficialsCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;


    @BeforeAll
    public void SetUp() {
        ug = new UserGenerator();
        ug.InitTest();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        Register(user_1, pw_1);
        Login(user_1, pw_1,null);
        ResponseT<Shop> shopResponseT = CreateShop("Test",user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        for(int i =10; i<ug.getNumOfUser(); i++) {
            Register(validUsers[i], pws[i]);
        }
        AppointNewShopOwner(shopID_1, validUsers[1], user_1);
        AppointNewShopManager(shopID_1, validUsers[2], user_1);
        AppointNewShopManager(shopID_1, validUsers[3], user_1);
        Login(validUsers[1],pws[1],null);
        Login(validUsers[2],pws[2],null);
        Login(validUsers[3],pws[3],null);
    }

    @AfterAll
    public void CleanUp()
    {
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();
    }

    @Test
    public void GoodGetOfficialsTest()
    {
        List<Role> ls = new ArrayList<Role>();
        SearchOfficialsFilter officialF = new SearchOfficialsFilter(ls);
        assertTrue(!RequestShopOfficialsInfo(shopID_1, officialF,user_1).isErrorOccurred() && RequestShopOfficialsInfo(shopID_1, officialF,user_1).getValue().size() == 4);
        assertTrue(!RequestShopOfficialsInfo(shopID_1, officialF,validUsers[1]).isErrorOccurred() && RequestShopOfficialsInfo(shopID_1, officialF,user_1).getValue().size() == 4);
    }

    @Test
    public void FilterOnTest()
    {
        List<Role> ls_1 = new ArrayList<Role>();
        List<Role> ls_2 = new ArrayList<Role>();
        ls_1.add(Role.ShopFounder);
        ls_1.add(Role.ShopOwner);
        ls_2.add(Role.ShopManager);
        SearchOfficialsFilter officialF_1 = new SearchOfficialsFilter(ls_1);
        SearchOfficialsFilter officialF_2 = new SearchOfficialsFilter(ls_2);
        assertTrue(!RequestShopOfficialsInfo(shopID_1, officialF_1,user_1).isErrorOccurred() && RequestShopOfficialsInfo(shopID_1, officialF_1,user_1).getValue().size() == 2);
        assertTrue(!RequestShopOfficialsInfo(shopID_1, officialF_2,user_1).isErrorOccurred() && RequestShopOfficialsInfo(shopID_1, officialF_2,user_1).getValue().size() == 2);
    }

    @Test
    public void NotOwnerCaseTest()
    {
        List<Role> ls = new ArrayList<Role>();
        SearchOfficialsFilter officialF = new SearchOfficialsFilter(ls);
        assertFalse(!RequestShopOfficialsInfo(shopID_1, officialF,validUsers[2]).isErrorOccurred());
    }

}
