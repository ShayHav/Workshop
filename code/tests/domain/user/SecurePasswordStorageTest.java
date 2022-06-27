package domain.user;

import Testing_System.UserGenerator;
import domain.Exceptions.InvalidSequenceOperationsExc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurePasswordStorageTest {
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();
    private SecurePasswordStorage securePasswordStorage;

    @BeforeEach
    void setUp(){
        securePasswordStorage = SecurePasswordStorage.getSecurePasswordStorage_singleton();
        for(int i = 0;i<userName.length;i++){
            securePasswordStorage.inRole(userName[i],userPass[i]);
        }
    }
    @Test
    void passwordCheck() {
        for(int i=0;i<userName.length;i++){
            assertTrue(securePasswordStorage.passwordCheck(userName[i],userPass[i]));
            assertFalse(securePasswordStorage.passwordCheck(userName[i],badPass[i]));
        }
    }

    @Test
    void inRole() {
        for(int i=0;i<userName.length;i++){
            assertTrue(securePasswordStorage.isUserRole(userName[i]));
        }
    }

    @Test
    void passwordCheckThread() throws InterruptedException {
        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA");
                securePasswordStorage.passwordCheck(userName[0],userPass[0]);

            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                securePasswordStorage.passwordCheck(userName[1],userPass[1]);
            }
        });
        t0.start();
        t1.start();
    }

}