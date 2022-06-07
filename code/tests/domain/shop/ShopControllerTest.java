package domain.shop;


import domain.Exceptions.*;
import domain.UserGeneratorTemp;
import domain.user.Role;
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
    private UserGeneratorTemp userGenerator = new UserGeneratorTemp();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.getPw();
    private String[] badPass = userGenerator.getBadPW();
    private String[] nitayName = userGenerator.getNitayNames();
    private String[] nitayPass = userGenerator.getNitayPassword();

    @BeforeEach
    void setUp() {
        for(int i=0;i<userName.length;i++) {
            try {
                userController.enterMarket();
                String guestName = String.format("-Guest%d",i+1);
                userController.register(userName[i], userPass[i]);
                userController.leaveMarket(guestName);
            }
            catch (InvalidSequenceOperationsExc | IncorrectIdentification invalidSequenceOperationsExc){
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
                        userController.enterMarket();
                        String guestName = String.format("-Guest%d",finalI+1);
                        userController.register(nitayName[finalI], nitayPass[finalI]);
                        User u =userController.login(guestName,nitayName[finalI], nitayPass[finalI]);
                        shopController.createShop("",nitayName[finalI],null,null,u);
                        userController.leaveMarket(guestName);
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
    void closeShop() throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, InterruptedException, ShopNotFoundException {
        String guestName = String.format("-Guest%d",0);
        shopController.createShop("","nitay",null,null,userController.getUser(userName[0]));
        userController.login(guestName,userName[0],userPass[0]);
        shopController.AppointNewShopManager(1,userName[1],userName[0]);
        assertTrue(userController.getUser(userName[1]).getRoleList().get(1).contains(Role.ShopManager));
        shopController.AddShopMangerPermissions(1,ShopManagersPermissions.CloseShop,userName[1],userName[0]);
        String guestName1 = String.format("-Guest%d",1);
        userController.login(guestName1,userName[1],userPass[1]);
        //shopController.closeShop(1,userName[1]);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    shopController.closeShop(1,userName[1]);
                } catch (InvalidSequenceOperationsExc | IncorrectIdentification | BlankDataExc e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    shopController.closeShop(1,userName[0]);
                } catch (InvalidSequenceOperationsExc | IncorrectIdentification | BlankDataExc e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        assertTrue(shopController.isShopClose(1));
        userController.logout(userName[1]);
        userController.logout(userName[0]);
    }

    @Test
    void openShop() {
    }
}