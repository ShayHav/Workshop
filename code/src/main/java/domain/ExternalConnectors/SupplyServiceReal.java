package domain.ExternalConnectors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SupplyServiceReal extends ExternalService implements SupplyService{


    public SupplyServiceReal(String url){
        super(url);
    }


    @Override
    public int supply(String fullName, String address, String city, String country, String zip, Map<Integer, Integer> items) {
        try{
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "supply");
            params.put("name ", fullName);
            params.put("address", address);
            params.put("city", city);
            params.put("country", country);
            params.put("zip", zip);
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
    public boolean cancelSupply(int transactionID) {
        return cancelAction("cancel_supply", transactionID);
    }
}
