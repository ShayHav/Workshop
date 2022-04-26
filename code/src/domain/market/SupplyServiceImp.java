package domain.market;

import java.util.Map;

public class SupplyServiceImp implements SupplyService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public boolean supply(String fullName, String address, Map<Integer, Integer> items) {
        return true;
    }
}
