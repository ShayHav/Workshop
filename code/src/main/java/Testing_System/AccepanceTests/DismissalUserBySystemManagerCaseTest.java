package Testing_System.AccepanceTests;

import Testing_System.Tester;
import Testing_System.UserGenerator;
import domain.user.filter.SearchUserFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
public class DismissalUserBySystemManagerCaseTest extends Tester {
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
        for(int i = 0; i<ug.getNumOfUser(); i++)
            assertTrue(!Register(validUserNames[i],PWs[i]).isErrorOccurred());
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
            assertTrue(!DismissalUserBySystemManager("admin",validUserNames[i]).isErrorOccurred());
        SearchUserFilter f = new SearchUserFilter();
        for (int i = 0; i<ug.getNumOfUser(); i++){
            f.setName(validUserNames[i]);
            assertFalse(!(RequestUserInfo(f,"admin").getValue().size()==0));
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
