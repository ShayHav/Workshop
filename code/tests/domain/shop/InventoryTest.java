package domain.shop;

import domain.Exceptions.InvalidProductInfoException;
import domain.Exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;



class InventoryTest {

    Inventory inv;

    @BeforeEach
    void setUp() throws Exception {
        inv = new Inventory();

        try {
            inv.addProduct(1,"Iphone13", "Apples new overpriced smartphone", "smartphone",3499.90, 100);
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("add product should have succeeded, all product info is legal");
            throw new Exception("Before each fail, abort");
        }

        try {
            inv.addProduct(2,"Galaxy s22", "Samsung new overpriced smartphone", "smartphone",2999.90, 0);
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("add product should have succeeded, all product info is legal");
            throw new Exception("Before each fail, abort");
        }
    }

    @Test
    void isInStock() {
        assertTrue(inv.isInStock(1));
        try {
            inv.setAmount(1, 0);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
        }
        assertFalse(inv.isInStock(1));
        assertFalse(inv.isInStock(2));
    }
    @Test
    void isInStockThread() {
        final String[] s = {""};

        ExecutorService pool = Executors.newFixedThreadPool(2);
        for(int i =0;i<2;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (inv.isInStock(1))
                        s[0] = "isInStock";
                    else s[0] = "notInStock";
                }
            };
            pool.execute(r);
        }


        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        assertTrue(s[0].equals("isInStock"));
    }

    @Test
    void setPrice() {
        double priceProduct1;
        assertThrows(ProductNotFoundException.class, ()->inv.setPrice(-1,1), "-1 is an illegal ID and should not exist");
        try {
            priceProduct1 = inv.getPrice(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }
        assertEquals(3499.90, priceProduct1);
        assertThrows(InvalidProductInfoException.class, ()->inv.setPrice(1,-1), "0 is an illegal price");
        try {
            priceProduct1 = inv.getPrice(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }
        assertEquals(3499.90, priceProduct1);
        try {
            inv.setPrice(1, 3399.90);
            priceProduct1 = inv.getPrice(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info is legal");
            return;
        }
        assertEquals(3399.90, priceProduct1);
    }
    @Test
    void setPriceThreads() {
        //TODO:
    }

    @Test
    void setAmount() {
        assertThrows(ProductNotFoundException.class ,()->inv.setAmount(-1,1), "Illegal product id, product should not exist");
        assertThrows(InvalidProductInfoException.class ,()->inv.setAmount(1,-1), "-1 is an Illegal product amount");
        int prod1Quantity;
        try {
            inv.setAmount(1,500);
            prod1Quantity = inv.getQuantity(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }
        assertEquals(500, prod1Quantity);
        try {
            inv.setAmount(1,300);
            prod1Quantity = inv.getQuantity(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }
        assertEquals(300, prod1Quantity);
    }

    /*@Test
    void removeProduct() {
        assertNull(inv.findProduct(-1), "product id -1 is illegal, should not exist and thus should have not been found");
        assertNotNull(inv.findProduct(1), "product id 1 should exist but can't be found");
        inv.removeProduct(1);
        assertNull(inv.findProduct(1), " product id 1 should not exist but was found");
    }*/

    @Test
    void getItemsInStock()  {
        assertEquals(1, inv.getItemsInStock().size(), "expected 2 item listings");
        try {
            inv.setAmount(1,0);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }

        assertEquals(0, inv.getItemsInStock().size());
        try {
            inv.setAmount(2, 1);
            inv.setAmount(1, 1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }
        assertEquals(2, inv.getItemsInStock().size());
    }

    @Test
    void getItemsInStockThreads(){
        final int[] counter = {0,0};
        assertEquals(1, inv.getItemsInStock().size(), "expected 2 item listings");
        try {
            inv.setAmount(1,0);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }

        assertEquals(0, inv.getItemsInStock().size());
        try {
            inv.setAmount(2, 1);
            inv.setAmount(1, 1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("product info legal, should have succeeded");
            return;
        }
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for(int i =0;i<2;i++){
            int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                   counter[finalI] += inv.getItemsInStock().size();
                }
            };
            pool.execute(r);
        }


        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        for(int i=0;i<2;i++) {
            assertTrue(counter[i] == 2);
        }
    }

    @Test
    void reduceAmount(){
        int currQuan;
        try {
            currQuan = inv.getQuantity(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }


        try {
            inv.reduceAmount(1,5);
        } catch (ProductNotFoundException productNotFoundException) {
            fail("product should exist");
            return;
        }

        int newQuan;
        try {
            newQuan = inv.getQuantity(1);
        }catch (ProductNotFoundException productNotFoundException){
            fail("product should exist");
            return;
        }
        assertEquals(currQuan - 5, newQuan, "should have had 5 less in new quantity");
    }

    @Test
    void reservedItem(){
        Map<Integer, Integer> map = new HashMap<>();

        int p1QuanOld, p2QuanOld;
        try {
            p1QuanOld = inv.getQuantity(1);
            p2QuanOld = inv.getQuantity(2);
        }catch (ProductNotFoundException productNotFoundException){
            fail("products should exist");
            return;
        }

        map.put(2,3);
        assertFalse(inv.reserveItems(map));

        int p1QuanNew, p2QuanNew;
        try {
            p2QuanNew = inv.getQuantity(2);
        }catch (ProductNotFoundException productNotFoundException){
            fail("products should exist");
            return;
        }
        assertEquals(p2QuanOld, p2QuanNew, "not enough amount of item, amounts should have not been changed");
        map.remove(2);
        map.put(1, 3);
        assertTrue(inv.reserveItems(map));
        try {
            p1QuanNew = inv.getQuantity(1);
            p2QuanNew = inv.getQuantity(2);
        }catch (ProductNotFoundException productNotFoundException){
            fail("products should exist");
            return;
        }

        assertEquals(p1QuanOld - 3, p1QuanNew, "product 1 amount should have been 3 less");
        assertEquals(p2QuanOld, p2QuanNew, "p2 amount should have not been changed.");
    }

    @Test
    void restoreStock(){
        Map<Integer, Integer> map = new HashMap<>();

        int p1QuanOld, p2QuanOld;
        try {
            p1QuanOld = inv.getQuantity(1);
            p2QuanOld = inv.getQuantity(2);
        }catch (ProductNotFoundException productNotFoundException){
            fail("products should exist");
            return;
        }

        map.put(1,3);
        map.put(2, 3);

        int p1QuanNew, p2QuanNew;
        try {
            inv.restoreStock(map);
            p1QuanNew = inv.getQuantity(1);
            p2QuanNew = inv.getQuantity(2);
        }catch (ProductNotFoundException productNotFoundException){
            fail("products should exist");
            return;
        }catch (InvalidProductInfoException invalidProductInfoException){
            fail("restock information is legal, should have succeeded");
            return;
        }
        assertEquals(p1QuanOld + 3, p1QuanNew);
        assertEquals(p2QuanOld + 3, p2QuanNew);
    }


}