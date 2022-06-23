package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.user.filter.SearchUserFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DismissalUserBySystemManagerCaseTest extends Tester {
    private UserGenerator ug = new UserGenerator();
    private String[] validUserNames = ug.GetValidUsers();
    private String[] badUserName;
    private String[] sadUserNames;
    private String[] PWs;
    private String[] badPWs;

    @BeforeAll
    public void SetUp() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        DeleteUserTest(validUserNames);
        validUserNames = ug.GetValidUsers();
        badUserName = ug.GetBadUsers();
        sadUserNames = ug.GetSadUsers();
        PWs = ug.GetPW();
        badPWs = ug.GetBadPW();
        ug.InitTest();
        for(int i = 0; i<ug.getNumOfUser(); i++) {
            String g = !EnterMarket().isErrorOccurred() ? EnterMarket().getValue().getUserName() : "";
            assertTrue(!Register(g,validUserNames[i], PWs[i]).isErrorOccurred());
        }
    }

    @AfterEach
    public void CleanUp()
    {
        DeleteUserTest(validUserNames);
    }

    @Test
    public void DismissalAllUsers()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!DismissalUserBySystemManager("Admin",validUserNames[i]).isErrorOccurred());
        SearchUserFilter f = new SearchUserFilter();
        for (int i = 0; i<ug.getNumOfUser(); i++){
            f.setName(validUserNames[i]);
            assertFalse(!(RequestUserInfo(f,"Admin").getValue().size()==0));
        }
    }

    @Test
    public void NotExistUserTest()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertFalse(!DismissalUserBySystemManager("admin",badUserName[i]).isErrorOccurred());

        assertFalse(!DismissalUserBySystemManager("admin",null).isErrorOccurred());
    }

    @Test
    public void NoPermissionDismissal()
    {
        for(int i = 0; i<ug.getNumOfUser(); i++)
            for(int j = 0; j<ug.getNumOfUser(); j++)
                assertFalse(!DismissalUserBySystemManager(validUserNames[j],validUserNames[i]).isErrorOccurred());
    }
}
