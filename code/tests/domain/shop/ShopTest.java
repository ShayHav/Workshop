package domain.shop;

import domain.Exceptions.*;
import domain.market.ExternalConnector;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ShopTest {
    Inventory inventory;
    DiscountPolicy discountPolicy;
    PurchasePolicy purchasePolicy;
    OrderHistory orders;
    User davidos;
    Shop shop;
    int appleID;
    int orangeID;
    Product p1;
    Product p2;

    @BeforeEach
    void setUp() throws IncorrectIdentification, BlankDataExc {
        ExternalConnector ec = mock(ExternalConnector.class);


    }

    private User setDavidos(){
        User davidos = mock(User.class);
        when(davidos.getUserName() == "Davidos");

        return davidos;
    }

    private void setDavidosShop(){
        User davidos = setDavidos();
        shop = new Shop("David's","food shop", discountPolicy, purchasePolicy, davidos,1);
        discountPolicy = mock(DiscountPolicy.class);
        purchasePolicy = mock(PurchasePolicy.class);
        Product p1 = mock(ProductImp.class);
        Product p2 = mock(ProductImp.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(discountPolicy.calcPricePerProduct(1, 5.0, 3)).thenReturn(4.0);
        when(discountPolicy.calcPricePerProduct(2, 12.0, 7)).thenReturn(8.0);
        shop.setDiscountPolicy(discountPolicy);
        shop.setPurchasePolicy(purchasePolicy);
    }

    @Test
    void addAndGetProduct() throws ProductNotFoundException {
        Product apple;
        try {
            //int serialNumber, String productName, String productDesc, String productCategory, double price, int quantity,String userId
            apple = shop.addListing(001, "red apple", "fruits", "food", 50.0, 5,"Davidos");
        }catch (InvalidAuthorizationException InvAuthExc){
            fail("founder can add product");
            return;
        }catch (InvalidProductInfoException InvProdInfoExc){
            fail("product info is legal");
            return;
        }
        assertTrue(shop.isProductIsAvailable(apple.getId(),0));
        assertEquals(shop.getProduct(apple.getId()).getId(), apple.getId());
    }




    @Test
    void getProduct() throws ProductNotFoundException {
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
