package domain;

import java.util.List;

public class ResponseList<T> extends Response {
    private List<T> value;

    public ResponseList(String msg){
        super(msg);
    }
    public ResponseList(){
        super();
    }

    public ResponseList(List<T> value){
        super();
        this.value = value;
    }

    public ResponseList(List<T> value, String msg){
        super(msg);
        this.value = value;
    }

    public List<T> getValue() {
        return value;
    }
}
