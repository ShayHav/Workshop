package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.user.User;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginCaseTest extends Tester {

    UserGenerator ug = new UserGenerator();
    String[] validUsernames = ug.GetValidUsers();
    String[] passwords = ug.GetPW();
    String[] notRegisteredUsers = ug.GetNotRegisteredUsers();

    @BeforeAll
    public void SetUp()
    {
        ug.InitTest();
        for(int i = 0; i< ug.getNumOfUser(); i++)
            Register(validUsernames[i], passwords[i]);
    }

    @AfterAll
    public void CleanUp()
    {
        DeleteUserTest(validUsernames);
        ug.DeleteAdmin();
    }


    @Test
    public void GoodLogin()
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
            assertTrue(!Login(validUsernames[i], passwords[i],null).isErrorOccurred());

    }

    @Test
    public void MissMatchUserPWTest()
    {
        for (int i = 1; i < ug.getNumOfUser(); i++)
            assertFalse(!Login(validUsernames[i], passwords[i-1],null).isErrorOccurred());
    }

    @Test
    public void DoubleLoginTest() //fixed from version 1
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
        {
           assertTrue(!Login(validUsernames[i], passwords[i],null).isErrorOccurred());
           assertFalse(!Login(validUsernames[i], passwords[i],null).isErrorOccurred());

        }
    }

    @Test
    public void UserDoesNotExist()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Login(notRegisteredUsers[i], passwords[i],null).isErrorOccurred());

    }



}
