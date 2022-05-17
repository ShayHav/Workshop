package domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartTest {

    private Cart cart;

    @BeforeAll
    void setUp() {
        Cart cart = new Cart();
        ShoppingBasket basket = mock(ShoppingBasket.class);
    }

    @Test
    void addProductToCart() {
    }

    @Test
    void updateAmountOfProduct() {
    }

    @Test
    void removeProductFromCart() {
    }

    @Test
    void getTotalAmount() {
    }

    @Test
    void showCart() {
    }

    @Test
    void checkout() {
    }
}