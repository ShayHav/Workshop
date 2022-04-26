package domain.shop;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
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
        inv.addProduct(1,3499.90, 100);
        inv.addProduct(2,2999.90, 0);
    }

    @Test
    void isInStock() {
        assertTrue(inv.isInStock(1));
        inv.setAmount(1, 0);
        assertFalse(inv.isInStock(2));
    }

    @Test
    void addProduct() {
        assertFalse(inv.addProduct(1, 1500, 12));
        ProductImp p = new ProductImp(3,"Xiaomi mi19", "chines phone", "smartphone");
        assertFalse(inv.addProduct(3, 2000.0, 50));
        when(products.getProduct(3)).thenReturn(p);
        assertFalse(inv.addProduct(3, -50, 1));
        assertFalse(inv.addProduct(3, 2000, -1));
        assertTrue(inv.addProduct(3, 2000, 1));
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
        assertEquals();
    }


}