package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.user.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginCaseTest extends Tester {

    UserGenerator ug = new UserGenerator();
    String[] validUsernames = ug.GetValidUsers();
    String[] passwords = ug.GetPW();
    String[] notRegisteredUsers = ug.GetNotRegisteredUsers();

    @Before
    public void SetUp()
    {
        for(int i = 0; i< ug.getNumOfUser(); i++)
            Register(validUsernames[i], passwords[i]);
    }

    @After
    public void CleanCase()
    {
        DeleteUserTest(validUsernames);
    }


    @Test
    public void GoodLogin()
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
            Assert.assertTrue(Login(validUsernames[i], passwords[i]).GetFirstElement());

    }

    @Test
    public void MissMatchUserPWTest()
    {
        for (int i = 1; i < ug.getNumOfUser(); i++)
            Assert.assertFalse(Login(validUsernames[i], passwords[i-1]).GetFirstElement());
    }

    @Test
    public void DoubleLoginTest() //fixed from version 1
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
        {
            Assert.assertTrue(Login(validUsernames[i], passwords[i]).GetFirstElement());
            Assert.assertFalse(Login(validUsernames[i], passwords[i]).GetFirstElement());

        }
    }

    @Test
    public void UserDoesNotExist()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            Assert.assertTrue(Login(notRegisteredUsers[i], passwords[i]).GetFirstElement());

    }



}
