package domain.shop;

import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.InvalidProductInfoException;
import domain.ResponseT;
import domain.market.ExternalConnector;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.user.TransactionInfo;
import domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ShopTest {
    DiscountPolicy discountPolicy;
    PurchasePolicy purchasePolicy;
    Shop shop;
    int appleID;
    int orangeID;

    @BeforeEach
    public void setup() {
        ExternalConnector ec = mock(ExternalConnector.class);
        setDavidos();
        setDavidosShop();
    }

    private User setDavidos(){
        User davidos = mock(User.class);
        when(davidos.getUserName()).thenReturn("Davidos");
        return davidos;
    }

    private void setDavidosShop(){
        User davidos = setDavidos();
        shop = new Shop("David's", discountPolicy, purchasePolicy, davidos, 1);
        discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        Product p1 = mock(ProductImp.class);
        Product p2 = mock(ProductImp.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        try {
            orangeID = shop.addListing("orange", "red orange", "fruits", 12.0, 7, "Davidos").getId();
            appleID = shop.addListing("apple", "red apple", "fruits", 5.0, 3, "Davidos").getId();
        }catch (InvalidAuthorizationException InvAuthExc){
            fail("founder can add product");
            return;
        }catch (InvalidProductInfoException InvProdInfoExc){
            fail("product info is legal");
            return;
        }
        when(discountPolicy.calcPricePerProduct(appleID, 5.0, 3)).thenReturn(4.0);
        when(discountPolicy.calcPricePerProduct(orangeID, 12.0, 7)).thenReturn(8.0);
        when(purchasePolicy.checkIfProductRulesAreMet(anyString(),anyInt(), anyDouble(), anyInt())).thenReturn(true);
        shop.setDiscountPolicy(discountPolicy);
        shop.setPurchasePolicy(purchasePolicy);


    }

    @Test
    void addAndGetProduct() {
        Product apple;
        try {
            apple = shop.addListing("apple", "red apple", "fruits", 5.0, 3, "Davidos");
        }catch (InvalidAuthorizationException InvAuthExc){
            fail("founder can add product");
            return;
        }catch (InvalidProductInfoException InvProdInfoExc){
            fail("product info is legal");
            return;
        }
        appleID = apple.getId();
        assertTrue(shop.isProductAvailable(apple.getId()));
        assertEquals(shop.getProduct(apple.getId()).getId(), apple.getId());
        assertTrue(shop.getProduct(appleID).getName().compareTo("apple") == 0, "product p1 should have been returned");
        assertTrue(shop.getProduct(appleID).getDescription().compareTo("red apple") == 0);
        assertThrows(NullPointerException.class, ()->shop.getProduct(appleID + 100), "this item should not exist");
        Product orange;
        try {
            orange = shop.addListing("orange", "red orange", "fruits", 12.0, 7, "Davidos");
        }catch (InvalidAuthorizationException InvAuthExc){
            fail("founder can add product");
            return;
        }catch (InvalidProductInfoException InvProdInfoExc){
            fail("product info is legal");
            return;
        }
        orangeID = orange.getId();
        assertTrue(shop.isProductAvailable(orangeID));
        assertEquals(shop.getProduct(orangeID).getId(), orangeID);
        assertEquals(0, shop.getProduct(orangeID).getName().compareTo("orange"), "product p1 should have been returned");
        assertEquals(0, shop.getProduct(orangeID).getDescription().compareTo("red orange"), "incorrect description of orange");
    }



    @Test
    void productPriceAfterDiscounts(){
        assertEquals( 4.0, shop.productPriceAfterDiscounts(appleID, 3), "product p1 price after discounts should have been 4");
        assertEquals(8.0, shop.productPriceAfterDiscounts(orangeID, 7),  "product p2 price after discounts should have been 8");
        assertEquals(0.0, shop.productPriceAfterDiscounts(orangeID + 200, 4),  "product p3 doesn't not exist and should have returned price 0");
    }





    @Test
    void calculateTotalAmountOfOrder(){
        Map<Integer, Integer> product_Quantity = new HashMap<>();
        product_Quantity.put(appleID, 3);
        assertEquals(12.0, shop.calculateTotalAmountOfOrder(product_Quantity),"product p1 price after discounts should have been 4");
        product_Quantity.put(orangeID, 7);
        assertEquals( 68.0, shop.calculateTotalAmountOfOrder(product_Quantity), "product p2 price after discounts should have been 8");
        product_Quantity = new HashMap<>();
        product_Quantity.put(orangeID + 200, 3);
        assertEquals(0.0, shop.calculateTotalAmountOfOrder(product_Quantity),  "product p3 doesn't not exist and should have returned price 0");
    }

    @Test
    void checkOut(){
        TransactionInfo trans = new TransactionInfo("hamood", "Haham mood", "tel hai 166", "0534356345", "123123", "10/30", LocalDate.of(2022, 05, 10), 68);
        Map<Integer, Integer> product_QuantityInBasket = new HashMap<>();
        product_QuantityInBasket.put(appleID, 3);
        product_QuantityInBasket.put(orangeID, 7);
        MarketSystem ms = mock(MarketSystem.class);
        when(ms.pay(trans)).thenReturn(true);
        when(ms.supply(trans, product_QuantityInBasket)).thenReturn(true);
        ResponseT<Order> checkoutRet = shop.checkout(product_QuantityInBasket, trans);
        assertFalse(checkoutRet.isErrorOccurred());
        assertFalse(shop.isProductAvailable(appleID));
        assertFalse(shop.isProductAvailable(orangeID));
    }

    @Test
    void checkOut2(){
        TransactionInfo trans = new TransactionInfo("hamood", "Haham mood", "tel hai 166", "0534356345", "123123", "10/30", LocalDate.of(2022, 05, 10), 68);
        Map<Integer, Integer> product_QuantityInBasket = new HashMap<>();
        product_QuantityInBasket.put(appleID, 3);
        ResponseT<Order> checkoutRet = shop.checkout(product_QuantityInBasket, trans);
        assertFalse(checkoutRet.isErrorOccurred());
        assertFalse(shop.isProductAvailable(appleID));
        assertTrue(shop.isProductAvailable(orangeID));
    }

}
