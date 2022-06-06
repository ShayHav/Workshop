package domain.user;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.Exceptions.*;
import domain.market.MarketSystem;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.ProductInfo;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.shop.discount.DiscountPolicy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.server.ServerCloneException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingBasketTest {


    ShoppingBasket basket;
    User user;
    Shop shop;
    MarketSystem marketSystem = MarketSystem.getInstance();
    UserController userController = UserController.getInstance();
    ShopController shopController = ShopController.getInstance();



    @Test
    void addProductToBasket() throws IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc, InvalidAuthorizationException, ShopNotFoundException {
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"admin","admin");
        marketSystem.register("nitay","Password");
        marketSystem.logIn("nitay","Password",null);
        marketSystem.createShop("book shop","nitay's",new DiscountPolicy(),new PurchasePolicy(),"nitay");
        marketSystem.AddProductToShopInventory(1,"Dune","Best Thing ever","books",90.0,50,"nitay",1);
        basket = new ShoppingBasket(marketSystem.getShop(1));
        try{
            basket.addProductToBasket(5, -1);
        }
        catch (IllegalArgumentException illegalArgumentException){
            assertTrue(false);
        }
        catch (ProductNotFoundException productNotFoundException) {
            assertTrue(true);
        }
        try {
            basket.addProductToBasket(5, 0);
        }
        catch (IllegalArgumentException illegalArgumentException){
            assertTrue(false);
        }
        catch (ProductNotFoundException productNotFoundException) {
            assertTrue(true);
        }
        assertFalse(basket.getProductAmountList().containsKey(5));
    }

    @Test
    void calculateTotalAmount() throws InvalidSequenceOperationsExc, IncorrectIdentification, ShopNotFoundException, BlankDataExc, InvalidAuthorizationException {
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"admin","admin");
        marketSystem.register("nitay","Password");
        marketSystem.logIn("nitay","Password",null);
        marketSystem.createShop("book shop","nitay's",new DiscountPolicy(),new PurchasePolicy(),"nitay");
        marketSystem.AddProductToShopInventory(1,"Dune","Best Thing ever","books",90.0,50,"nitay",1);
        basket = new ShoppingBasket(marketSystem.getShop(1));
        Map<Integer,Integer> productsWithAmount = basket.getProductAmountList();
        assertEquals(basket.calculateTotalAmount(), 0);
        productsWithAmount.put(1, 2);
        assertEquals(basket.calculateTotalAmount(), 180.0);
    }

    @Test
    void updateAmount() throws ProductNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"admin","admin");
        marketSystem.register("nitay","Password");
        marketSystem.logIn("nitay","Password",null);
        marketSystem.createShop("book shop","nitay's",new DiscountPolicy(),new PurchasePolicy(),"nitay");
        marketSystem.AddProductToShopInventory(1,"Dune","Best Thing ever","books",90.0,50,"nitay",1);
        basket = new ShoppingBasket(marketSystem.getShop(1));
        basket.addProductToBasket(1,1);
        try {
            basket.updateAmount(1, -1);
        }
        catch (IllegalArgumentException illegalArgumentException){
            assertTrue(true);
        }
        assertTrue(basket.getProductAmountList().containsKey(1));
        basket.updateAmount(1, 20);
        assertTrue(basket.getProductAmountList().containsKey(1));
        assertTrue(basket.getProductAmountList().get(1).equals(20));
        assertFalse(basket.getProductAmountList().containsKey(2));
    }

    @Test
    void removeProduct() throws ProductNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"admin","admin");
        marketSystem.register("nitay","Password");
        marketSystem.logIn("nitay","Password",null);
        marketSystem.createShop("book shop","nitay's",new DiscountPolicy(),new PurchasePolicy(),"nitay");
        marketSystem.AddProductToShopInventory(1,"Dune","Best Thing ever","books",90.0,50,"nitay",1);
        basket = new ShoppingBasket(marketSystem.getShop(1));
        basket.addProductToBasket(1,1);
        basket.removeProduct(1);
        assertFalse(basket.getProductAmountList().containsKey(1));
    }


    @Test
    void showBasket() throws ProductNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"admin","admin");
        marketSystem.register("nitay","Password");
        marketSystem.logIn("nitay","Password",null);
        marketSystem.createShop("book shop","nitay's",new DiscountPolicy(),new PurchasePolicy(),"nitay");
        marketSystem.AddProductToShopInventory(1,"Dune","Best Thing ever","books",90.0,50,"nitay",1);
        basket = new ShoppingBasket(marketSystem.getShop(1));
        basket.addProductToBasket(1,1);
        ShoppingBasket.ServiceBasket b = basket.showBasket();
        assertTrue(b.getShopId()==1);
        assertTrue(b.getShopName().equals("nitay's"));
        assertTrue(b.getProductWithAmount().size()==1);
        assertFalse(b.getTotalAmount()==100);
    }
}