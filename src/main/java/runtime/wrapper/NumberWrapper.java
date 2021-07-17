package runtime.wrapper;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class NumberWrapper implements Instruction {
    private final Double value;

    public NumberWrapper(final Double value) {
        this.value = value;
    }

    public Double getValue() {
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
        if (!(o instanceof NumberWrapper)) {
            return false;
        }
        final var nw = (NumberWrapper) o;
        return Double.compare(value, nw.value) == 0;
    }

    @Override
    public int hashCode() {
        final var valueLong = Double.doubleToLongBits(value);
        return 31 * 17 + (int) (valueLong ^ (valueLong >>> 32));
    }
}
