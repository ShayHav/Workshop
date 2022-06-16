package domain.market.ExternalConnectors;

import domain.Exceptions.BlankDataExc;
import domain.user.TransactionInfo;

import java.util.Map;
import java.util.concurrent.*;

public class ExternalConnector {

    private final int MAX_REQUEST_TIME = 15; // in seconds

    private PaymentService paymentService;
    private SupplyService supplyService;

    public ExternalConnector(){
    }

    public boolean setPaymentService(PaymentService service) throws BlankDataExc {
        if(service == null){
            throw new BlankDataExc("payment service to set is null");
        }
        if (connectToPaymentService(service)) {
            paymentService = service;
            return true;
        }
        return false;
    }

    public boolean setSupplyService(SupplyService service) throws BlankDataExc {
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
     */
    public synchronized boolean pay(TransactionInfo ti){
        Integer transactionID = submit(() ->
                paymentService.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpirationDate(), ti.getTotalAmount()));
        return transactionID != null && transactionID >= 0;
    }

    /**
     * call the external supply service to process a delivery from the market
     * if the process exceed 15 seconds it will terminate and return false
     * @param ti all the information about the delivery
     * @param products the prodcuts to deliver
     * @return true if a delivery was created and false otherwise
     */
    public synchronized boolean supply(TransactionInfo ti, Map<Integer,Integer> products){
        Integer transactionID = submit(() -> supplyService.supply(ti.getFullName(), ti.getAddress(), products));
        return transactionID != null && transactionID >= 0;
    }

    /**
     * call to the external supply service to cancel the delivery with transactionID
     * if the method execution take more than 15 second it will end and return false
     * @param transactionID number to identify previously successful supply request
     * @return true if the supply request was canceled in less than 15 second
     */
    public boolean cancelSupply(int transactionID){
        Boolean succeed = submit(() -> supplyService.cancelSupply(transactionID));
        return succeed != null && succeed;
    }

    /**
     * call to the external payment service to cancel the payment with a matching transactionID
     * if the method's execution take more than 15 seconds it will end and return false
     * @param transactionID
     * @return
     */
    public boolean cancelPayment(int transactionID){
        Boolean succeed = submit(()-> paymentService.cancelPayment(transactionID));
        return succeed != null && succeed;
    }

    /**
     * call the external service to execute a request in less than MAX_REQUEST_TIME seconds
     * @param callback the request to be done
     * @param <T> the answer type we expect to return i.e. boolean or integer
     * @return the answer from the external service if it took less than MAX_REQUEST_TIME second otherwise null
     */
    private <T> T submit(Callable<T> callback){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callback);
        try{
            return future.get(MAX_REQUEST_TIME, TimeUnit.SECONDS);
        }
        catch (TimeoutException | InterruptedException | ExecutionException timeout){
            future.cancel(true);
            return null;
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
    private boolean connectToSupplyService(SupplyService service){
        Boolean connected = submit(service::connect);
        return connected != null && connected;
    }

    /**
     * try to connect to payment service meaning perform and handshake with the service
     * @param service external payment service
     * @return true if the service return that connection started
     */
    private boolean connectToPaymentService(PaymentService service){
        Boolean connected = submit(service::connect);
        return connected != null && connected;
    }
}
