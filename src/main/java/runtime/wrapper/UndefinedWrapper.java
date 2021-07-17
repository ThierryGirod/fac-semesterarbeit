package runtime.wrapper;

import java.util.ArrayList;
import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;
import runtime.Instruction;

public class UndefinedWrapper implements Instruction {

    @Override
    public Instruction accept(final Visitor visitor, final Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public List<ParsetreeItem> getParsetree(final int level) {
        final var list = new ArrayList<ParsetreeItem>();
        list.add(new ParsetreeItem(this, level));
        list.add(new ParsetreeItem("undefined", level));
        return list;
    }

    @Override
    public String toString() {
        return "undefined";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        return o instanceof UndefinedWrapper;
    }

    @Override
    public int hashCode() {
        return 31 * 17 - 1;
    }

}
