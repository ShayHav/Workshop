package domain.market.ExternalConnectors;

import java.util.Map;

public class SupplyServiceImp implements SupplyService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public int supply(String fullName, String address, Map<Integer, Integer> items) {
        return 1;
    }

    @Override
    public boolean cancelSupply(int transactionID) {
        return true;
    }
}
