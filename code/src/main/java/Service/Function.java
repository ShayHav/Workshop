package Service;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

class Function {
    public String name;
    public Object[] args;

    public Function() {
    }

    @JsonGetter("function")
    public String getName() {
        return name;
    }

    @JsonGetter("args")
    public Object[] getArgs() {
        return args;
    }

    @JsonSetter("function")
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter("args")
    public void setArgs(Object[] args) {
        this.args = args;
    }

}
