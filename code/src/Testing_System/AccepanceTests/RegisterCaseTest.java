package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

public class RegisterCaseTest extends Tester {

    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames;
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;

    @Before
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
            Assert.assertTrue(Register(validUserNames[i],PWs[i]).GetFirstElement());
    }

    @Test
    public void UserExistTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            Assert.assertTrue(Register(validUserNames[i],PWs[i]).GetFirstElement());
        for(int i = 0; i<ug.getNumOfUser(); i++)
            Assert.assertFalse(Register(validUserNames[i],PWs[i]).GetFirstElement());
    }

    @Test
    public void BadUserNameTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            Assert.assertFalse(Register(badUserName[i],PWs[i]).GetFirstElement());
    }

    @Test
    public void BadPWTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            Assert.assertFalse(Register(validUserNames[i],badPWs[i] ).GetFirstElement());
    }

    @Test
    public void SadUsersTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            Assert.assertFalse(Register(sadUserNames[i],PWs[i]).GetFirstElement());
    }


}
