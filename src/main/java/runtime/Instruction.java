package runtime;

import java.util.List;

import context.Context;
import interpreter.Visitor;
import parser.parsetree.ParsetreeItem;

public interface Instruction {
    Instruction accept(final Visitor visitor, final Context context);

    List<ParsetreeItem> getParsetree(final int level);
}
