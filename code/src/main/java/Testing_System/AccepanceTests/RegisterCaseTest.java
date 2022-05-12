package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;

public class RegisterCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames;
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;

    @BeforeAll
    public void SetUp()
    {
        validUserNames = ug.GetValidUsers();
        badUserName = ug.GetBadUsers();
        sadUserNames = ug.GetSadUsers();
        PWs = ug.GetPW();
        badPWs = ug.GetBadPW();
    }

    @AfterEach
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }

    @Test
    public void GoodRegisterTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!Register(validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void UserExistTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!Register(validUserNames[i],PWs[i]).isErrorOccurred());
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadUserNameTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(badUserName[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadPWTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(validUserNames[i],badPWs[i] ).isErrorOccurred());
    }

    @Test
    public void SadUsersTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!Register(sadUserNames[i],PWs[i]).isErrorOccurred());
    }


}
