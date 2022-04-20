package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inv;


    @BeforeEach
    void setUp() {
        inv = new Inventory();
        ProductImp p = new ProductImp(1, "Iphone13", "Apples new overpriced smartphone", "smartphone");
        ProductImp p2 = new ProductImp(2,"Galaxy s22", "Samsung new overpriced smartphone", "smartphone");
        inv.addProduct(p,3499.90, 100);
        inv.addProduct(p2,2999.90, 0);
    }

    @Test
    void isInStock() {
        assertTrue(inv.isInStock(1));
        inv.setAmount(1, 0);
        assertFalse(inv.isInStock(2));
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