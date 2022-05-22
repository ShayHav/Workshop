package domain.user;

import domain.shop.Product;
import domain.shop.Shop;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CartTest {

    private Cart cart;
    private Shop shop;
    private User user;
    private Product product;

    @BeforeAll
    void setUp() {
        Cart cart = new Cart();
        user = new User("admin");
        shop = new Shop("shop",null,null,user,1);
        ShoppingBasket basket = new ShoppingBasket(shop);
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