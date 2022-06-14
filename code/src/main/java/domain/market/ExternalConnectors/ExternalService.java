package domain.market.ExternalConnectors;

import java.io.IOException;
import java.util.Map;

public class ExternalService {

    protected final Client client;

    public ExternalService(String url){
        client = new Client(url);
    }

    public boolean connect() {
        try{
            String response = client.sendPost(Map.of("action_type", "handshake"));
            return response.equals("OK");
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean cancelAction(String actionType, int transactionID) {
        try{
            Map<String, String> params = Map.of("action_type", actionType,
                    "transaction_id ", "" + transactionID);
            int response = Integer.parseInt(client.sendPost(params));
            return response == 1;
        }
        catch (IOException | NumberFormatException e){
            return false;
        }
    }
}
