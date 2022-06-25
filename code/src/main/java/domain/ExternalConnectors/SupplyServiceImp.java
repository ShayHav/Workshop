package domain.ExternalConnectors;

import java.util.Map;

public class SupplyServiceImp implements SupplyService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public int supply(String fullName, String address, String city, String country, String zip, Map<Integer, Integer> items) {
        return 15000;
    }

    @Override
    public boolean cancelSupply(int transactionID) {
        return true;
    }
}
