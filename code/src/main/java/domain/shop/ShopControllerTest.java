package domain.shop;

import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Exceptions.ShopNotFoundException;
import domain.user.User;
import domain.user.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ShopControllerTest {
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
                userController.register(userName[i], userPass[i]);
            }
            catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
                System.out.println(invalidSequenceOperationsExc.getMessage());
            }
        }
    }

    @Test
    void createShopThreads() throws ShopNotFoundException {
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        userController.register(nitayName[finalI], nitayPass[finalI]);
                        User u =userController.logIn(nitayName[finalI], nitayPass[finalI]);
                        shopController.createShop("",nitayName[finalI],null,null,u);
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
            assertTrue(shopController.shopExist(nitayName[i])!=null);
        }
    }

    @Test
    void getInfoOfShops() {
    }

    @Test
    void closeShop() {

    }

    @Test
    void openShop() {
    }
}