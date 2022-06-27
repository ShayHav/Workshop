package domain.user;

import Testing_System.UserGenerator;
import domain.Exceptions.*;
import domain.shop.Shop;
import domain.shop.ShopController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController = UserController.getInstance();;
    private ShopController shopController = ShopController.getInstance();
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();
    private String[] nitayName = userGenerator.getNitayNames();
    private String[] nitayPass = userGenerator.getNitayPassword();


    @BeforeEach
    void setUp() {
        for(int i=0;i<userName.length;i++) {
            try {
                userController.enterMarket();
                userController.register(userName[i], userPass[i]);
                userController.leaveMarket(String.format("-Guest%d",i));
            }
            catch (InvalidSequenceOperationsExc | IncorrectIdentification invalidSequenceOperationsExc){
                System.out.println(invalidSequenceOperationsExc.getMessage());
            }
        }
    }
    @AfterEach
    void init(){
        for(int i=0;i<userName.length;i++) {
            try {
                userController.logout(userName[i]);
            }
            catch (InvalidSequenceOperationsExc | IncorrectIdentification invalidSequenceOperationsExc){
                System.out.println(invalidSequenceOperationsExc.getMessage());
            }
        }
    }

    @Test
    void logIn() {
        for(int i = 1; i < userName.length; i++){
            try {
                userController.logout(userName[i]);
                assertTrue(userController.login(String.format("-Guest%d",i),userName[i], userPass[i]) != null);
            }
             catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
                invalidSequenceOperationsExc.printStackTrace();
            } catch (IncorrectIdentification incorrectIdentification) {
                incorrectIdentification.printStackTrace();
            } catch (InvalidAuthorizationException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < userName.length; i++){
            int finalI = i;
            try {
                userController.login(String.format("-Guest%d",i),userName[finalI], badPass[finalI]);
                assertTrue(false);
            }
            catch (InvalidAuthorizationException invalidAuthorizationException){
                assertTrue(true);
            } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
                invalidSequenceOperationsExc.printStackTrace();
            } catch (IncorrectIdentification incorrectIdentification) {
                incorrectIdentification.printStackTrace();
            }
        }
    }

    @Test
    void logout() throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException {
        for(int i = 0; i < userName.length; i++){
            userController.login(String.format("-Guest%d",i),userName[i], userPass[i]);
            userController.logout(userName[i]);
            assertFalse(userController.getUser(userName[i]).isLoggedIn());
        }
    }

    @Test
    void register() throws InvalidSequenceOperationsExc, IncorrectIdentification {
        //userController.deleteUserTest(userName);
        for (int i = 0; i < userName.length; i++) {
            assertTrue(userController.getUser(userName[i]).getUserName().equals(userName[i]));
        }
    }

    @Test
    void getUser() throws IncorrectIdentification {
        for (int i = 0; i < userName.length; i++){
            assertTrue(userController.getUser(userName[i]).getUserName().equals(userName[i]));
        }
    }

    @Test
    void createSystemManager(){
        try {
            userController.createSystemManager(admin, adminPass);
            assertTrue(userController.getUser(admin).isSystemManager());
        }
        catch (InvalidSequenceOperationsExc | IncorrectIdentification exception){
            assertTrue(false);
        }
    }
    @Test
    void deleteUserName() {
        try {
            userController.deleteUserName(userName[0]);
            assertTrue( userController.getUser(userName[0]) == null);
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc | BlankDataExc | ShopNotFoundException exception) {
            fail();
        }
    }

    //TODO: Bar scenario
    @Test
    void DismissalOwner() throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, ShopNotFoundException, BidNotFoundException, CriticalInvariantException {
        userController.login(String.format("-Guest%d",0),userName[0],userPass[0]);
        User u = userController.getUser(userName[0]);
        Shop s = shopController.createShop("","",null,null, u);
        shopController.AppointNewShopOwner(s.getShopID(),userName[1], userName[0]);
        User u1 = userController.getUser(userName[1]);
        assertTrue(u1.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        userController.login(String.format("-Guest%d",1),userName[1],userPass[1]);
        shopController.AppointNewShopOwner(s.getShopID(),userName[2], userName[1]);
        User u2 = userController.getUser(userName[2]);
        assertTrue(u2.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        userController.DismissalOwner(userName[0], userName[1],s.getShopID());
        assertFalse(u1.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
        assertFalse(u2.getRoleList().get(s.getShopID()).contains(Role.ShopOwner));
    }

    @Test
    void ThreadLogIn() throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, InterruptedException {
        userController.logout(userName[0]);
        userController.logout(userName[1]);
        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.login(String.format("-Guest%d",0),userName[0], userPass[0]);
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                } catch (IncorrectIdentification e) {
                    e.printStackTrace();
                } catch (InvalidAuthorizationException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.login(String.format("-Guest%d",1),userName[1], userPass[1]);
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                } catch (IncorrectIdentification e) {
                    e.printStackTrace();
                } catch (InvalidAuthorizationException e) {
                    e.printStackTrace();
                }
            }
        });
        t0.start();
        t1.start();

        t0.join();
        t1.join();

        assertTrue(userController.isLogin(userName[0]));
        assertTrue(userController.isLogin(userName[1]));
    }


    @Test
    void ThreadRegistered() throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, InterruptedException {
        Thread t0= new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.register("useruser","userpass");
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.register("useruser1","userpass1");
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                }
            }
        });
        t0.start();
        t1.start();

        t1.join();

        assertTrue(userController.getUser("useruser").getUserName().equals("useruser"));
        assertTrue(userController.getUser("useruser1").getUserName().equals("useruser1"));
    }

    @Test
    void createSystemManagerThread() throws InterruptedException, IncorrectIdentification {
        Thread t0= new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.createSystemManager(admin, adminPass);
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    userController.createSystemManager(admin+"1", adminPass+"1");
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                }
            }
        });
        t0.start();
        t1.start();

        t1.join();

        assertTrue(userController.getUser(admin).isSystemManager());
        assertTrue(userController.getUser(admin+"1").isSystemManager());
    }


    @Test
    void createSystemManagerThreads() throws IncorrectIdentification {
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        userController.createSystemManager(nitayName[finalI], nitayPass[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    }
                }
            };
            pool.execute(r);
        }


        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        for(int i =0;i<nitayName.length;i++){
            //System.out.println(String.format("User: %s  is exist? %s",nitayName[i],userController.userExist(nitayName[i])));
            assertTrue(userController.getUser(nitayName[i]).isSystemManager());
        }
    }
    @Test
    void logInThreads() throws IncorrectIdentification {
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        int count[] = {userName.length};
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        userController.enterMarket();
                        count[0]++;
                        String guestName = String.format("-Guest%d",finalI+1);
                        userController.register(nitayName[finalI], nitayPass[finalI]);
                        userController.login(guestName,nitayName[finalI], nitayPass[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    }
                }
            };
            pool.execute(r);
        }


        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        for(int i =0;i<nitayName.length;i++){
            assertTrue(userController.getUser(nitayName[i]).isLoggedIn());
        }
    }
}
