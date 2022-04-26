package domain.market;

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
        paymentServices.add(new PaymentServiceImp());
        supplyServices.add(new SupplyServiceImp());
    }

    public boolean connectToPaymentService(PaymentService service){
        if(service.connect()){
            paymentServices.add(service);
            return true;
        }
        return false;
    }

    public boolean connectToSupplyService(SupplyService service){
        if(service.connect()){
            supplyServices.add(service);
            return true;
        }
        return false;
    }

    public boolean removePaymentService(PaymentService service){
        return paymentServices.remove(service);
    }

    public boolean removeSupplyService(SupplyService service){
        return supplyServices.remove(service);
    }

    public boolean pay(TransactionInfo ti){
        for(PaymentService service: paymentServices){
            boolean processed = service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpirationDate(), ti.getTotalAmount());
            if(processed)
                return true;
        }
        return false;
    }

    public boolean supply(TransactionInfo ti, Map<Integer,Integer> products){
        for(SupplyService service: supplyServices){
            boolean processed = service.supply(ti.getFullName(), ti.getAddress(), products);
            if(processed)
                return true;
        }
        return false;
    }

}
