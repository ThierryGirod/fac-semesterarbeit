package runtime.function;

import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

import java.util.ArrayList;
import java.util.List;

import context.Context;

public class PrintInstruction implements Instruction {
    private final ArgumentList arguments;

    public PrintInstruction(final ArgumentList arguments) {
        this.arguments = arguments;
    }

    public ArgumentList getArguments() {
        return arguments;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("Print: ", level));
        list.addAll(arguments.getParsetree(level + 1));
        return list;
    }
}
