package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryTest {

    Inventory inv;
    Products products;

    @BeforeEach
    void setUp() {
        inv = new Inventory();
        products = mock(Products.class);
        inv.setProductList(products);
        ProductImp p1 = new ProductImp(1, "Iphone13", "Apples new overpriced smartphone", "smartphone");
        ProductImp p2 = new ProductImp(2,"Galaxy s22", "Samsung new overpriced smartphone", "smartphone");
        when(products.getProduct(1)).thenReturn( p1);
        when(products.getProduct(2)).thenReturn( p2);
        inv.addProduct("Iphone13", "Apples new overpriced smartphone", "smartphone",3499.90, 100);
        inv.addProduct("Galaxy s22", "Samsung new overpriced smartphone", "smartphone",2999.90, 0);
    }

    @Test
    void isInStock() {
        assertTrue(inv.isInStock(1));
        inv.setAmount(1, 0);
        assertFalse(inv.isInStock(2));
    }

    @Test
    void setPrice() {
        assertFalse(inv.setPrice(-1,1));
        assertFalse(inv.setPrice(1, -1));
        assertTrue(inv.setPrice(1,3399.90));
    }

    @Test
    void setAmount() {
        assertFalse(inv.setAmount(-1,1));
        assertFalse(inv.setAmount(1, -1));
        assertTrue(inv.setAmount(1,500));
    }

    @Test
    void removeProduct() {
        assertNull(inv.findProduct(-1));
        inv.removeProduct(1);
        assertNull(inv.findProduct(1));
    }

    @Test
    void getItemsInStock(){
        assertEquals(inv.getItemsInStock().size(),2);
        inv.setAmount(1,0);
        assertEquals(inv.getItemsInStock().size(),1);
        inv.setAmount(2,0);
        assertEquals(inv.getItemsInStock().size(),0);
    }

    @Test
    void reduceAmount(){
        int currQuan = inv.getQuantity(1);
        inv.reduceAmount(1,5);
        assertEquals(inv.getQuantity(1) - 5, currQuan - 5);
    }

    @Test
    void reservedItem(){
        Map<Integer, Integer> map = new HashMap<>();
        int p1Quan = inv.getQuantity(1), p2Quan = inv.getQuantity(2);
        map.put(2,3);
        assertFalse(inv.reserveItems(map));
        assertEquals(inv.getQuantity(2),p2Quan);
        map.put(2,0);
        map.put(1, 3);
        assertTrue(inv.reserveItems(map));
        assertEquals(inv.getQuantity(1) ,p1Quan - 3);
        assertEquals(inv.getQuantity(2) ,p1Quan);
    }

    @Test
    void restoreStock(){
        Map<Integer, Integer> map = new HashMap<>();
        int p1Quan = inv.getQuantity(1), p2Quan = inv.getQuantity(2);
        map.put(1,3);
        map.put(2, 3);
        assertEquals(inv.getQuantity(1), p1Quan + 3);
        assertEquals(inv.getQuantity(2), p2Quan + 3);
    }


}