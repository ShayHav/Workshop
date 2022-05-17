package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.shop.*;
import domain.user.Filter;
import domain.user.SearchProductFilter;
import domain.user.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
public class SearchProductCaseTest extends Tester {

    private UserGenerator ug;
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
    Product p_1;
    Product p_2;


    @BeforeAll
    public void SetUp()
    {
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
        Register(user_1, pw_1);
        Login(user_1, pw_1);
        for(int i = 1; i<ug.getNumOfUser()-1; i++) {
            Register(validUsers[i], pws[i]);
            Login(validUsers[i],pws[i]);
        }
        ResponseT<Shop> shopResponseT = CreateShop(user_1, "TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID_1 = shopResponseT.getValue().getShopID();
        ResponseT<Product> productResponseT = AddProductToShopInventory(pName_1,pDis_1,pCat_1,price_1,amountToAdd_1,user_1,shopID_1);
        if (!productResponseT.isErrorOccurred())
            p_1 = productResponseT.getValue();
        productResponseT = AddProductToShopInventory(pName_2,pDis_2,pCat_2, price_2,amountToAdd_2,user_1,shopID_1);
        if(!productResponseT.isErrorOccurred())
            p_2 = productResponseT.getValue();
        ResponseT<User> userResponseT = EnterMarket();
        if(!userResponseT.isErrorOccurred())
            guestID = userResponseT.getValue().getId();
    }

    @AfterAll
    public void CleanUp()
    {
        DeleteUserTest(validUsers);
//        LeaveMarket(guestID);
        Logout(guestID);
        ug.DeleteAdmin();

    }
    @Test
    public void SearchByNameTest()
    {
        Filter<ProductInfo> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(SearchProductByName(validUsers[i],pName_1,f).GetFirstElement());

        assertTrue(SearchProductByName(guestID,pName_2,f).GetFirstElement());
    }

    @Test
    public void SearchByKeyword()
    {
        Filter<ProductInfo> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(SearchProductByKeyword(validUsers[i],"se",f).GetFirstElement() &&SearchProductByKeyword(validUsers[i],"se",f).GetSecondElement().size() ==2);
        assertTrue(SearchProductByKeyword(guestID,"geni",f).GetFirstElement() && SearchProductByKeyword(guestID,"geni",f).GetSecondElement().size() == 1);
    }

    @Test
    public void NoProductTest()
    {
        Filter<ProductInfo> f = new SearchProductFilter(null,null,null,null,null);
        for(int i = 0; i<ug.getNumOfUser()-1; i++)
            assertTrue(SearchProductByKeyword(validUsers[i],"NoSuchProduct",f).GetFirstElement() && SearchProductByKeyword(validUsers[i],"NoSuchProduct",f).GetSecondElement().isEmpty());
        assertTrue(SearchProductByKeyword(guestID,"ResultIsGood",f).GetFirstElement() && SearchProductByKeyword(guestID,"ResultIsGood",f).GetSecondElement().isEmpty());
    }

    @Test
    public void SearchByCategory()
    {
        Filter<ProductInfo> f_1 = new SearchProductFilter(null,null,null,null,pCat_1);
        Filter<ProductInfo> f_2 = new SearchProductFilter(null,null,null,null,pCat_2);
        Filter<ProductInfo> f_3 = new SearchProductFilter(null,null,null,null,"PPPPNOCAT");
        assertTrue(SearchProductByKeyword(guestID,"",f_1).GetFirstElement() && SearchProductByKeyword(guestID, "", f_1).GetSecondElement().size() == 1);
        assertTrue(SearchProductByKeyword(guestID,"",f_2).GetFirstElement() && SearchProductByKeyword(guestID, "", f_2).GetSecondElement().size() == 1);
        assertTrue(SearchProductByKeyword(guestID,"",f_3).GetFirstElement() && SearchProductByKeyword(guestID, "", f_3).GetSecondElement().isEmpty());

    }

    @Test
    public void SearchByFilter()
    {
        Filter<ProductInfo> f_1 = new SearchProductFilter(10.0,20.0,null,null,null);
        Filter<ProductInfo> f_2 = new SearchProductFilter(1.0,90.5,null,null,null);
        Filter<ProductInfo> f_3 = new SearchProductFilter(50.0,100.0,null,null,null);
        Filter<ProductInfo> f_4 = new SearchProductFilter(3.0,100.0,null,null,null);
        assertTrue(SearchProductByKeyword(guestID,"",f_1).GetFirstElement() && SearchProductByKeyword(guestID, "", f_1).GetSecondElement().size() == 0);
        assertTrue(SearchProductByKeyword(guestID,"",f_2).GetFirstElement() && SearchProductByKeyword(guestID, "", f_2).GetSecondElement().size() == 1);
        assertTrue(SearchProductByKeyword(guestID,"",f_3).GetFirstElement() && SearchProductByKeyword(guestID, "", f_3).GetSecondElement().size() == 1);
        assertTrue(SearchProductByKeyword(guestID,"",f_4).GetFirstElement() && SearchProductByKeyword(guestID, "", f_4).GetSecondElement().size() == 2);

    }

    @Test
    public void NotRegisterUser()
    {
        Filter<ProductInfo> f_1 = new SearchProductFilter(null,null,null,null,null);
        assertFalse(SearchProductByKeyword(badUser[0],"",f_1).GetFirstElement());

    }

    @Test
    public void NotLoggedUser()
    {
        Filter<ProductInfo> f_1 = new SearchProductFilter(null,null,null,null,null);
        Register(validUsers[ug.getNumOfUser()-1],pws[ug.getNumOfUser()-1]);
        assertFalse(SearchProductByKeyword(validUsers[ug.getNumOfUser()-1],"",f_1).GetFirstElement());

    }

    @Test
    public void changedInfoTest()
    {
        Filter<ProductInfo> f_4 = new SearchProductFilter(3.0,100.0,null,null,null);
        assertTrue(SearchProductByKeyword(guestID,"",f_4).GetFirstElement() && SearchProductByKeyword(guestID, "", f_4).GetSecondElement().size() == 2);
        Product newP = new ServiceProduct(p_1.getId(), p_1.getName(),p_1.getDescription(),p_1.getCategory(),1.0,90);
        ChangeProduct(user_1,newP,shopID_1);
        assertTrue(SearchProductByKeyword(guestID,"",f_4).GetFirstElement() && SearchProductByKeyword(guestID, "", f_4).GetSecondElement().size() == 1);

    }
}