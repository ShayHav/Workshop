package domain.market;

import domain.Exceptions.*;
import domain.UserGeneratorTemp;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import domain.shop.discount.DiscountPolicy;
import jdk.dynalink.linker.LinkerServices;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketSystemTest {
    private MarketSystem marketSystem = MarketSystem.getInstance();
    private UserGeneratorTemp userGenerator = new UserGeneratorTemp();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.getPw();
    private String[] badPass = userGenerator.getBadPW();
    private String[] nitayName = userGenerator.getNitayNames();
    private String[] nitayPass = userGenerator.getNitayPassword();
    private PaymentService ps1 = mock(PaymentService.class);
    private SupplyService ss2 = mock(SupplyService.class);

    @Test
    void registerLogInCreateAppointOwner() throws ShopNotFoundException, InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification, InvalidAuthorizationException {
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.EnterMarket();
        marketSystem.register("-Guest0",userName[0],userPass[0]);
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        when(purchasePolicy.checkIfProductRulesAreMet(anyString(), anyInt(), anyDouble(), anyInt())).thenReturn(true);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.EnterMarket();
                        String guestName = String.format("-Guest%d",finalI+1);
                        marketSystem.register(guestName,nitayName[finalI], nitayName[finalI]);
                        marketSystem.login(guestName,nitayName[finalI], nitayName[finalI]);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopOwner(s.getShopID(),userName[0],nitayName[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
                    }catch (ShopNotFoundException shopNotFoundException) {
                        shopNotFoundException.printStackTrace();
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
        for(int i =1;i<=6;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isOwner(userName[0]));
        }
    }

    @Test
    void registerLogInCreateAppointManager() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.EnterMarket();
        marketSystem.register("-Guest0",userName[0],userPass[0]);
        marketSystem.LeaveMarket("-Guest0");
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.EnterMarket();
                        String guestName = String.format("-Guest%d",finalI+1);
                        marketSystem.register(guestName,nitayName[finalI], nitayName[finalI]);
                        marketSystem.login(guestName,nitayName[finalI], nitayName[finalI]);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[0],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.LeaveMarket(nitayName[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
                    } catch (ShopNotFoundException shopNotFoundException) {
                        shopNotFoundException.printStackTrace();
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
        for(int i =1;i<=8;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isManager(userName[0]));
        }
    }

    @Test
    void registerLogInCreateAppointManagerAddPermissionCloseShop() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.EnterMarket();
        marketSystem.register("-Guest0",userName[0],userPass[0] );
        marketSystem.LeaveMarket("-Guest0");
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        final int[] count = {0};
        for(int i =1;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName1 = String.format("-Guest%d",count[0]);
                        marketSystem.register(guestName1,nitayName[finalI], nitayPass[finalI]);
                        marketSystem.LeaveMarket(guestName1);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName2 = String.format("-Guest%d",count[0]);
                        System.out.println(guestName1);
                        System.out.println(guestName2);
                        marketSystem.register(guestName2,userName[finalI], userPass[finalI]);
                        marketSystem.LeaveMarket(guestName2);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName3 = String.format("-Guest%d",count[0]);
                        marketSystem.login(guestName3,nitayName[finalI], nitayPass[finalI]);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.LeaveMarket(nitayName[finalI]);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName4 = String.format("-Guest%d",count[0]);
                        marketSystem.login(guestName4,userName[finalI], userPass[finalI]);
                        marketSystem.CloseShop(s.getShopID(),userName[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
                    } catch (ShopNotFoundException shopNotFoundException) {
                        shopNotFoundException.printStackTrace();
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
        for(int i =1;i<3;i++){
            Shop s = marketSystem.getShop(i);
            assertFalse(s.isOpen());
        }
    }

    @Test
    void registerLogInCreateAppointManagerAddPermissionCloseShopOpenAddProduct() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.EnterMarket();
        marketSystem.register("-Guest0",userName[0],userPass[0] );
        marketSystem.LeaveMarket("-Guest0");
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        final int[] count = {0};
        for(int i =0;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName1 = String.format("-Guest%d", count[0]);
                        marketSystem.register(guestName1,nitayName[finalI], nitayPass[finalI]);
                        marketSystem.LeaveMarket(guestName1);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName2 = String.format("-Guest%d",count[0]);
                        marketSystem.register(guestName2,userName[finalI], userPass[finalI]);
                        marketSystem.login(guestName2,nitayName[finalI], nitayPass[finalI]);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.LeaveMarket(nitayName[finalI]);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName3 = String.format("-Guest%d",count[0]);
                        marketSystem.login(guestName3,userName[finalI], userPass[finalI]);
                        marketSystem.CloseShop(s.getShopID(),userName[finalI]);
                        marketSystem.OpenShop(s.getShopID(),userName[finalI]);
                        marketSystem.AddProductToShopInventory(001,"basketball","sports","sports",50.0,50,userName[finalI],s.getShopID());
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
                    } catch (ShopNotFoundException shopNotFoundException) {
                        shopNotFoundException.printStackTrace();
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
        for(int i =1;i<4;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isOpen());
        }
    }

    @Test
    void checkOut() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, InvalidAuthorizationException {
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        when(ps1.processPayment(anyString(),anyString(),anyString(),anyString(),anyDouble())).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.supply(anyVararg(),anyVararg(),anyVararg())).thenReturn(true);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.EnterMarket();
        marketSystem.register("-Guest0",userName[0],userPass[0] );
        marketSystem.LeaveMarket("-Guest0");
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        double basePrice = 50;
        when(discountPolicy.calcPricePerProduct(1, basePrice, 1)).thenReturn(basePrice);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        final int[] count = {0};
        for(int i =0;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        when(purchasePolicy.checkIfProductRulesAreMet(userName[finalI], 001, 50.0, 1)).thenReturn(true);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName1 = String.format("-Guest%d", count[0]);
                        marketSystem.register(guestName1,nitayName[finalI], nitayPass[finalI]);
                        marketSystem.LeaveMarket(guestName1);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName2 = String.format("-Guest%d", count[0]);
                        marketSystem.register(guestName2,userName[finalI], userPass[finalI]);
                        marketSystem.login(guestName2,nitayName[finalI], nitayPass[finalI]);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.LeaveMarket(nitayName[finalI]);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName4 = String.format("-Guest%d", count[0]);
                        marketSystem.login(guestName4,userName[finalI], userPass[finalI]);
                        marketSystem.CloseShop(s.getShopID(),userName[finalI]);
                        marketSystem.OpenShop(s.getShopID(),userName[finalI]);
                        marketSystem.AddProductToShopInventory(001,"basketball","sports","sports",50.0,50,nitayName[finalI],s.getShopID());
                        marketSystem.logout(userName[finalI]);
                        marketSystem.LeaveMarket(nitayName[finalI]);
                        marketSystem.EnterMarket();
                        count[0]++;
                        String guestName3 = String.format("-Guest%d", count[0]);
                        marketSystem.login(guestName3,userName[finalI], userPass[finalI]);
                        marketSystem.AddProductToCart(userName[finalI],s.getShopID(),001,1);
                        marketSystem.Checkout(userName[finalI],userName[finalI],"Beer shava","+972528403121","0123456789","07/26");
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
                    } catch (ShopNotFoundException shopNotFoundException) {
                        shopNotFoundException.printStackTrace();
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
        for(int i =1;i<4;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isOpen());
        }
    }

}