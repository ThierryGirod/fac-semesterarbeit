package runtime.main;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class AssignInstruction implements Instruction {
    private final String identifier;
    private final Instruction expression;

    public AssignInstruction(final String identifier, final Instruction expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Instruction getExpression() {
        return expression;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("identifier: " + identifier, level));
        list.add(new ParsetreeItem("Value:", level));
        list.addAll(expression.getParsetree(level + 1));
        return list;
    }
}
