package runtime.function;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;
import runtime.main.InstructionList;

public class FunctionDefinitionInstruction implements Instruction {
    private final String identifier;
    private final ParameterList parameters;
    private final InstructionList block;

    public FunctionDefinitionInstruction(final String identifier, final InstructionList block) {
        this.identifier = identifier;
        this.parameters = new ParameterList();
        this.block = block;
    }

    public FunctionDefinitionInstruction(final String identifier, final ParameterList parameters,
            final InstructionList block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
    }

    public ParameterList getParameters() {
        return parameters;
    }

    public InstructionList getBlock() {
        return block;
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
        list.add(new ParsetreeItem("Parameters:", level));
        list.addAll(parameters.getParsetree(level + 1));
        list.add(new ParsetreeItem("Block:", level));
        list.addAll(block.getParsetree(level + 1));
        return list;
    }
}
