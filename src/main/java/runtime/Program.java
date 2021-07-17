package runtime;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.main.InstructionList;


public class Program implements Instruction {
    private final InstructionList block;

    public Program(final InstructionList block) {
        this.block = block;
    }

    public InstructionList getBlock() {
        return block;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.addAll(block.getParsetree(level + 1));
        return list;
    }
}
