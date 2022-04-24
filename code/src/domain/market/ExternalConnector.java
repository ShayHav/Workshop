package domain.market;

import domain.shop.TransactionInfo;
import domain.user.TransactionInfo;

import java.util.ArrayList;
import java.util.List;

public class ExternalConnector {
    private final List<PaymentService> paymentServices;
    private final List<SupplyService> supplyServices;

    public ExternalConnector(){
        paymentServices = new ArrayList<>();
        supplyServices = new ArrayList<>();
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

    public boolean pay(TransactionInfo ti){
        for(PaymentService service: paymentServices){
            boolean processed = service.processPayment(ti.getFullName(), ti.getUserID(), ti.getCardNumber(),
                    ti.getExpiredDate(), ti.getTotalAmount());
            if(processed)
                return true;
        }
        return false;
    }

    public boolean supply(TransactionInfo ti){
        for(SupplyService service: supplyServices){
            boolean processed = service.supply(ti.getFullName(), ti.getAddress(), ti.getItems());
            if(processed)
                return true;
        }
        return false;
    }

}
