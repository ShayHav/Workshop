package Service;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

class Function {
    public String function;
    public Object[] args;

    public Function() {
    }

    @JsonGetter("function")
    public String getFunction() {
        return function;
    }

    @JsonGetter("args")
    public Object[] getArgs() {
        return args;
    }

    @JsonSetter("function")
    public void setFunction(String function) {
        this.function = function;
    }

    @JsonSetter("args")
    public void setArgs(Object[] args) {
        this.args = args;
    }

}
