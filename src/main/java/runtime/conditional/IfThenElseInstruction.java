package runtime.conditional;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;
import runtime.main.InstructionList;

public class IfThenElseInstruction implements Instruction {
    private final Instruction condition;
    private final InstructionList trueBlock;
    private final InstructionList falseBlock;

    public IfThenElseInstruction(final Instruction condition, final InstructionList trueBlock, final InstructionList falseBlock) {
        this.condition = condition;
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    public Instruction getCondition() {
        return condition;
    }

    public InstructionList getFalseBlock() {
        return falseBlock;
    }

    public InstructionList getTrueBlock() {
        return trueBlock;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("Condition", level));
        list.addAll(condition.getParsetree(level + 1));
        list.add(new ParsetreeItem("If True", level));
        list.addAll(trueBlock.getParsetree(level + 1));
        list.add(new ParsetreeItem("If False", level));
        list.addAll(falseBlock.getParsetree(level + 1));
        return list;
    }
}
