package Testing_System.AccepanceTests;
import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUsernames = ug.GetValidUsers();
    private String[] passwords = ug.GetPW();
    private String[] notRegisteredUsers = ug.GetNotRegisteredUsers();
    private String[] guestArr;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug.InitTest();
    }

    @BeforeEach
    public void SetTest()
    {
        guestArr = new String[ug.getNumOfUser()];
        for(int i = 0; i< ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            guestArr[i] = g;
            Register(g, validUsernames[i], passwords[i]);
        }
    }

    @AfterEach
    public void CleanUp()
    {
        DeleteUserTest(validUsernames);
    }

    @AfterAll
    public void CleanAll()
    {
        ug.DeleteAdmin();
    }


    @Test
    public void GoodLogin()
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
            assertTrue(!Login(guestArr[i],validUsernames[i], passwords[i]).isErrorOccurred());

    }

    @Test
    public void MissMatchUserPWTest()
    {
        for (int i = 1; i < ug.getNumOfUser(); i++)
            assertFalse(!Login(guestArr[i],validUsernames[i], passwords[i-1]).isErrorOccurred());
    }

    @Test
    public void DoubleLoginTest() //fixed from version 1
    {
        for (int i = 0; i < ug.getNumOfUser(); i++)
        {
            assertTrue(!Login(guestArr[i],validUsernames[i], passwords[i]).isErrorOccurred());
            assertFalse(!Login(guestArr[i],validUsernames[i], passwords[i]).isErrorOccurred());

        }
    }

    @Test
    public void UserDoesNotExist()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Login(guestArr[i],notRegisteredUsers[i], passwords[i]).isErrorOccurred());

    }



}
