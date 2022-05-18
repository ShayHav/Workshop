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
public class RequestUserInfoCaseTest extends Tester {
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
    public void NoFilterRequest(){
        assertTrue(RequestUserInfo(null,"admin").getValue().size()== validUserNames.length);
    }

    @Test
    public void SpecificUserFiler(){
        SearchUserFilter f = new SearchUserFilter();
        for (int i = 0; i<ug.getNumOfUser(); i++) {
            f.setName(validUserNames[i]);
            assertTrue(RequestUserInfo(f, "admin").getValue().get(0).equals(validUserNames[i]));
        }
    }

    @Test
    public void NoPermissionRequest(){
        for (int i = 0; i<ug.getNumOfUser(); i++) {
            assertFalse(!RequestUserInfo(null, validUserNames[i]).isErrorOccurred());
        }
    }
    @Test
    public void NoExistUser(){
        SearchUserFilter f = new SearchUserFilter();
        for (int i = 0; i<ug.getNumOfUser(); i++) {
            f.setName(badUserName[i]);
            assertTrue(RequestUserInfo(f, "admin").getValue().size()==0);
        }
    }
    @Test
    public void MemberUser(){
        SearchUserFilter f = new SearchUserFilter("isMember");
        assertTrue(RequestUserInfo(f, "admin").getValue().size()==validUserNames.length);
    }
}