package domain.user;

import domain.shop.Product;
import domain.Exceptions.ProductNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingBasketTest {

    ShoppingBasket basket;
    Shop shop;
    Product productInfo;

    @BeforeEach
    void setUp() {
        shop = mock(Shop.class);
        productInfo = mock(Product.class);
        basket = new ShoppingBasket(shop);
        Map<Integer, Integer> productsWithAmount = basket.getProductAmountList();
        productsWithAmount.put(1, 100);
    }


    @Test
    void addProductToBasket() throws ProductNotFoundException {
        when(shop.isProductIsAvailable(5)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,()->basket.addProductToBasket(5, -1));
        basket.addProductToBasket(5,50);
        assertEquals(50,basket.getProductAmountList().get(5));
        assertTrue(basket.getProductAmountList().containsKey(5));
        assertNotEquals(basket.getProductAmountList().get(1),50);
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
    void updateAmount() throws ProductNotFoundException {
        when(shop.isProductIsAvailable(5)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, ()->basket.updateAmount(1, -1));
        assertTrue(basket.getProductAmountList().containsKey(1));
        basket.updateAmount(1, 50);
        assertEquals(50,basket.getProductAmountList().get(5));
        assertTrue(basket.getProductAmountList().containsKey(1));
        basket.updateAmount(1, 0);
        assertFalse(basket.getProductAmountList().containsKey(1));
    }

    @Test
    void removeProduct() throws ProductNotFoundException {
        when(shop.isProductIsAvailable(5)).thenReturn(true);
        basket.getProductAmountList().put(2,200);
        basket.removeProduct(1);
        assertFalse(basket.getProductAmountList().containsKey(1));
        basket.removeProduct(2);
        assertFalse(basket.getProductAmountList().containsKey(2));
    }


    @Test
    void showBasket() throws ProductNotFoundException {
        when(shop.getShopID()).thenReturn(1);
        when(shop.getName()).thenReturn("david&sons");
        when(shop.getInfoOnProduct(1)).thenReturn(productInfo);
        when(shop.calculateTotalAmountOfOrder(basket.getProductAmountList())).thenReturn(100.0);
        ShoppingBasket.ServiceBasket info = basket.showBasket();
        assertEquals(info.getShopId(),1);
        assertEquals(info.getShopName(),"david&sons");
        assertEquals(info.getProductWithAmount().get(productInfo), 100);
        assertEquals(info.getTotalAmount(), 100.0);
    }
}