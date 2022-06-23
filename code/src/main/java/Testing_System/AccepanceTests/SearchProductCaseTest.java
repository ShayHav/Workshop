package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Responses.ResponseT;
import domain.shop.*;
import domain.user.filter.Filter;
import domain.user.filter.SearchProductFilter;
import domain.user.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchProductCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] badUser;
    private String[] validUsers;
    private String[] pws;
    private String user_1;
    private String pw_1;
    private int shopID_1;
    private String guestID;
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
    private Product p_1;
    private Product p_2;
    private String[] guestArr;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        badUser = ug.GetBadUsers();
        pName_1 = "Durex";
        pDis_1 = "Protection rubber item. Single item.";
        pCat_1 = "Sex";
        price_1 = 4.6;
        amountToAdd_1 = 1000;
        amountToAdd_2 = 500;
        pName_2 = "Vibrator";
        pDis_2 = "Man genitalia sex toy.";
        pCat_2 = "Funny";
        price_2 = 99.9;
        ug = new UserGenerator();
        validUsers = ug.GetValidUsers();
        pws = ug.GetPW();
        ug.InitTest();
        user_1 = validUsers[0];
        pw_1 = pws[0];
        guestArr = new String[ug.getNumOfUser()];
        guestArr[0] = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guestArr[0],user_1, pw_1);
        Login(guestArr[0],user_1, pw_1);
        for(int i = 1; i<ug.getNumOfUser()-1; i++) {
            guestArr[i] = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            Register(guestArr[i],validUsers[i], pws[i]);
            Login(guestArr[i],validUsers[i],pws[i]);
        }
        ResponseT<Shop> shopResponseT = CreateShop("Test_1",user_1, "TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        ResponseT<Product> productResponseT = AddProductToShopInventory(1,pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1);
        if (!productResponseT.isErrorOccurred())
            p_1 = productResponseT.getValue();
        productResponseT = AddProductToShopInventory(2,pName_2,pDis_2,pCat_2, price_2,amountToAdd_2,user_1,shopID_1);
        if(!productResponseT.isErrorOccurred())
            p_2 = productResponseT.getValue();
        ResponseT<User> userResponseT = EnterMarket();
        if(!userResponseT.isErrorOccurred())
            guestID = userResponseT.getValue().getUserName();
    }

    @AfterAll
    public void CleanUp()
    {
        LeaveMarket(guestID);
        DeleteUserTest(validUsers);
        ug.DeleteAdmin();

    }
    @Test
    public void SearchByNameTest()
    {
        Filter<Product> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(!SearchProductByName(validUsers[i],pName_1,f).isErrorOccurred());

        assertTrue(!SearchProductByName(guestID,pName_2,f).isErrorOccurred());
    }

    @Test
    public void SearchByKeyword()
    {
        Filter<Product> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(!SearchProductByKeyword(validUsers[i],"se",f).isErrorOccurred() &&SearchProductByKeyword(validUsers[i],"se",f).getValue().size() ==2);
        assertTrue(!SearchProductByKeyword(guestID,"geni",f).isErrorOccurred() && SearchProductByKeyword(guestID,"geni",f).getValue().size() == 1);
    }

    @Test
    public void NoProductTest()
    {
        Filter<Product> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(!SearchProductByKeyword(validUsers[i],"NoSuchProduct",f).isErrorOccurred() && SearchProductByKeyword(validUsers[i],"NoSuchProduct",f).getValue().isEmpty());
        assertTrue(!SearchProductByKeyword(guestID,"ResultIsGood",f).isErrorOccurred() && SearchProductByKeyword(guestID,"ResultIsGood",f).getValue().isEmpty());
    }

    @Test
    public void SearchByCategory()
    {
        /*Filter<Product> f_1 = new SearchProductFilter(null,null,null,null,pCat_1);
        Filter<Product> f_2 = new SearchProductFilter(null,null,null,null,pCat_2);
        Filter<Product> f_3 = new SearchProductFilter(null,null,null,null,"PPPPNOCAT");
        assertTrue(!SearchProductByKeyword(guestID,"",f_1).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_1).getValue().size() == 1);
        assertTrue(!SearchProductByKeyword(guestID,"",f_2).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_2).getValue().size() == 1);
        assertTrue(!SearchProductByKeyword(guestID,"",f_3).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_3).getValue().isEmpty());
         */
        assertTrue(!SearchProductByCategory(validUsers[0],"sex",new SearchProductFilter()).isErrorOccurred());
        assertTrue(!SearchProductByCategory(validUsers[0],"Funny",new SearchProductFilter()).isErrorOccurred());
        assertTrue(SearchProductByCategory(validUsers[0],"sports",new SearchProductFilter()).getValue().size()==0);

    }

    @Test
    public void SearchByFilter()
    {
        Filter<Product> f_1 = new SearchProductFilter(10.0,20.0,null,null,null);
        Filter<Product> f_2 = new SearchProductFilter(1.0,90.5,null,null,null);
        Filter<Product> f_3 = new SearchProductFilter(50.0,100.0,null,null,null);
        Filter<Product> f_4 = new SearchProductFilter(3.0,100.0,null,null,null);
        assertTrue(!SearchProductByKeyword(guestID,"",f_1).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_1).getValue().size() == 0);
        assertTrue(!SearchProductByKeyword(guestID,"",f_2).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_2).getValue().size() == 1);
        assertTrue(!SearchProductByKeyword(guestID,"",f_3).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_3).getValue().size() == 1);
        assertTrue(!SearchProductByKeyword(guestID,"",f_4).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_4).getValue().size() == 2);

    }

    @Test
    public void NotRegisterUser()
    {
        Filter<Product> f_1 = new SearchProductFilter(null,null,null,null,null);
        assertFalse(!SearchProductByKeyword(badUser[0],"",f_1).isErrorOccurred());

    }

    @Test
    public void NotLoggedUser()
    {
        Filter<Product> f_1 = new SearchProductFilter(null,null,null,null,null);
        guestArr[ug.getNumOfUser()-1] = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
        Register(guestArr[ug.getNumOfUser()-1],validUsers[ug.getNumOfUser()-1],pws[ug.getNumOfUser()-1]);
        assertFalse(!SearchProductByKeyword(validUsers[ug.getNumOfUser()-1],"",f_1).isErrorOccurred());

    }

    @Test
    public void changedInfoTest()
    {
        Filter<Product> f_4 = new SearchProductFilter(3.0,100.0,null,null,null);
        assertTrue(!SearchProductByKeyword(guestID,"",f_4).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_4).getValue().size() == 2);
        Product newP = new ServiceProduct(p_1.getId(), p_1.getName(),p_1.getDescription(),p_1.getCategory(),1.0,90);
        ChangeProduct(user_1,newP,shopID_1);
        assertTrue(!SearchProductByKeyword(guestID,"",f_4).isErrorOccurred() && SearchProductByKeyword(guestID, "", f_4).getValue().size() == 1);

    }
}
