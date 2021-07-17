package runtime.conditional;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class BooleanWrapper implements Instruction {
    private final boolean value;

    public BooleanWrapper(final boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem(String.valueOf(value), level));
        return list;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BooleanWrapper)) {
            return false;
        }
        final var bw = (BooleanWrapper) o;
        return bw.value == value;
    }

    @Override
    public int hashCode() {
        return 31 * 17 + (value ? 1 : 0);
    }
}
