package runtime.conditional;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class BooleanInstruction implements Instruction {
    private final BooleanOperator operator;
    private final Instruction left;
    private final Instruction right;

    public BooleanInstruction(final BooleanOperator operator, final Instruction left, final Instruction right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public BooleanOperator getOperator() {
        return operator;
    }

    public Instruction getRight() {
        return right;
    }

    public Instruction getLeft() {
        return left;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("Operator: " + operator.toString(), level));
        list.add(new ParsetreeItem("Left", level));
        list.addAll(left.getParsetree(level + 1));
        list.add(new ParsetreeItem("Right", level));
        list.addAll(right.getParsetree(level + 1));
        return list;
    }
}
