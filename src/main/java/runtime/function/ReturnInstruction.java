package runtime.function;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;
import runtime.wrapper.UndefinedWrapper;

public class ReturnInstruction implements Instruction {
    private final Instruction expression;

    public ReturnInstruction(final Instruction expression) {
        this.expression = expression;
    }

    public ReturnInstruction() {
        this.expression = new UndefinedWrapper();
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
        if (expression == null) {
            list.add(new ParsetreeItem("empty return", level));
        } else {
            list.addAll(expression.getParsetree(level + 1));
        }
        return list;
    }

    @Override
    public String toString() {
        return "return " + expression;
    }
}
