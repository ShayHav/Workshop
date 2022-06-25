package domain.ExternalConnectors;

import domain.Exceptions.BlankDataExc;
import domain.user.TransactionInfo;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.*;

public class ExternalConnector {

    private final int MAX_REQUEST_TIME = 15; // in seconds

    private PaymentService paymentService;
    private SupplyService supplyService;

    public ExternalConnector(){
    }

    public boolean setPaymentService(PaymentService service) throws BlankDataExc, ConnectException {
        if(service == null){
            throw new BlankDataExc("payment service to set is null");
        }
        if (connectToPaymentService(service)) {
            paymentService = service;
            return true;
        }
        return false;
    }

    public boolean setSupplyService(SupplyService service) throws BlankDataExc, ConnectException {
        if(service == null){
            throw new BlankDataExc("supply service to set is null");
        }
        if (connectToSupplyService(service)) {
            supplyService = service;
            return true;
        }
        return false;
    }

    /**
     * call to the external payment service to process a payment from the market
     * if the process exceed 15 seconds it will terminate and return false
     * @param ti an object that contain all the information to process the payment
     * @return true if the payment was processed and false otherwise
     * @throws ConnectException if fail to open connection or didn't get an answer within 15 seconds
     */
    public synchronized int pay(TransactionInfo ti) throws ConnectException {
        boolean connected = connectToPaymentService(this.paymentService);
        if(!connected)
            throw new ConnectException("no connection to external service");

        return submit(() ->
                paymentService.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpirationDate(), ti.getCcv(), ti.getTotalAmount()));
    }

    /**
     * call the external supply service to process a delivery from the market
     * if the process exceed 15 seconds it will terminate and return false
     * @param ti all the information about the delivery
     * @param products the prodcuts to deliver
     * @return true if a delivery was created and false otherwise
     * @throws ConnectException if failed to get an answer withing 15 seconds or failed to open connection
     */
    public synchronized int supply(TransactionInfo ti, Map<Integer,Integer> products) throws ConnectException {
        boolean connected = connectToSupplyService(this.supplyService);
        if(!connected)
            throw new ConnectException("failed to connect to external supply service");
        return submit(() -> supplyService.supply(ti.getFullName(), ti.getAddress(), ti.getCity(),
                ti.getCountry(), ti.getZip(), products));
    }

    /**
     * call to the external supply service to cancel the delivery with transactionID
     * if the method execution take more than 15 second it will end and return false
     * @param transactionID number to identify previously successful supply request
     * @return true if the supply request was canceled in less than 15 second
     */
    public boolean cancelSupply(int transactionID) throws ConnectException {
        Boolean succeed = submit(() -> supplyService.cancelSupply(transactionID));
        return succeed != null && succeed;
    }

    /**
     * call to the external payment service to cancel the payment with a matching transactionID
     * if the method's execution take more than 15 seconds it will end and return false
     * @param transactionID
     * @return
     */
    public boolean cancelPayment(int transactionID) throws ConnectException {
        Boolean succeed = submit(()-> paymentService.cancelPayment(transactionID));
        return succeed != null && succeed;
    }

    /**
     * call the external service to execute a request in less than MAX_REQUEST_TIME seconds
     * @param callback the request to be done
     * @param <T> the answer type we expect to return i.e. boolean or integer
     * @return the answer from the external service if it took less than MAX_REQUEST_TIME second otherwise null
     */
    private <T> T submit(Callable<T> callback) throws ConnectException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callback);
        try{
            return future.get(MAX_REQUEST_TIME, TimeUnit.SECONDS);
        }
        catch (TimeoutException | InterruptedException | ExecutionException timeout){
            future.cancel(true);
            throw new ConnectException("failed to receive answer from external service");
        }
        finally {
            executor.shutdownNow();
        }
    }

    /**
     * try to connect to supply service meaning perform and handshake with the service
     * @param service external supply service
     * @return true if the service return that connection started
     */
    private boolean connectToSupplyService(SupplyService service) throws ConnectException {
        Boolean connected = submit(service::connect);
        return connected != null && connected;
    }

    /**
     * try to connect to payment service meaning perform and handshake with the service
     * @param service external payment service
     * @return true if the service return that connection started
     */
    private boolean connectToPaymentService(PaymentService service) throws ConnectException {
        Boolean connected = submit(service::connect);
        return connected != null && connected;
    }
}
