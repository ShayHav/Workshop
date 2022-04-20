package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inv;
    ProductImp p = new ProductImp(1, "Iphone13", "Apples new overpriced smartphone", "smartphone");
    ProductImp p2 = new ProductImp(2,"Galaxy s22", "Samsung new overpriced smartphone", "smartphone");

    @BeforeEach
    void setUp() {
        inv = new Inventory();
        inv.addProduct(p,3499.90, 100);
        inv.addProduct(p2,2999.90, 100);
    }

    @Test
    void isInStock() {
        assertTrue(inv.isInStock(p));
        inv.setAmount(p, 0);
        assertFalse(inv.isInStock(p));
    }

    @Test
    void getPrice() {
    }

    @Test
    void getQuantity() {
    }

    @Test
    void addProduct() {
    }

    @Test
    void setPrice() {
    }

    @Test
    void setAmount() {
    }

    @Test
    void removeProduct() {
    }


}