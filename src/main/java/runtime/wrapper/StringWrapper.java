package runtime.wrapper;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class StringWrapper implements Instruction {
    public final String value;

    public StringWrapper(final String value) {
        this.value = value;
    }

    public String getString() {
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
        list.add(new ParsetreeItem("'" + value + "'", level));
        return list;
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StringWrapper)) {
            return false;
        }
        final var sw = (StringWrapper) o;
        return sw.value.equals(value);
    }

    @Override
    public int hashCode() {
        return 31 * 17 + value.hashCode();
    }
}
