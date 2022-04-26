package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderHistoryTest {

    OrderHistory myHistory;
    ProductImp p = new ProductImp(1, "TestProduct", "", "Testing");


    List<Product> getProducts(){
        List<Product> lst = new ArrayList<>();
        lst.add(new ProductHistory(p, 1.0, 3));
        return lst;
    }

    @BeforeEach
    void setUp() {
        myHistory = new OrderHistory();
        Order o = new Order(getProducts(),3.0, "1");
    }

    @Test
    void searchByDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1), tomorrow = LocalDate.now().plusDays(1);
        p.setName("NewName");
        List<Order> orders = myHistory.searchByDate(yesterday, tomorrow);
        assertEquals(orders.size(), 1);
        assertEquals(orders.get(0).getBroughtItem().get(0).getName(),"TestProduct" );
    }

    @Test
    void searchByUser() {
        List<Order> orders = myHistory.searchByUser("12");
        assertEquals(orders.size(), 0);
        orders = myHistory.searchByUser("1");
        assertEquals(orders.size(), 1);
    }

    @Test
    void getOrder() {
        Order toAdd = new Order(getProducts(),5, "2");
        Order o = myHistory.getOrder(5);
        assertNull(o);
        o = myHistory.getOrder(1);
        assertEquals(o, toAdd);
    }
}