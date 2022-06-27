package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderHistoryTest {
    OrderHistory myHistory;
    ProductImp p = new ProductImp(1, "TestProduct", "", "Testing", 100, 50);


    List<Product> getProducts(){
        List<Product> lst = new ArrayList<>();
        lst.add(new ProductHistory(p, 1.0, 3));
        return lst;
    }

    @BeforeEach
    void setUp() {
        myHistory = new OrderHistory();
        Order o = new Order(getProducts(),3.0, "1", 1, "davidos");
        myHistory.addOrder(o);
    }

    @Test
    void searchByDate() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1), tomorrow = LocalDateTime.now().plusDays(1);
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
    void searchByUserThreads() throws InterruptedException {
        final List<Order>[] final1 = new List[]{new LinkedList<>(),new LinkedList<>()};
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                final1[0] = myHistory.searchByUser("12");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                final1[1] = myHistory.searchByUser("1");
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(final1[0].size(), 0);
        assertEquals(final1[1].size(), 1);
    }


    @Test
    void getOrder() {
        Order toAdd = new Order(getProducts(),5, "2", 1, "davidos");
        myHistory.addOrder(toAdd);
        Order o = myHistory.getOrder(100);
        assertNull(o);
        o = myHistory.getOrder(toAdd.getOrderId());
        assertEquals(o.getOrderId(), toAdd.getOrderId());
    }

    @Test
    void getOrderThreads() throws InterruptedException {
        Order toAdd = new Order(getProducts(),5, "2", 1, "davidos");
        myHistory.addOrder(toAdd);
        Order[] finalO = {null,null};
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
               finalO[0] = myHistory.getOrder(100);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                finalO[1] = myHistory.getOrder(toAdd.getOrderId());
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertNull(finalO[0]);
        assertEquals(finalO[1].getOrderId(), toAdd.getOrderId());
    }

}