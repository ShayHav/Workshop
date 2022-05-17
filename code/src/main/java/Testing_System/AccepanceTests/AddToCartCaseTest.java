package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.ResponseT;
import domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class AddToCartCaseTest extends Tester {

    private String[] validUsers;
    private String[] badUser;
    private String[] pws;
    private String user_1;
    private String user_2;
    private String user_3;

    private String pw_user_1;
    private String pw_user_2;
    private String pw_user_3;

    private String guest;

    private int shopID;

    private UserGenerator ug;

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

    @BeforeAll
    public void SetUp()
    {
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
        badUser = ug.GetBadUsers();
        ug.InitTest();
        user_1 = validUsers[0];
        user_2 = validUsers[1];
        user_3 = validUsers[2];

        pw_user_1 = pws[0];
        pw_user_2 = pws[1];
        pw_user_3 = pws[2];

        ResponseT<User> u = EnterMarket();
        if(!u.isErrorOccurred())
            guest = u.getValue().getUserName();

        Register(user_1,pw_user_1);
        Register(user_2,pw_user_2);
        Register(user_3,pw_user_3);

        Login(user_1,pw_user_1);

        ResponseT<Shop> shopResponseT = CreateShop(user_1,"TestShop");
        if(!shopResponseT.isErrorOccurred())
            shopID = shopResponseT.getValue().getShopID();
    }

    @BeforeEach
    public void LogUsers()
    {
        Login(user_2,pw_user_2);
        Login(user_3,pw_user_3);
    }





}
