package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames = ug.GetValidUsers();
    private String[] PWs = ug.GetPW();


    @BeforeAll
    public void SetUp()
    {
        for(int i = 0; i < ug.getNumOfUser()/2; i++)
            Register(validUserNames[i], PWs[i]);

    }

    @BeforeEach
    public void LogUsers()
    {
        for(int i = 0; i < ug.getNumOfUser()/2; i++)
            Login(validUserNames[i], PWs[i]);
    }



    @AfterAll
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }

    @Test
    public void GoodLogoutTest()
    {
        for(int i = 0; i < ug.getNumOfUser()/2; i++)
            assertTrue(!Logout(validUserNames[i]).isErrorOccurred());
    }

    @Test
    public void NotLoggedUsersTest()
    {
        for(int i = 0; i < ug.getNumOfUser()/2; i++)
            assertTrue(!Logout(validUserNames[i]).isErrorOccurred());
        for(int i = 0; i < ug.getNumOfUser()/2; i++)
            assertFalse(!Logout(validUserNames[i]).isErrorOccurred());
    }

    @Test
    public void NotRegisteredUsersTest()
    {
        for(int i = (ug.getNumOfUser()/2) + 1; i<ug.getNumOfUser(); i++)
            assertFalse(!Logout(validUserNames[i]).isErrorOccurred());
    }




}
