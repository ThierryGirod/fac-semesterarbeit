package runtime.function;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class FunctionCallInstruction implements Instruction {
    private final String identifier;
    private final ArgumentList arguments;

    public FunctionCallInstruction(final String identifier) {
        this.identifier = identifier;
        this.arguments = new ArgumentList();
    }

    public FunctionCallInstruction(final String identifier, final ArgumentList arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public ArgumentList getArguments() {
        return arguments;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("Identifier: " + identifier, level));
        list.addAll(arguments.getParsetree(level + 1));
        return list;
    }
}
