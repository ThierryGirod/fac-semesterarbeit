package runtime.loop;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;
import runtime.main.InstructionList;

public class LoopInstruction implements Instruction {

    private final Instruction condition;
    private final InstructionList block;

    public LoopInstruction(final Instruction condition, final InstructionList block) {
        this.condition = condition;
        this.block = block;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    public InstructionList getBlock() {
        return block;
    }

    public Instruction getCondition() {
        return condition;
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("Condition", level));
        list.addAll(condition.getParsetree(level + 1));
        list.add(new ParsetreeItem("Block", level));
        list.addAll(block.getParsetree(level + 1));
        return list;
    }

}
