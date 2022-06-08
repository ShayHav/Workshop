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
public class RegisterCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames;
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;
    private String guest;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        validUserNames = ug.GetValidUsers();
        badUserName = ug.GetBadUsers();
        sadUserNames = ug.GetSadUsers();
//        ug.InitTest();
        PWs = ug.GetPW();
        badPWs = ug.GetBadPW();
        guest = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";

    }

    @AfterEach
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }

    @AfterAll
    public void CleanAll()
    {
        ug.DeleteAdmin();
    }
    @Test
    public void GoodRegisterTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!Register(guest,validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void UserExistTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!Register(guest,validUserNames[i],PWs[i]).isErrorOccurred());
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(guest, validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadUserNameTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(guest, badUserName[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadPWTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(guest, validUserNames[i],badPWs[i]).isErrorOccurred());
    }

    @Test
    public void SadUsersTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(guest, sadUserNames[i],PWs[i]).isErrorOccurred());
    }


}
