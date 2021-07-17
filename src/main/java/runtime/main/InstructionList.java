package runtime.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class InstructionList implements Instruction {

    private final List<Instruction> instructions;

    public InstructionList(final List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public InstructionList() {
        this.instructions = new ArrayList<>();
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void add(final Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        for (Instruction i : instructions) {
            list.addAll(i.getParsetree(level + 1));
        }
        return list;
    }

    @Override
    public String toString() {
        return instructions.stream().map(Instruction::toString).collect(Collectors.joining(", "));
    }
}
