package domain;

import java.util.List;
import java.util.Map;

public class ResponseMap<T1,T2> extends Response {
    private Map<T1,T2> value;

    public ResponseMap(String msg){
        super(msg);
    }

    public ResponseMap(Map<T1,T2> value){
        super();
        this.value = value;
    }

    public ResponseMap(Map<T1,T2> value , String msg){
        super(msg);
        this.value = value;
    }

    public Map<T1,T2>getValue() {
        return value;
    }
}
