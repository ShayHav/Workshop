package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogoutCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames = ug.GetValidUsers();
    private String[] PWs = ug.GetPW();
    private String[] guestArr;


    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        DeleteUserTest(validUserNames);
        ug.InitTest();

    }

    @BeforeEach
    public void LogUsers()
    {
        guestArr = new String[ug.getNumOfUser()];
        for(int i = 0; i < ug.getNumOfUser()/2; i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            guestArr[i] = g;
            Register(g, validUserNames[i], PWs[i]);
            Login(g, validUserNames[i], PWs[i]);
        }
    }



    @AfterEach
    public void CleanTest()
    {
        DeleteUserTest(validUserNames);
    }

    @AfterAll
    public void CleanUp()
    {
        ug.DeleteAdmin();
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
