package domain.market.ExternalConnectors;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RealPaymentSystem extends ExternalService implements PaymentService{

    public RealPaymentSystem(String url){
        super(url);
    }

    @Override
    public int processPayment(String fullName, String id, String creditCard, String expiredDate, double total) {
        try{
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "pay");
            params.put("card_number", creditCard);
            params.put("holder", fullName);
            params.put("id", id);
            String[] splitExpiredDate = expiredDate.split("/");
            params.put("month", splitExpiredDate[0]);
            params.put("year", splitExpiredDate[1]);
            params.put("ccv", ""); //TODO send ccv
            String response = client.sendPost(params);
            int transactionID =  Integer.parseInt(response);
            if(10000 <= transactionID && transactionID <= 100000)
                return transactionID;
            return -1;
        }
        catch (IOException | NumberFormatException e){
            return -1;
        }
    }

    @Override
    public boolean cancelPayment(int transactionID) {
        return cancelAction("cancel_pay", transactionID);
    }
}
