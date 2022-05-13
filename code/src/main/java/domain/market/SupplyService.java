package domain.market;

import java.util.Map;

public interface SupplyService {
    boolean connect();
    boolean supply(String fullName, String address, Map<Integer,Integer> items);
}
