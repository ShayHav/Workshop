package domain.shop;

import domain.Exceptions.*;
import domain.Responses.ResponseT;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Basket;
import domain.shop.discount.DiscountPolicy;
import domain.shop.predicate.ToBuildDiscountPredicate;
import domain.user.TransactionInfo;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShopIntegrationTest {
    DiscountPolicy discountPolicy;
    PurchasePolicy purchasePolicy;
    Shop shop;
    int appleID;
    int orangeID;
    int shoesID;
    Basket mockBasket;
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
            appleID = shop.addListing(2, "apple", "red apple", category1, 5.0, 10, "Davidos").getId();
            shoesID = shop.addListing(5, "black shoes", "black running shoes", category2, 20, 10, "Davidos").getId();
        } catch (InvalidAuthorizationException InvAuthExc) {
            fail("founder can add product");
            return;
        } catch (InvalidProductInfoException InvProdInfoExc) {
            fail("product info is legal");
            return;
        }
        when(purchasePolicy.checkCart_RulesAreMet(Mockito.anyMap())).thenReturn(true);
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
    void ComplexDiscount(){
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
        try {
            discountID1 = shop.addSimpleProductDiscount(storeOwner.getUserName(), 1, 10);
        } catch (InvalidParamException invalidParamException) {
            fail("params given are viable");
            return;
        } catch (ProductNotFoundException productNotFoundException) {
            fail("product exists");
            return;
        }

        Map<Integer, Integer> productAmounts  = new HashMap<>();

        productAmounts.put(1, 2);
        assertEquals(21.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        productAmounts.put(2, 2);
        assertEquals(31.6, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));

        shop.removeDiscount(storeOwner.getUserName(), discountID1);
        assertEquals(34, shop.calculateTotalAmountOfOrder(productAmounts, new ArrayList<>()));
    }







}
