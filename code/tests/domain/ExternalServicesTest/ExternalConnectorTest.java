package domain.ExternalServicesTest;

import domain.Exceptions.BlankDataExc;
import domain.ExternalConnectors.ExternalConnector;
import domain.ExternalConnectors.PaymentService;
import domain.ExternalConnectors.SupplyService;
import domain.user.TransactionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalConnectorTest {

    private ExternalConnector connector;
    TransactionInfo ti = new TransactionInfo("1", "yosi", "", "","","", LocalDate.now(),0.0);
    TransactionInfo ti1 = new TransactionInfo("2", "shmuel", "", "", "", "",LocalDate.now(), 0.0);


    @BeforeEach
    void setUp() {
        connector = new ExternalConnector();
    }

    @Test
    void connectToPaymentService() throws BlankDataExc {
        PaymentService ps = mock(PaymentService.class);
        when(ps.connect()).thenReturn(false);
        assertFalse(connector.setPaymentService(ps));
        PaymentService ps1 = mock(PaymentService.class);
        when(ps1.connect()).thenReturn(true);
        assertTrue(connector.setPaymentService(ps1));
    }

    @Test
    void setSupplyService() {
        SupplyService ss = mock(SupplyService.class);
        when(ss.connect()).thenReturn(false);
        SupplyService ss2 = mock(SupplyService.class);
        when(ss2.connect()).thenReturn(true);
        try {
            assertFalse(connector.setSupplyService(ss));
            assertTrue(connector.setSupplyService(ss2));
        }catch (Exception e) {
            fail();
        }
    }

    @Test
    void pay() {
        PaymentService p1 = mock(PaymentService.class);

        when(p1.connect()).thenReturn(true);

        when(p1.processPayment(ti.getFullName(),ti.getUserID(),ti.getCardNumber(), ti.getExpirationDate(), ti.getTotalAmount())).thenReturn(1000);
        when(p1.processPayment(ti1.getFullName(),ti1.getUserID(),ti1.getCardNumber(), ti1.getExpirationDate(), ti.getTotalAmount())).thenReturn(-1);


        try {
            assertTrue(connector.setPaymentService(p1));
            assertTrue(connector.pay(ti));
            assertFalse(connector.pay(ti1));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void supply() {

        SupplyService s1 = mock(SupplyService.class);
        HashMap<Integer, Integer> items = new HashMap<>();
        when(s1.connect()).thenReturn(true);
        when(s1.supply(ti.getFullName(),ti.getAddress(), items)).thenReturn(1000);
        when(s1.supply(ti1.getFullName(), ti1.getAddress(), items)).thenReturn(-1);

        SupplyService s2 = mock(SupplyService.class);
        when(s2.supply(ti.getFullName(),ti.getAddress(), items)).thenReturn(-1);
        when(s2.supply(ti1.getFullName(), ti1.getAddress(), items)).thenReturn(-1);
        when(s2.connect()).thenReturn(true);

        try {
            connector.setSupplyService(s1);
            assertTrue(connector.supply(ti, items));
            connector.setSupplyService(s2);
            assertFalse(connector.supply(ti1, items));
        } catch (BlankDataExc e) {
            fail();
        }
    }

    @Test
    void supplyWithInfiniteLoop(){
        SupplyService service = mock(SupplyService.class);
        when(service.connect()).thenReturn(true);
        HashMap<Integer, Integer> items = new HashMap<>();
        when(service.supply(ti.getFullName(), ti.getAddress(),items )).then(invocationOnMock -> {
            while(true);
        });
        when(service.cancelSupply(1)).then(invocationOnMock -> {
            while(true);
        });
        try{
            assertTrue(connector.setSupplyService(service));
            assertFalse(connector.supply(ti, items));
            assertFalse(connector.cancelSupply(1));
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void paymentWithInfiniteLoop(){
        PaymentService service = mock(PaymentService.class);
        when(service.connect()).thenReturn(true);
        when(service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(), ti.getExpirationDate(), ti.getTotalAmount())).then(invocationOnMock -> {
            while (true);
        });
        when(service.cancelPayment(1)).then(invocationOnMock -> {
            while (true);
        });
        try {
            assertTrue(connector.setPaymentService(service));
            assertFalse(connector.pay(ti));
            assertFalse(connector.cancelPayment(1));
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void paymentLoseConnection(){
        boolean[] connect = {true};
        PaymentService service = mock(PaymentService.class);
        when(service.connect()).then(invocationOnMock -> {
            boolean current = connect[0];
            connect[0] = !current;
            return current;
        });
        when(service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(), ti.getExpirationDate(), ti.getTotalAmount())).thenReturn(1000);
        when(service.cancelPayment(1)).thenReturn(true);

        try{
            assertTrue(connector.setPaymentService(service));
            assertFalse(connector.pay(ti));
            assertTrue(connector.pay(ti));
        }
        catch (Exception e){
            fail();
        }
    }
}