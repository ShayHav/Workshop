package domain.shop;

import domain.Exceptions.*;
import domain.Responses.ResponseT;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Basket;
import domain.shop.discount.DiscountPolicy;
import domain.user.TransactionInfo;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShopTest {
    DiscountPolicy discountPolicy;
    PurchasePolicy purchasePolicy;
    Shop shop;
    int appleID;
    int orangeID;
    Basket mockBasket;

    @BeforeEach
    public void setup() {
        setDavidos();
        setDavidosShop();
    }

    private User setDavidos() {
        User davidos = mock(User.class);
        when(davidos.getUserName()).thenReturn("Davidos");
        return davidos;
    }

    private void setDavidosShop() {
        User davidos = setDavidos();
        shop = new Shop("David's", "good shop",discountPolicy, purchasePolicy, davidos, 1);
        discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        Product p1 = mock(ProductImp.class);
        Product p2 = mock(ProductImp.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        try {
            orangeID = shop.addListing(1,"orange", "red orange", "fruits", 12.0, 7, "Davidos").getId();
            appleID = shop.addListing(2, "apple", "red apple", "fruits", 5.0, 3, "Davidos").getId();
        } catch (InvalidAuthorizationException InvAuthExc) {
            fail("founder can add product");
            return;
        } catch (InvalidProductInfoException InvProdInfoExc) {
            fail("product info is legal");
            return;
        }
        mockBasket = mock(Basket.class);
        when(mockBasket.calculateTotal()).thenReturn(50.0);
        when(discountPolicy.calcPricePerProductForCartTotal(Mockito.anyMap())).thenReturn(mockBasket);
        when(purchasePolicy.checkCart_RulesAreMet(Mockito.anyMap())).thenReturn(true);
        shop.setDiscountPolicy(discountPolicy);
        shop.setPurchasePolicy(purchasePolicy);
    }

    @Test
    void addAndGetProduct() {
        Product apple;
        try {
            apple = shop.addListing(2,"apple", "red apple", "fruits", 5.0, 3, "Davidos");
        } catch (InvalidAuthorizationException InvAuthExc) {
            fail("founder can add product");
            return;
        } catch (InvalidProductInfoException InvProdInfoExc) {
            fail("product info is legal");
            return;
        }
        appleID = apple.getId();
        assertTrue(shop.isProductAvailable(apple.getId()));
        try {
            assertEquals(shop.getProduct(apple.getId()).getId(), apple.getId());
            assertEquals(0, shop.getProduct(appleID).getName().compareTo("apple"), "product p1 should have been returned");
            assertEquals(0, shop.getProduct(appleID).getDescription().compareTo("red apple"));
        }catch (ProductNotFoundException productNotFoundException){
            fail("product exists, but failed to find it.");
            return;
        }

        assertThrows(ProductNotFoundException.class, () -> shop.getProduct(appleID + 100), "this item should not exist");
        Product orange;
        try {
            orange = shop.addListing(1, "orange", "red orange", "fruits", 12.0, 7, "Davidos");
        } catch (InvalidAuthorizationException InvAuthExc) {
            fail("founder can add product");
            return;
        } catch (InvalidProductInfoException InvProdInfoExc) {
            fail("product info is legal");
            return;
        }
        orangeID = orange.getId();
        assertTrue(shop.isProductAvailable(orangeID));
        try{
            assertEquals(shop.getProduct(orangeID).getId(), orangeID);
            assertEquals(0, shop.getProduct(orangeID).getName().compareTo("orange"), "product p1 should have been returned");
            assertEquals(0, shop.getProduct(orangeID).getDescription().compareTo("red orange"), "incorrect description of orange");
        }catch (ProductNotFoundException productNotFoundException){
            fail("product exists, but failed to find it.");
        }
    }



    @Test
    void calculateTotalAmountOfOrder() {
        Map<Integer, Integer> product_Quantity = new HashMap<>();
        product_Quantity.put(appleID, 3);
        assertEquals(50, shop.calculateTotalAmountOfOrder(product_Quantity), "product p1 price after discounts should have been 4");
        product_Quantity.put(orangeID, 7);
        when(mockBasket.calculateTotal()).thenReturn(90.0);
        assertEquals(90.0, shop.calculateTotalAmountOfOrder(product_Quantity), "product p2 price after discounts should have been 4");
        product_Quantity = new HashMap<>();
        product_Quantity.put(orangeID + 200, 3);
        when(mockBasket.calculateTotal()).thenReturn(0.0);
        assertEquals(0.0, shop.calculateTotalAmountOfOrder(product_Quantity), "product p3 doesn't not exist and should have returned price 0");
    }

    @Test
    void checkOut() {
        TransactionInfo trans = new TransactionInfo("hamood", "Haham mood", "tel hai 166", "0534356345", "123123", "10/30", LocalDate.of(2022, 5, 10), 68);
        Map<Integer, Integer> product_QuantityInBasket = new HashMap<>();
        product_QuantityInBasket.put(appleID, 3);
        product_QuantityInBasket.put(orangeID, 7);
        MarketSystem ms = mock(MarketSystem.class);
        when(ms.pay(trans)).thenReturn(true);
        try {
            when(ms.supply(trans, product_QuantityInBasket)).thenReturn(true);
        } catch (BlankDataExc blankDataExc) {
            fail(blankDataExc.getMessage());
            return;
        }
        ResponseT<Order> checkoutRet = null;
        try {
            checkoutRet = shop.checkout(product_QuantityInBasket, trans);
        } catch (BlankDataExc blankDataExc) {
            fail(blankDataExc.getMessage());
            return;
        }
        assertFalse(checkoutRet.isErrorOccurred());
        assertFalse(shop.isProductAvailable(appleID));
        assertFalse(shop.isProductAvailable(orangeID));
    }

    @Test
    void checkOut2() {
        TransactionInfo trans = new TransactionInfo("hamood", "Haham mood", "tel hai 166", "0534356345", "123123", "10/30", LocalDate.of(2022, 5, 10), 68);
        Map<Integer, Integer> product_QuantityInBasket = new HashMap<>();
        product_QuantityInBasket.put(appleID, 3);
        MarketSystem ms = mock(MarketSystem.class);
        when(ms.pay(trans)).thenReturn(true);
        try {
            when(ms.supply(trans, product_QuantityInBasket)).thenReturn(true);
        } catch (BlankDataExc blankDataExc) {
            fail(blankDataExc.getMessage());
            return;
        }
        ResponseT<Order> checkoutRet = null;
        try {
            checkoutRet = shop.checkout(product_QuantityInBasket, trans);
        } catch (BlankDataExc blankDataExc) {
            fail(blankDataExc.getMessage());
            return;
        }
        assertFalse(checkoutRet.isErrorOccurred());
        assertFalse(shop.isProductAvailable(appleID));
        assertTrue(shop.isProductAvailable(orangeID));
    }


    //predTypes should be either 1(price) or 2(productRequiredToBuy)
    //logical gates should be either 1(Or Gate) 2(And Gate) or 3(Xor gate)
/*    @Test
    void makeAndPred() {
        Predicate<Tuple<Map<Integer, Integer>, Double>> andPredicate;
        List<Integer> logicalGates;
        List<Integer> predTypes;

        List<Tuple<Tuple<Integer, Integer>, Double>> toBuildFrom;
        int[] productIDs = {1, 2, 3, 4};
        int[] amount = {3, 3, 3, 3,};
        double[] prices = {10, 10, 10, 10};
        try {
            toBuildFrom = makePredSetupToBuildFrom(productIDs, amount, prices);
        } catch (Exception e) {
            fail("test poorly written. either productIds, amounts, or prices aren't done correct");
            return;
        }
        try {
            //andPredicate = shop.makePred(logicalGates, predTypes, toBuildFrom);
        } catch (Exception e) {
            System.out.println("function issue with processing the parameters given.\n" + e.getMessage());
            fail("function issue with processing the parameters given.\n" + e.getMessage());
            return;
        }
        Map<Integer, Integer> product_amount = new HashMap<>();
        Tuple<Map<Integer, Integer>, Double> customerBasket1 = new Tuple<>(product_amount, 5.0);
        assertFalse(andPredicate.test(customerBasket1));
        product_amount.put(1, 2);
        assertFalse(andPredicate.test(customerBasket1));
        product_amount.put(2, 5);
        assertFalse(andPredicate.test(customerBasket1));
        product_amount.put(3, 3);
        assertFalse(andPredicate.test(customerBasket1));
        product_amount.put(4, 5);
        assertFalse(andPredicate.test(customerBasket1));
        Tuple<Map<Integer, Integer>, Double> customerBasket2 = new Tuple<>(product_amount, 10.0);
        assertTrue(andPredicate.test(customerBasket2));
        product_amount.remove(4);
        assertFalse(andPredicate.test(customerBasket2));
        product_amount.put(1, 3);
        assertFalse(andPredicate.test(customerBasket2));
    }

    @Test
    void makeOrPred() {
        Predicate<Tuple<Map<Integer, Integer>, Double>> orPredicate;
        List<Integer> logicalGates;
        List<Integer> predTypes;
        int[] intsPreds = {1, 2, 2, 2};
        predTypes = Arrays.stream(intsPreds).boxed().collect(Collectors.toList());
        int[] intsLGates = {1, 1, 1};
        logicalGates = Arrays.stream(intsLGates).boxed().collect(Collectors.toList());
        List<Tuple<Tuple<Integer, Integer>, Double>> toBuildFrom;
        int[] productIDs = {1, 2, 3, 4};
        int[] amount = {3, 3, 3, 3,};
        double[] prices = {10, 10, 10, 10};
        try {
            toBuildFrom = makePredSetupToBuildFrom(productIDs, amount, prices);
        } catch (Exception e) {
            fail("test poorly written. either productIds, amounts, or prices aren't done correct");
            return;
        }
        try {
            orPredicate = shop.makePred(logicalGates, predTypes, toBuildFrom);
        } catch (Exception e) {
            System.out.println("function issue with processing the parameters given.\n" + e.getMessage());
            fail("function issue with processing the parameters given.\n" + e.getMessage());
            return;
        }
        Map<Integer, Integer> product_amount = new HashMap<>();
        Tuple<Map<Integer, Integer>, Double> customerBasket1 = new Tuple<>(product_amount, 5.0);
        assertFalse(orPredicate.test(customerBasket1));
        product_amount.put(1, 2);
        assertFalse(orPredicate.test(customerBasket1));
        product_amount.put(2, 5);
        assertTrue(orPredicate.test(customerBasket1));
        product_amount.put(3, 3);
        assertTrue(orPredicate.test(customerBasket1));
        product_amount.remove(2);
        product_amount.remove(3);
        assertFalse(orPredicate.test(customerBasket1));
        product_amount.put(4, 5);
        assertTrue(orPredicate.test(customerBasket1));
        Tuple<Map<Integer, Integer>, Double> customerBasket2 = new Tuple<>(product_amount, 10.0);
        assertTrue(orPredicate.test(customerBasket2));
        product_amount.remove(4);
        assertTrue(orPredicate.test(customerBasket2));
        product_amount.put(1, 3);
        assertFalse(orPredicate.test(customerBasket1));
    }


    List<Tuple<Tuple<Integer, Integer>, Double>> makePredSetupToBuildFrom(int[] productIDs, int[] amount, double[] prices) throws Exception {
        if (productIDs.length != amount.length || amount.length != prices.length) {
            throw new Exception("bad parameters given");
        }
        List<Tuple<Tuple<Integer, Integer>, Double>> toRetList = new ArrayList<>();
        for (int i = 0; i < productIDs.length; i++) {
            Tuple<Integer, Integer> prod_amounts = new Tuple<>(productIDs[i], amount[i]);
            Tuple<Tuple<Integer, Integer>, Double> prod_amounts_prices = new Tuple<>(prod_amounts, prices[i]);
            toRetList.add(prod_amounts_prices);
        }
        return toRetList;
    }

    @Test
    void checkPredMakerParameters(){
        List<Integer> predTypes = new ArrayList<>();
        List<Integer> logicalGates = new ArrayList<>();
        List<Tuple<Tuple<Integer, Integer>, Double>> toBuildFrom = new ArrayList<>();
        int[] productIDs = {1, 2, 3, 4};
        int[] amount = {3, 3, 3, 3,};
        double[] prices = {10, 10, 10, 10};

        List<Tuple<Tuple<Integer, Integer>, Double>> finalToBuildFrom = toBuildFrom;
        assertThrows(InvalidParamException.class, ()->shop.checkPredMakerParameters(logicalGates, predTypes, finalToBuildFrom));
        predTypes.add(2);
        assertThrows(InvalidParamException.class, ()->shop.checkPredMakerParameters(logicalGates, predTypes, finalToBuildFrom));
        toBuildFrom.add(new Tuple<>(new Tuple<>(1, 10), 10.0));
        List<Tuple<Tuple<Integer, Integer>, Double>> finalToBuildFrom1 = toBuildFrom;
        assertDoesNotThrow(()->shop.checkPredMakerParameters(logicalGates, predTypes, finalToBuildFrom1));
        predTypes.add(4);
        toBuildFrom.add(new Tuple<>(new Tuple<>(1, 10), 10.0));
        List<Tuple<Tuple<Integer, Integer>, Double>> finalToBuildFrom2 = toBuildFrom;
        assertThrows(InvalidParamException.class, ()->shop.checkPredMakerParameters(logicalGates, predTypes, finalToBuildFrom2));
        logicalGates.add(2);
        assertThrows(InvalidParamException.class ,()->shop.checkPredMakerParameters(logicalGates, predTypes, finalToBuildFrom2));
    }*/

    @Test
    void PermissionsThreads() throws InterruptedException, InvalidSequenceOperationsExc {
        User davidos = setDavidos();
        List<ShopManagersPermissions> shopManagersPermissionsList = new LinkedList<>();
        shopManagersPermissionsList.add(ShopManagersPermissions.OpenShop);
        shopManagersPermissionsList.add(ShopManagersPermissions.CloseShop);
        shop.removePermissions(shopManagersPermissionsList,davidos.getUserName(),davidos.getUserName());
        List<ShopManagersPermissions> shopManagersPermissionsListRemove = new LinkedList<>();
        shopManagersPermissionsListRemove.add(ShopManagersPermissions.AddProductToInventory);
        shopManagersPermissionsListRemove.add(ShopManagersPermissions.RemoveProductFromInventory);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                shop.addPermissions(shopManagersPermissionsList,davidos.getUserName(),davidos.getUserName());
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    shop.removePermissions(shopManagersPermissionsListRemove,davidos.getUserName(),davidos.getUserName());
                } catch (InvalidSequenceOperationsExc e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("");
    }
}
