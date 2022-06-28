package domain.user;

import java.util.HashMap;
import java.util.Map;

public class SecurePasswordInt {
    private Map<String,UserInfo> map;

    public SecurePasswordInt()
    {
        map = new HashMap<>();
    }

    public Map<String, UserInfo> getMap() {
        return map;
    }

    public void setMap(Map<String, UserInfo> map) {
        this.map = map;
    }
}
