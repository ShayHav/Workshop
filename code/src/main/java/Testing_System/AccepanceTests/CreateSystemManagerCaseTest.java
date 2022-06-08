package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.ResponseT;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateSystemManagerCaseTest extends Tester {
    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames;
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        ug.InitTest();
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
    public void GoodCreateSystemManagerTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!CreateSystemManager("admin",validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void UserExistTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!CreateSystemManager("admin",validUserNames[i],PWs[i]).isErrorOccurred());
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertFalse(!CreateSystemManager("admin",validUserNames[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadUserNameTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!CreateSystemManager("admin",badUserName[i],PWs[i]).isErrorOccurred());
    }

    @Test
    public void BadPWTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!CreateSystemManager("admin",validUserNames[i],badPWs[i]).isErrorOccurred());
    }

    @Test
    public void SadUsersTest()
    {
        for(int i =0; i<ug.getNumOfUser(); i++)
            assertFalse(!CreateSystemManager("admin",sadUserNames[i],PWs[i]).isErrorOccurred());
    }



}
