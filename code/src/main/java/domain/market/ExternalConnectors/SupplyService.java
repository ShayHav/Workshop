package domain.market.ExternalConnectors;

import java.util.Map;

public interface SupplyService {
    boolean connect();
    int supply(String fullName, String address, Map<Integer,Integer> items);
    boolean cancelSupply(int transactionID);
}
