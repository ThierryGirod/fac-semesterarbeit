package runtime.function;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class ArgumentList implements Instruction {
    private final List<Instruction> arguments;

    public ArgumentList(final Instruction argument) {
        arguments = new ArrayList<>();
        arguments.add(argument);
    }

    public ArgumentList() {
        arguments = new ArrayList<>();
    }

    public List<Instruction> getArguments() {
        return arguments;
    }

    public void add(final Instruction argument) {
        arguments.add(argument);
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        for (Instruction arg : arguments) {
            list.addAll(arg.getParsetree(level + 1));
        }
        if (arguments.isEmpty()) {
            list.add(new ParsetreeItem("[empty]", level));
        }
        return list;
    }

    public int size() {
        return arguments.size();
    }
}
