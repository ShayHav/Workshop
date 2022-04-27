package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.market.ExternalConnector;
import domain.market.MarketSystem;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.Discount;
import domain.shop.discount.DiscountPolicy;
import domain.user.TransactionInfo;
import domain.user.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ShopTest {
    Inventory inventory;
    DiscountPolicy discountPolicy;
    PurchasePolicy purchasePolicy;
    OrderHistory orders;
    Shop shop;
    ProductImp p1;
    ProductImp p2;

    @BeforeEach
    void setUp() {
        discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        inventory = mock(Inventory.class);
        shop = new Shop("David's", discountPolicy, purchasePolicy, "Davidos", 1);
        Product p1 = mock(ProductImp.class);
        Product p2 = mock(ProductImp.class);
        ExternalConnector ec = mock(ExternalConnector.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(inventory.findProduct(1)).thenReturn(p1);
        /*when(ec.pay()).thenReturn(true);*/
        when(inventory.findProduct(2)).thenReturn(p2);
        when(inventory.getPrice(1)).thenReturn(5.0);
        when(inventory.getPrice(2)).thenReturn(12.0);
        when(discountPolicy.calcPricePerProduct(1, 5.0, 3)).thenReturn(4.0);
        when(discountPolicy.calcPricePerProduct(2, 12.0, 7)).thenReturn(8.0);
        when(inventory.getPrice(2)).thenReturn(12.0);
        shop.setInventory(inventory);
        shop.setDiscountPolicy(discountPolicy);
        shop.setPurchasePolicy(purchasePolicy);
    }




    @Test
    void getProduct(){
        assertEquals(shop.getProduct(1), p1, "product p1 should have been returned");
        assertEquals(shop.getProduct(2), p2, "product p1 should have been returned");
        assertThrows(NullPointerException.class, ()->shop.getProduct(3), "item should not exist");
    }



    @Test
    void productPriceAfterDiscounts(){
        assertEquals(shop.productPriceAfterDiscounts(1, 3), 4.0, "product p1 price after discounts should have been 4");
        assertEquals(shop.productPriceAfterDiscounts(2, 7), 8.0, "product p2 price after discounts should have been 8");
        assertEquals(shop.productPriceAfterDiscounts(3, 4), 0.0, "product p3 doesn't not exist and should have returned price 0");
    }





    @Test
    void calculateTotalAmountOfOrder(){
        Map<Integer, Integer> product_Quantity = new HashMap<>();
        product_Quantity.put(1, 3);
        assertEquals(shop.calculateTotalAmountOfOrder(product_Quantity), 12.0, "product p1 price after discounts should have been 4");
        product_Quantity.put(2, 7);
        assertEquals(shop.calculateTotalAmountOfOrder(product_Quantity), 68.0, "product p2 price after discounts should have been 8");
        product_Quantity = new HashMap<>();
        product_Quantity.put(3, 3);
        assertEquals(shop.calculateTotalAmountOfOrder(product_Quantity), 0.0, "product p3 doesn't not exist and should have returned price 0");
    }




    /*public ResponseT<Order> checkOut(Map<Integer,Integer> products, double totalAmount, domain.user.TransactionInfo transaction){
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            //check purchase policy regarding the Product
            if (!purchasePolicyLegal(transaction.getUserID(), set.getKey(), inventory.getPrice(set.getKey()), set.getValue()))
                return new ResponseT("violates purchase policy");
        }
        synchronized (inventory) {
            if (!inventory.reserveItems(products)) {
                inventory.restoreStock(products);
                return new ResponseT<>("not in stock");
            }
        }


        //calculate price

        Map<Integer, Double> product_PricePer = new HashMap<>();
        double product_price_single;
        for(Map.Entry<Integer, Integer> set : products.entrySet()){
            product_price_single = productPriceAfterDiscounts(set.getKey(), set.getValue());
            product_PricePer.put(set.getKey(), product_price_single);
        }


        MarketSystem market = MarketSystem.getInstance();
        if(!market.pay(transaction)){
            inventory.restoreStock(products);
            return new ResponseT<>();
        }
        if(!market.supply(transaction, products)){
            return new ResponseT<>("problem with supply system");
        }
        // creating Order object to store in the Order History with unmutable copy of product
        Order o = createOrder(products, transaction, product_PricePer);
        return new ResponseT(o);
    }*/



}
