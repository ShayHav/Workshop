package Service;

import java.util.List;

public class StateFile {
    private List<Function> functions;

    public StateFile(){}

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }
}
