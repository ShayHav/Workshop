package domain.user;

import domain.shop.Shop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;

class ShoppingBasketTest {

    ShoppingBasket basket;

    @BeforeEach
    void setUp(){
        Shop s = mock(Shop.class);
        basket = new ShoppingBasket(s);
        Map<Integer,Integer> productsWithAmount = basket.getProductAmountList();
        productsWithAmount.put(1,100);
    }


    @Test
    void addProductToBasket() {
       assertFalse(basket.addProductToBasket(5, -1));
       assertFalse(basket.addProductToBasket(5, 0));
       assertTrue(basket.addProductToBasket(5, 50));
        assert(basket.getProductAmountList().get(1) == 50);
       assertTrue(basket.addProductToBasket(1,50));
       assert(basket.getProductAmountList().get(1) == 150);
    }

    @Test
    void calculateTotalAmount(){

    }


    @Test
    void updateAmount() {

    }

    @Test
    void removeProduct() {
    }


    @Test
    void showBasket() {
    }
}