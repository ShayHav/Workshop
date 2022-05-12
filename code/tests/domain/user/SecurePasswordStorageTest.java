package domain.user;

import Testing_System.UserGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurePasswordStorageTest {
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();
    private SecurePasswordStorage securePasswordStorage;
    

    @Test
    void passwordCheck() {
    }

    @Test
    void inRole() {
    }
}