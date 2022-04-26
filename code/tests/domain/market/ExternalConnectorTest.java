package domain.market;

import domain.user.TransactionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExternalConnectorTest {

    private ExternalConnector connector;
    TransactionInfo ti = new TransactionInfo("1", "yosi", "", "","","", LocalDate.now(),0.0);
    TransactionInfo ti1 = new TransactionInfo("2", "shmuel", "", "", "", "",LocalDate.now(), 0.0);


    @BeforeEach
    void setUp() {
        connector = new ExternalConnector();
    }

    @Test
    void connectToPaymentService() {
        PaymentService ps = mock(PaymentService.class);
        when(ps.connect()).thenReturn(false);
        assertFalse(connector.connectToPaymentService(ps));
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        assertTrue(connector.connectToPaymentService(ps1));
    }

    @Test
    void connectToSupplyService() {
        SupplyService ss = mock(SupplyService.class);
        when(ss.connect()).thenReturn(false);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        assertFalse(connector.connectToSupplyService(ss));
        assertTrue(connector.connectToSupplyService(ss2));
    }

    @Test
    void pay() {
        PaymentService p1 = mock(PaymentService.class);
        PaymentService p2 = mock(PaymentService.class);
        when(p1.connect()).thenReturn(true);
        when(p2.connect()).thenReturn(true);
        when(p1.processPayment(ti.getFullName(),ti.getUserID(),ti.getCardNumber(), ti.getExpirationDate(), 1.0)).thenReturn(true);
        when(p1.processPayment(ti1.getFullName(),ti1.getUserID(),ti1.getCardNumber(), ti1.getExpirationDate(), 1.0)).thenReturn(false);

        when(p2.processPayment(ti.getFullName(),ti.getUserID(),ti.getCardNumber(), ti.getExpirationDate(), 1.0)).thenReturn(false);
        when(p2.processPayment(ti1.getFullName(),ti1.getUserID(),ti1.getCardNumber(), ti1.getExpirationDate(), 1.0)).thenReturn(false);

        assertTrue(connector.pay(ti));
        assertFalse(connector.pay(ti1));
    }

    @Test
    void supply() {

        SupplyService s1 = mock(SupplyService.class);
        HashMap<Integer, Integer> items = new HashMap<>();
        when(s1.connect()).thenReturn(true);
        when(s1.supply(ti.getFullName(),ti.getAddress(), items)).thenReturn(true);
        when(s1.supply(ti1.getFullName(), ti1.getAddress(), items)).thenReturn(false);

        SupplyService s2 = mock(SupplyService.class);
        when(s2.supply(ti.getFullName(),ti.getAddress(), items)).thenReturn(false);
        when(s2.supply(ti1.getFullName(), ti1.getAddress(), items)).thenReturn(false);
        when(s2.connect()).thenReturn(true);

        connector.connectToSupplyService(s1);
        connector.connectToSupplyService(s2);

        assertTrue(connector.supply(ti, items));
        assertFalse(connector.supply(ti1, items));
    }
}