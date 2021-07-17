package runtime.function;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class ParameterList implements Instruction {
    private final List<String> parameters;

    public ParameterList(final String parameter) {
        parameters = new ArrayList<>();
        parameters.add(parameter);
    }

    public ParameterList() {
        parameters = new ArrayList<>();
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void add(final String parameter) {
        parameters.add(parameter);
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        for (String param : parameters) {
            list.add(new ParsetreeItem(param, level));
        }
        if (parameters.isEmpty()) {
            list.add(new ParsetreeItem("[empty]", level));
        }
        return list;
    }

    public int size() {
        return parameters.size();
    }
}
