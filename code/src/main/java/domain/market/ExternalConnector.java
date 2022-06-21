package domain.market;

import domain.user.TransactionInfo;

import javax.persistence.Entity;
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
            boolean processed = service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpirationDate(), ti.getTotalAmount());
            if(processed)
                return true;
        }
        return false;
    }

    public synchronized boolean supply(TransactionInfo ti, Map<Integer,Integer> products){
        for(SupplyService service: supplyServices){
            boolean processed = service.supply(ti.getFullName(), ti.getAddress(), products);
            if(processed)
                return true;
        }
        return false;
    }

}
