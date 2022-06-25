package domain.ExternalServicesTest;

import domain.Exceptions.BlankDataExc;
import domain.ExternalConnectors.ExternalConnector;
import domain.ExternalConnectors.PaymentService;
import domain.ExternalConnectors.SupplyService;
import domain.user.TransactionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalConnectorTest {

    private ExternalConnector connector;
    TransactionInfo ti = new TransactionInfo("1", "yosi", "Street 1", "City","County","00001", "050123456", "123456",
            "01/25", "123", LocalDate.now(),0.0);
    TransactionInfo ti1 = new TransactionInfo("2", "shmuel", "Street2", "City", "Country", "000001",
            "0503456789", "1234567", "02/28", "123",LocalDate.now(), 0.0);
    int goodReturn = 10000, badReturn = -1;

    @BeforeEach
    void setUp() {
        connector = new ExternalConnector();
    }

    @Test
    void connectToPaymentService() throws BlankDataExc {
        PaymentService ps = mock(PaymentService.class);
        when(ps.connect()).thenReturn(false);
        try {
            assertFalse(connector.setPaymentService(ps));
            PaymentService ps1 = mock(PaymentService.class);
            when(ps1.connect()).thenReturn(true);
            assertTrue(connector.setPaymentService(ps1));
        } catch (ConnectException e) {
            fail();
        }
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

        when(p1.processPayment(ti.getFullName(),ti.getUserID(),ti.getCardNumber(), ti.getExpirationDate(),  ti.getCcv(), ti.getTotalAmount())).thenReturn(goodReturn);
        when(p1.processPayment(ti1.getFullName(),ti1.getUserID(),ti1.getCardNumber(), ti1.getExpirationDate(),ti1.getCcv(), ti1.getTotalAmount())).thenReturn(badReturn);

        try {
            assertTrue(connector.setPaymentService(p1));
            assertEquals(goodReturn,connector.pay(ti));
            assertEquals(badReturn,connector.pay(ti1));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void supply() {

        SupplyService s1 = mock(SupplyService.class);
        HashMap<Integer, Integer> items = new HashMap<>();
        when(s1.connect()).thenReturn(true);
        when(s1.supply(ti.getFullName(),ti.getAddress(), ti.getCity(), ti.getCountry(), ti.getZip(), items)).thenReturn(goodReturn);
        when(s1.supply(ti1.getFullName(), ti1.getAddress(), ti1.getCity(), ti1.getCountry(), ti1.getZip(), items)).thenReturn(-1);

        SupplyService s2 = mock(SupplyService.class);
        when(s2.supply(ti.getFullName(),ti.getAddress(),ti.getCity(), ti.getCountry(), ti.getZip(), items)).thenReturn(-1);
        when(s2.supply(ti1.getFullName(), ti1.getAddress(),ti1.getCity(), ti1.getCountry(), ti1.getZip(), items)).thenReturn(-1);
        when(s2.connect()).thenReturn(true);

        try {
            connector.setSupplyService(s1);
            assertEquals(goodReturn, connector.supply(ti, items));
            connector.setSupplyService(s2);
            assertEquals(badReturn, connector.supply(ti1, items));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void supplyWithInfiniteLoop(){
        SupplyService service = mock(SupplyService.class);
        when(service.connect()).thenReturn(true);
        HashMap<Integer, Integer> items = new HashMap<>();
        when(service.supply(ti.getFullName(), ti.getAddress(),ti.getCity(), ti.getCountry(), ti.getZip(), items )).then(invocationOnMock -> {
            while(true);
        });
        when(service.cancelSupply(1)).then(invocationOnMock -> {
            while(true);
        });
        try{
            assertTrue(connector.setSupplyService(service));
            assertThrows(ConnectException.class, () -> connector.supply(ti, items));
            assertThrows(ConnectException.class, () -> connector.cancelSupply(1));
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void paymentWithInfiniteLoop(){
        PaymentService service = mock(PaymentService.class);
        when(service.connect()).thenReturn(true);
        when(service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(), ti.getExpirationDate(), ti.getCcv(), ti.getTotalAmount())).then(invocationOnMock -> {
            while (true);
        });
        when(service.cancelPayment(1)).then(invocationOnMock -> {
            while (true);
        });
        try {
            assertTrue(connector.setPaymentService(service));
            assertThrows(ConnectException.class, () -> connector.pay(ti));
            assertThrows(ConnectException.class, () -> connector.cancelPayment(1));
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
        when(service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(), ti.getExpirationDate(),ti.getCcv(), ti.getTotalAmount())).thenReturn(goodReturn);

        try{
            assertTrue(connector.setPaymentService(service));
            assertThrows(ConnectException.class, () -> connector.pay(ti));
            assertEquals(goodReturn, connector.pay(ti));
        }
        catch (Exception e){
            fail();
        }
    }
}