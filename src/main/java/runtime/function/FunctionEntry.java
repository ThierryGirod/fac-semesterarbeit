package runtime.function;

import java.util.List;

import runtime.Instruction;

public class FunctionEntry {
    private final List<String> parameters;

    public FunctionEntry(final List<String> parameters, final Instruction functionBody) {
        this.parameters = parameters;
        this.functionBody = functionBody;
    }

    private final Instruction functionBody;

    public Instruction getFunctionBody() {
        return functionBody;
    }

    public List<String> getParameters() {
        return parameters;
    }

}
