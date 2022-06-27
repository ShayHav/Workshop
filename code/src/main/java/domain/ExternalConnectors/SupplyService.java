package domain.ExternalConnectors;

import java.util.Map;

public interface SupplyService {
    boolean connect();
    int supply(String fullName, String address, String city, String country, String zip, Map<Integer,Integer> items);
    boolean cancelSupply(int transactionID);
}
