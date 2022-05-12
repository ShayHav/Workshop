package domain.user;

import domain.shop.ProductInfo;
import domain.Exceptions.ProductNotFoundException;
import domain.shop.Shop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingBasketTest {

    ShoppingBasket basket;
    Shop shop;
    ProductInfo productInfo;

    @BeforeEach
    void setUp() {
        shop = mock(Shop.class);
        productInfo = mock(ProductInfo.class);
        basket = new ShoppingBasket(shop);
        Map<Integer, Integer> productsWithAmount = basket.getProductAmountList();
        productsWithAmount.put(1, 100);
    }


    @Test
    void addProductToBasket() {
        assertFalse(basket.addProductToBasket(5, -1));
        assertTrue(basket.addProductToBasket(5, 0));
        assertTrue(basket.getProductAmountList().containsKey(5));
        assertTrue(basket.addProductToBasket(5, 50));
        assertNotEquals(basket.getProductAmountList().get(1),50);
        assertTrue(basket.addProductToBasket(1, 50));
        assertEquals(basket.getProductAmountList().get(1), 150);

    }

    @Test
    void calculateTotalAmount() {
        Map<Integer,Integer> productsWithAmount = basket.getProductAmountList();
        when(shop.calculateTotalAmountOfOrder(productsWithAmount)).thenReturn(100.0);
        assertEquals(basket.calculateTotalAmount(), 100.0);
        productsWithAmount.put(2, 50);
        when(shop.calculateTotalAmountOfOrder(productsWithAmount)).thenReturn(200.0);
        assertEquals(basket.calculateTotalAmount(), 200.0);
    }

    @Test
    void updateAmount() {
        assertFalse(basket.updateAmount(1, -1));
        assertTrue(basket.getProductAmountList().containsKey(1));
        assertTrue(basket.updateAmount(1, 50));
        assertTrue(basket.getProductAmountList().containsKey(1));
        assertTrue(basket.updateAmount(1, 0));
        assertFalse(basket.getProductAmountList().containsKey(1));
    }

    @Test
    void removeProduct() {
        basket.getProductAmountList().put(2,200);
        assertTrue(basket.removeProduct(1));
        assertFalse(basket.getProductAmountList().containsKey(1));
        assertTrue(basket.removeProduct(2));
        assertFalse(basket.getProductAmountList().containsKey(2));
    }


    @Test
    void showBasket() throws ProductNotFoundException {
        when(shop.getShopID()).thenReturn(1);
        when(shop.getName()).thenReturn("david&sons");
        when(shop.getInfoOnProduct(1)).thenReturn(productInfo);
        when(shop.calculateTotalAmountOfOrder(basket.getProductAmountList())).thenReturn(100.0);
        ShoppingBasket.BasketInfo info = basket.showBasket();
        assertEquals(info.getShopId(),1);
        assertEquals(info.getShopName(),"david&sons");
        assertEquals(info.getProductWithAmount().get(productInfo), 100);
        assertEquals(info.getTotalAmount(), 100.0);
    }
}