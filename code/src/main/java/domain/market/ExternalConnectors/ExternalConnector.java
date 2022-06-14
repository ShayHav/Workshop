package domain.market.ExternalConnectors;

import domain.user.TransactionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExternalConnector {
    private final List<PaymentService> paymentServices;
    private final List<SupplyService> supplyServices;

    public ExternalConnector(){
        paymentServices = new ArrayList<>();
        supplyServices = new ArrayList<>();
    }

    public boolean connectToPaymentService(PaymentService service) {
        if (service.connect()) {
            synchronized (this) {
                paymentServices.add(service);
            }
            return true;
        }
        return false;
    }

    public boolean connectToSupplyService(SupplyService service) {
        if (service != null && service.connect()) {
            synchronized (this) {
                supplyServices.add(service);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean removePaymentService(PaymentService service){
        return paymentServices.remove(service);
    }

    public synchronized boolean removeSupplyService(SupplyService service){
        return supplyServices.remove(service);
    }

    public synchronized boolean pay(TransactionInfo ti){
        for(PaymentService service: paymentServices){
            int processed = service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpirationDate(), ti.getTotalAmount());
            if(processed > 0)
                return true;
        }
        return false;
    }

    public synchronized boolean supply(TransactionInfo ti, Map<Integer,Integer> products){
        for(SupplyService service: supplyServices){
            int processed = service.supply(ti.getFullName(), ti.getAddress(), products);
            if(processed > 0)
                return true;
        }
        return false;
    }

    public boolean cancelSupply(int transactionID){
        for (SupplyService service: supplyServices){
            if(service.cancelSupply(transactionID)){
                return true;
            }
        }
        return false;
    }

    public boolean cancelPayment(int transactionID){
        for(PaymentService service: paymentServices){
            if(service.cancelPayment(transactionID))
                return true;
        }
        return false;
    }

}
