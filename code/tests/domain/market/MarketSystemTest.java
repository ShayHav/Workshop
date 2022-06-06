package domain.market;

import Testing_System.UserGenerator;
import domain.Exceptions.*;
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
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String admin = userGenerator.GetAdminID();
    private String adminPass = userGenerator.GetAdminPW();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();
    private String[] nitayName = userGenerator.getNitayNames();
    private String[] nitayPass = userGenerator.getNitayPassword();
    private PaymentService ps1 = mock(PaymentService.class);
    private SupplyService ss2 = mock(SupplyService.class);

    @Test
    void registerLogInCreateAppointOwner() throws ShopNotFoundException, InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification {
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.register(userName[0],userPass[0] );
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
                        marketSystem.register(nitayName[finalI], nitayName[finalI]);
                        marketSystem.logIn(nitayName[finalI], nitayName[finalI],null);
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
        for(int i =1;i<=nitayName.length;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isOwner(userName[0]));
        }
    }

    @Test
    void registerLogInCreateAppointManager() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.register(userName[0],userPass[0] );
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<nitayName.length;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.register(nitayName[finalI], nitayName[finalI]);
                        marketSystem.logIn(nitayName[finalI], nitayName[finalI],null);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[0],nitayName[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
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
        for(int i =1;i<=nitayName.length;i++){
            Shop s = marketSystem.getShop(i);
            assertTrue(s.isManager(userName[0]));
        }
    }

    @Test
    void registerLogInCreateAppointManagerAddPermissionCloseShop() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc{
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.register(userName[0],userPass[0] );
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.register(nitayName[finalI], nitayPass[finalI]);
                        marketSystem.register(userName[finalI], userPass[finalI]);
                        marketSystem.logIn(nitayName[finalI], nitayPass[finalI],null);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.logIn(userName[finalI], userPass[finalI],null);
                        marketSystem.CloseShop(s.getShopID(),userName[finalI]);
                    } catch (InvalidSequenceOperationsExc e) {
                        e.printStackTrace();
                    } catch (IncorrectIdentification incorrectIdentification) {
                        incorrectIdentification.printStackTrace();
                    } catch (InvalidAuthorizationException e) {
                        e.printStackTrace();
                    } catch (BlankDataExc blankDataExc) {
                        blankDataExc.printStackTrace();
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
            assertFalse(s.isOpen());
        }
    }

    @Test
    void registerLogInCreateAppointManagerAddPermissionCloseShopOpenAddProduct() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        marketSystem.start(ps1,ss2,"admin","admin");
        marketSystem.register(userName[0],userPass[0] );
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        marketSystem.register(nitayName[finalI], nitayPass[finalI]);
                        marketSystem.register(userName[finalI], userPass[finalI]);
                        marketSystem.logIn(nitayName[finalI], nitayPass[finalI],null);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.logIn(userName[finalI], userPass[finalI],null);
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
    void checkOut() throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
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
        marketSystem.register(userName[0],userPass[0] );
        DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
        double basePrice = 50;
        when(discountPolicy.calcPricePerProduct(1, basePrice, 1)).thenReturn(basePrice);
        PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
        ExecutorService pool = Executors.newFixedThreadPool(nitayName.length);
        for(int i =0;i<4;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        when(purchasePolicy.checkIfProductRulesAreMet(userName[finalI], 001, 50.0, 1)).thenReturn(true);
                        marketSystem.register(nitayName[finalI], nitayPass[finalI]);
                        marketSystem.register(userName[finalI], userPass[finalI]);
                        marketSystem.logIn(nitayName[finalI], nitayPass[finalI],null);
                        Shop s = marketSystem.createShop("sports",nitayName[finalI] + "'s", discountPolicy, purchasePolicy, nitayName[finalI]);
                        marketSystem.AppointNewShopManager(s.getShopID(),userName[finalI],nitayName[finalI]);
                        marketSystem.AddShopMangerPermissions(s.getShopID(),shopManagersPermissionsList,userName[finalI],nitayName[finalI]);
                        marketSystem.CloseShop(s.getShopID(),userName[finalI]);
                        marketSystem.OpenShop(s.getShopID(),userName[finalI]);
                        marketSystem.AddProductToShopInventory(001,"basketball","sports","sports",50.0,50,nitayName[finalI],s.getShopID());
                        marketSystem.logout(nitayName[finalI]);
                        marketSystem.logIn(userName[finalI], userPass[finalI],null);
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