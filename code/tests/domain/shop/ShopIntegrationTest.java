package domain.shop;

import domain.Exceptions.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.predicate.ToBuildDiscountPredicate;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShopIntegrationTest {
    PurchasePolicy purchasePolicy;
    Shop shop;
    int appleID;
    int orangeID;
    int shoesID;
    User storeOwner;
    String category1 = "fruits";
    String category2 = "shoes";

    @BeforeEach
    public void setup() {
        setDavidosShop();
    }

    private void setDavidos() {
        User davidos = mock(User.class);
        when(davidos.getUserName()).thenReturn("Davidos");
        storeOwner = davidos;
    }

    private void setDavidosShop() {
        setDavidos();
        shop = new Shop("David's", "good shop", storeOwner, 1);
        //discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        Product p1 = mock(ProductImp.class);
        Product p2 = mock(ProductImp.class);
        Product p3 = mock(ProductImp.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p3.getId()).thenReturn(3);
        try {
            orangeID = shop.addListing(1,"orange", "red orange", category1, 12.0, 7, "Davidos").getId();
            appleID = shop.addListing(2, "apple", "red apple", category1, 5.0, 40, "Davidos").getId();
            shoesID = shop.addListing(5, "black shoes", "black running shoes", category2, 20, 10, "Davidos").getId();
        } catch (InvalidAuthorizationException InvAuthExc) {
            fail("founder can add product");
            return;
        } catch (InvalidProductInfoException InvProdInfoExc) {
            fail("product info is legal");
            return;
        }
        when(purchasePolicy.checkCart_RulesAreMet(Mockito.any())).thenReturn(true);
        shop.setPurchasePolicy(purchasePolicy);
    }

    @Test
    void addDiscount(){
        int discountID;
        try {
            discountID = shop.addSimpleProductDiscount(storeOwner.getUserName(), 1, 10);
        } catch (InvalidParamException invalidParamException) {
            fail("params given are viable");
            return;
        } catch (ProductNotFoundException productNotFoundException) {
            fail("product exists");
            return;
        }

        Map<Integer, Integer> productAmounts  = new HashMap<>();

        productAmounts.put(orangeID, 2);
        assertEquals(21.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        productAmounts.put(appleID, 2);
        assertEquals(31.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        shop.removeDiscount(storeOwner.getUserName(), discountID);
        assertEquals(34, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
    }


    @Test
    void addConditionalDiscount(){
        int discountID1;
        ToBuildDiscountPredicate toBuild1;
        ToBuildDiscountPredicate toBuild2;
        try {
            toBuild1 = new ToBuildDiscountPredicate(shoesID, "black shoes", 2);
            toBuild2 = new ToBuildDiscountPredicate(100);
        } catch (InvalidParamException invalidParamException) {
            fail("the tobuilds are written incorrectly.");
            return;
        }
        int discountID2;

        try {
            discountID1 = shop.addConditionalProductDiscount(storeOwner.getUserName(), orangeID, 10, toBuild1);
            discountID2 = shop.addConditionalCategoryDiscount(storeOwner.getUserName(), category2, 20, toBuild2);
        } catch (InvalidParamException invalidParamException) {
            fail("params given are viable");
            return;
        } catch (ProductNotFoundException productNotFoundException) {
            fail("product exists");
            return;
        } catch (AccessDeniedException accessDeniedException) {
            fail("toBuild accessed illegally" + accessDeniedException.getMessage());
            return;
        } catch (CriticalInvariantException criticalInvariantException) {
            fail("toBuild has an unsupported type");
            return;
        }

        Map<Integer, Integer> productAmounts  = new HashMap<>();

        productAmounts.put(orangeID, 2);
        assertEquals(24, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        productAmounts.put(shoesID, 2);
        assertEquals(61.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        productAmounts.put(appleID, 2);
        assertEquals(71.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        productAmounts.remove(appleID);
        productAmounts.put(orangeID, 5);
        assertEquals(32 + 54 , shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        shop.removeDiscount(storeOwner.getUserName(), discountID2);
        assertEquals(40 + 54 , shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
    }


    @Test
    void complexDiscount(){
        int discountID1;
        int discountID2;
        int discountID3;
        int discountID4;
        int complexDiscountID1;
        int complexDiscountID2;
        int complexDiscountID3;

        ToBuildDiscountPredicate toBuild3;
        ToBuildDiscountPredicate toBuild4;
        try {
            toBuild3 = new ToBuildDiscountPredicate(shoesID, "black shoes", 1);
            toBuild4 = new ToBuildDiscountPredicate(100);
        } catch (InvalidParamException invalidParamException) {
            fail("the tobuilds are written incorrectly.");
            return;
        }
        try {
            discountID1 = shop.addSimpleProductDiscount(storeOwner.getUserName(), appleID, 20);
            discountID2 = shop.addSimpleCategoryDiscount(storeOwner.getUserName(), category1, 15);
            discountID3 = shop.addConditionalProductDiscount(storeOwner.getUserName(), orangeID, 20, toBuild3);
            discountID4 = shop.addConditionalProductDiscount(storeOwner.getUserName(), orangeID, 20, toBuild4);
            //either 20 percent off on apples or 15 percent off on fruits.
            complexDiscountID1 = shop.addXorDiscount(storeOwner.getUserName(), discountID1, discountID2);
            //20 percent off oranges if bought black shoe or if basket price >= 100
            complexDiscountID2 = shop.addOrDiscount(storeOwner.getUserName(), discountID3, discountID4);
            //(20 percent off on oranges) or (or 20 percent off on apples or 15 percent off on fruits) if bought black shoe or if basket price >= 100
            complexDiscountID3 = shop.addAndDiscount(storeOwner.getUserName(), complexDiscountID1, complexDiscountID2);
        } catch (InvalidParamException invalidParamException) {
            fail("params given are viable");
            return;
        } catch (ProductNotFoundException productNotFoundException) {
            fail("product exists");
            return;
        } catch (AccessDeniedException accessDeniedException) {
            fail("toBuild accessed illegally" + accessDeniedException.getMessage());
            return;
        } catch (CriticalInvariantException criticalInvariantException) {
            fail("toBuild has an unsupported type");
            return;
        } catch (DiscountNotFoundException discountNotFoundException) {
            fail("discount should have existed");
            return;
        }

        Map<Integer, Integer> productAmounts  = new HashMap<>();
        //(20 percent off on oranges) or (or 20 percent off on apples or 15 percent off on fruits) if bought black shoe or if basket price >= 100
        productAmounts.put(orangeID, 1);
        //og price 24
        assertEquals(12, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        //productAmounts.remove(orangeID);
        productAmounts.put(shoesID, 1);
        //og price is 20
        assertEquals(20 + 9.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        productAmounts.put(appleID, 10);
        assertEquals(20 + 12 + 40, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        productAmounts.put(orangeID, 2);
        assertEquals(20 + 42.5 + 20.4, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        productAmounts.remove(shoesID);
        assertEquals(50 + 24, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        productAmounts.put(orangeID, 5);
        assertEquals(42.5 + 51, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
        productAmounts.put(shoesID, 1);
        assertEquals(20 + 42.5 + 51 , shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
    }



    /*@Test
    void bidTest(){
        String regUN = "Haim Nehmad";
        User buyer;
        try {
            MarketSystem.getInstance().
                    MarketSystem.getInstance().register("haim", regUN, "123456");
            buyer = MarketSystem.getInstance().getUser(regUN);
        } catch (BlankDataExc blankDataExc) {
            fail("params given are viable");
            return;
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            fail("sequencing incorrect");
            return;
        } catch (IncorrectIdentification incorrectIdentification) {
            fail("id incorrect, should not happen");
            return;
        } catch (InvalidAuthorizationException e) {
            fail("invalid authorization");
            return;
        }
        int bidID;
        ResponseT<Integer> bidSucceed;
        try {
            bidSucceed = buyer.addNewBid(shop.getShopID(), orangeID, 1);
        } catch (ShopNotFoundException shopNotFoundException) {
            fail("shop should exist");
            return;
        }

        if(bidSucceed.isErrorOccurred()) {
            fail("bid should succeed");
            return;
        }

        bidID = bidSucceed.getValue();

    }
*/





}
